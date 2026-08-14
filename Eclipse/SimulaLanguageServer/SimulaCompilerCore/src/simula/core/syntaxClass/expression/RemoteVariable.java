/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.expression;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.classfile.constantpool.FieldRefEntry;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.CoreGlobal2;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.token.Identifier;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.ArrayDeclaration;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.Declaration;
import simula.core.syntaxClass.declaration.Parameter;
import simula.core.syntaxClass.declaration.ProcedureDeclaration;
import simula.core.syntaxClass.declaration.StandardClass;
import simula.core.syntaxClass.declaration.StandardProcedure;
import simula.core.syntaxClass.declaration.UndefinedDeclaration;
import simula.core.syntaxClass.declaration.VirtualSpecification;
import simula.core.utilities.Meaning;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.RTS;
import simula.core.utilities.Util;

/// Remote Variable.
/// 
/// <pre>
/// 
/// Syntax:
/// 
///   remote-variable = expression  DOT  variable
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/expression/RemoteVariable.java">
/// <b>Source File</b></a>.
/// 
/// @author Øystein Myhre Andersen
public final class RemoteVariable extends Expression {
	
	/// The remote attribute's semantic meaning.
	/// Set by doChecking.
	private Meaning remoteAttribute; // Set by doChecking
	
	/// Call Remote Procedure. If the remoteAttribute is declared as a ProcedureDeclaration 'callRemoteProcedure' is the procedure to be called.
	/// Set by doChecking.
	private ProcedureDeclaration callRemoteProcedure = null;  // Set by doChecking
	
	/// Call Remote Virtual. If the remoteAttribute is declared as a VirtualSpecification 'callRemoteVirtual' is the procedure to be called.
	/// Set by doChecking.
	private VirtualSpecification callRemoteVirtual = null;  // Set by doChecking
	
	/// The object expression before the dot.
	public Expression obj;
	
	/// The variable after the dot.
	public VariableExpression var;

	/// Used to indicate access remote array. Set by doChecking.
	private boolean accessRemoteArray = false;

	/// Create a new RemoteVariable
	/// @param obj object expression
	/// @param var the variable
	RemoteVariable(final SimulaBuilder simBuilder, final Expression obj, final VariableExpression var) {
		super(simBuilder);
		this.obj = obj;
		this.var = var;
		obj.backLink = var.backLink = this;
	}

	@Override
	public VariableExpression getWriteableVariable() {
		return (var);
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		if (Option.internal.TRACE_CHECKER)
			Util.TRACE("BEGIN RemoteVariable" + toString() + ".doChecking - Current Scope Chain: " + CoreGlobal.getCurrentScope().edScopeChain());
		this.type = doRemoteChecking(obj, var);
		if (Option.internal.TRACE_CHECKER) Util.TRACE("END RemoteVariable" + toString() + ".doChecking - Result type=" + this.type);
		SET_SEMANTICS_CHECKED();
	}

	/// Perform semantic checking
	/// @param obj remote object
	/// @param attr remote attribute
	/// @return the attribute's type
	private Type doRemoteChecking(final Expression obj, final Expression attr) {
		CoreGlobal.sourceLineNumber = firstLineNumber();
		Type result;
		obj.doChecking();
		Type objType = obj.type;
		if(objType == null) {
			Util.semanticError(obj, "doRemoteChecking: Object Expression (" + obj + ") has no type");
			return Type.Undef;
		}
		if (objType.keyWord == Type.T_TEXT)
			return (doRemoteTextChecking(obj, attr));

		objType.doChecking(CoreGlobal.getCurrentScope(), obj); // Nødvendig hvis TypeDeclaration er nedenfor
		ClassDeclaration qual = objType.getQual();
		if (qual == null) {
			if(objType.keyWord != Type.T_UNDEF)
				Util.semanticError(obj, "doRemoteChecking: Object Expression (" + obj + ") is not a ref() type rather " + objType);
		} else if (qual.hasLocalClasses) {
			if (CoreGlobal2.EXTENSIONS)
				 Util.warning(obj, "Illegal remote access into object of class with local classes.");
			else Util.semanticError(obj, "Illegal remote access into object of class with local classes.");
		}

		if (attr instanceof VariableExpression var) {
			Identifier ident = var.identifier;
			qual = objType.getQual();
			if(qual!=null) remoteAttribute = qual.findRemoteAttributeMeaning(ident);
			if (remoteAttribute == null) {
				if(objType.keyWord != Type.T_UNDEF)
					Util.semanticError(obj, "RemoteVariable.doRemoteChecking: " + ident.value + " is not an attribute of "	+ objType.getRefIdent().value);
				UndefinedDeclaration undef = new UndefinedDeclaration(null, ident);
				remoteAttribute = new Meaning(undef, CoreGlobal.getCurrentScope()); // Error Recovery
			}
			var.setRemotelyAccessed(remoteAttribute);

			Declaration declaredAs = remoteAttribute.declaredAs;
			result = declaredAs.type;
			if (declaredAs instanceof Parameter par) {
				if (par.kind == Parameter.Kind.Array)
					accessRemoteArray = true;
			}

			if (declaredAs instanceof ArrayDeclaration) { // Array
				if (var.hasArguments())	accessRemoteArray = true;
			} else if (declaredAs instanceof ProcedureDeclaration proc) { // Procedure
				callRemoteProcedure = proc;
			} else if (declaredAs instanceof VirtualSpecification virSpec) { // Virtual Procedure
				callRemoteVirtual = virSpec;
				if(virSpec.procedureSpec != null)
					result = virSpec.procedureSpec.type;
				return (result);
			}
		} else {
			Util.semanticError(obj, "Illegal attribute(" + attr + ") in remote access");
			result = attr.type;
		}
		return (result);
	}

	/// Perform semantic checking
	/// @param obj remote object
	/// @param attr remote attribute
	/// @return the attribute's type
	private Type doRemoteTextChecking(final Expression obj, final Expression attr) {
		Type result;
		if (attr instanceof VariableExpression var) { // Covers FunctionDesignator and SubscriptedVariable since they are subclasses
			Identifier ident = var.identifier;
			Meaning meaning = StandardClass.typeText.findMeaning(ident);
//			IO.println("RemoteVatiable.doRemoteTextChecking: meaning=" + meaning.declaredAs.getClass().getSimpleName());
			if (meaning.declaredAs instanceof UndefinedDeclaration) {
				Util.semanticError(obj, "RemoteVariable.doRemoteTextChecking: " + ident + " is not a Text attribute");
				return Type.Undef;
			}
			var.setRemotelyAccessed(meaning);
			callRemoteProcedure = (ProcedureDeclaration) meaning.declaredAs;
			result = meaning.declaredAs.type;

		} else {
			Util.semanticError(obj, "Illegal attribute(" + attr + ") in remote access");
			result = attr.type;
		}
		return (result);
	}

	// Returns true if this expression may be used as a statement.
	@Override
	public boolean maybeStatement() {
		ASSERT_SEMANTICS_CHECKED();
		return (var.maybeStatement());
	}

	@Override
	public String toJavaCode() {
		ASSERT_SEMANTICS_CHECKED();
		if (callRemoteProcedure != null)
			return (CallProcedure.remote(obj, callRemoteProcedure, var, backLink));
		else if (callRemoteVirtual != null)
			return (CallProcedure.remoteVirtual(obj, var, callRemoteVirtual));
		else if (accessRemoteArray)
			return (doAccessRemoteArray(obj, var));
		Expression constantElement=remoteAttribute.getConstant();
		if(constantElement != null) {
			if(constantElement instanceof Constant constant) return(constant.toJavaCode());
		}
		String result;
		if (remoteAttribute.foundBehindInvisible) {
			String remoteCast = remoteAttribute.foundIn.getJavaIdentifier();
			result = "((" + remoteCast + ")(" + obj.get() + "))." + var.get();
		} else result = obj.get() + '.' + var.get();
		return (result);
	}

	// ***********************************************************************
	// *** CODE: doAccessRemoteArray
	// ***********************************************************************
	/// Java Coding Utility: Edit remote array access.
	/// @param beforeDot expression before dot
	/// @param array the array variable
	/// @return the resulting Java source code
	private String doAccessRemoteArray(final Expression beforeDot, final VariableExpression array) {
		String obj = beforeDot.toJavaCode();
		String cast=array.type.toJavaArrayType();
		String var="(("+cast+')'+obj+'.'+array.edIdentifierAccess(false)+')';
		return(array.doGetELEMENT(var));
	}
	
	/// ClassFile Coding Utility: Build Access Remote Array.
	/// @param beforeDot expression before dot
	/// @param array the array variable
	/// @param codeBuilder the codeBuilder to use.
	private void doAccessRemoteArray(final Expression beforeDot, final VariableExpression array,CodeBuilder codeBuilder) {
		beforeDot.buildEvaluation(null, codeBuilder);
		Declaration declaredAs=array.meaning.declaredAs;
		if(declaredAs instanceof Parameter par) {
			ArrayDeclaration.arrayGetElement(type,par.getFieldIdentifier(),true,array.checkedParams,null,par.declaredIn,codeBuilder);
		} else if(declaredAs instanceof ArrayDeclaration) {
			array.buildEvaluation(null, codeBuilder);
		} else Util.IERR();;
	}

	/// ClassFile Coding Utility: Return the FieldRefEntry of this RemoteVariable
	/// @param pool the ConstantPoolBuilder to use
	/// @return the FieldRefEntry of this RemoteVariable
	public FieldRefEntry getFieldRefEntry(ConstantPoolBuilder pool) {
		ClassDeclaration cls=obj.type.getQual();
		String ident=var.meaning.declaredAs.getFieldIdentifier();
		return(pool.fieldRefEntry(cls.getClassDesc(), ident, type.toClassDesc()));
	}

	@Override
	public void buildEvaluation(Expression rightPart,CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		if(obj.type.keyWord == Type.T_TEXT) {
			callStandardTextProcedure(obj, (StandardProcedure)callRemoteProcedure, var, backLink, codeBuilder);
		} else if (callRemoteProcedure != null) {
			BuildCP.remote(obj, callRemoteProcedure, var, backLink,codeBuilder);
		} else if (callRemoteVirtual != null) {
			BuildCPV.remoteVirtual(obj, var, callRemoteVirtual, backLink, codeBuilder);
		} else if (accessRemoteArray) {
			doAccessRemoteArray(obj, var,codeBuilder);
		} else {
			Expression constantElement = remoteAttribute.getConstant();
			if (constantElement != null) {
				if(constantElement instanceof Constant constant) {
					constant.buildEvaluation(null, codeBuilder);
					return;
				}
			}
			// result = obj.get() + KeyWord.DOT.toJavaCode() + var.get();
			obj.buildEvaluation(null,codeBuilder);
			var.buildEvaluation(null,codeBuilder);
		}
	}


	// ********************************************************************
	// *** callStandardTextProcedure
	// ********************************************************************
	/// ClassFile coding utility: Call Standard TextProcedure.
	/// @param beforeDot expression before dot.
	/// @param pro the StandardProcedure.
	/// @param variable the VariableExpression.
	/// @param backLink if not null, this procedure call is part of the backLink Expression/Statement.
	/// @param codeBuilder the codeBuilder to use.
	private static void callStandardTextProcedure(Expression beforeDot,StandardProcedure pro,final VariableExpression variable, Object backLink,CodeBuilder codeBuilder) {
		beforeDot.buildEvaluation(null,codeBuilder);
		if(variable.checkedParams != null) 
			for(Expression expr:variable.checkedParams)
				expr.buildEvaluation(null,codeBuilder);

		codeBuilder.invokestatic(RTS.CD.RTS_TXT, pro.identifierValue(), pro.getMethodTypeDesc(beforeDot,variable.checkedParams));
		if(pro.type != null && backLink == null) {
			codeBuilder.pop();
		}
	}

	@Override
	public String toString() {
		return ("(" + obj + " DOT " + var + ")");
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private RemoteVariable() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeRemoteVariable: " + this);
		oupt.writeKind(ObjectKind.RemoteVariable);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** Expression
		oupt.writeType(type);
		oupt.writeObj(backLink);
		// *** RemoteVariable
		oupt.writeObj(obj);
		oupt.writeObj(var);
	}
	
	/// Read and return an object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the object read from the stream.
	/// @throws IOException if something went wrong.
	public static RemoteVariable readObject(AttributeInputStream inpt) throws IOException {
		RemoteVariable rem = new RemoteVariable();
		rem.OBJECT_SEQU = inpt.readSEQU(rem);
		// *** SyntaxElement
		rem.astData = readAstData(inpt);
		// *** SyntaxElement
		rem.type = inpt.readType();
		rem.backLink = (SyntaxElement) inpt.readObj();
		// *** RemoteVariable
		rem.obj = (Expression) inpt.readObj();
		rem.var = (VariableExpression) inpt.readObj();
		Util.TRACE_INPUT("readRemoteVariable: " + rem);
		return(rem);
	}


}
