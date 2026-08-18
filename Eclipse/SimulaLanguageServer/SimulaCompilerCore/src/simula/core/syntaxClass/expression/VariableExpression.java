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
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.util.Iterator;
import java.util.Vector;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.token.Identifier;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.OverLoad;
import simula.core.syntaxClass.ProcedureSpecification;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.ArrayDeclaration;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.ConnectionBlock;
import simula.core.syntaxClass.declaration.Declaration;
import simula.core.syntaxClass.declaration.DeclarationScope;
import simula.core.syntaxClass.declaration.InspectVariableDeclaration;
import simula.core.syntaxClass.declaration.LabelDeclaration;
import simula.core.syntaxClass.declaration.Parameter;
import simula.core.syntaxClass.declaration.ProcedureDeclaration;
import simula.core.syntaxClass.declaration.SimpleVariableDeclaration;
import simula.core.syntaxClass.declaration.StandardProcedure;
import simula.core.syntaxClass.declaration.SwitchDeclaration;
import simula.core.syntaxClass.declaration.UndefinedDeclaration;
import simula.core.syntaxClass.declaration.VirtualSpecification;
import simula.core.utilities.KeyWord;
import simula.core.utilities.Meaning;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.RTS;
import simula.core.utilities.Util;

/// Variable.
/// 
/// <pre>
/// 
/// Simula Standard: 3.1 Variables
/// Simula Standard: 3.1.3. Array elements
/// Simula Standard: 3.1.4. Function designators
/// 
/// variable = simple-variable | subscripted-variable | simple-object-expression . variable
/// 
///    simple-object-expression = NONE | variable | function-designator | object-generator
///                             | local-object | qualified-object | ( object-expression )
///                             
///    simple-variable = identifier
/// 
///    subscripted-variable = function-designator | array-element
/// 
///       function-designator = procedure-identifier ( [ actual-parameter-part ] )
///  
///          actual-parameter-part = actual-parameter { , actual-parameter }
///          
///             actual-parameter = expression | array-identifier1 
///                              | switch-identifier1 | procedure-identifier1
///                              
///                identifier1 = identifier | remote-identifier
///                
///                   remote-identifier = simple-object-expression . attribute-identifier
///                                     | text-primary . attribute-identifier
/// 
///       array-element = array-identifier [ subscript-list ]
///       
///          subscript-list = arithmetic-expression { , arithmetic-expression }
/// 
/// </pre>
/// <b>Function designators:</b>
/// 
/// A function designator defines a value which results through the application
/// of a given set of rules defined by a procedure declaration (see 5.4) to a
/// fixed set of actual parameters. The rules governing specification of actual
/// parameters are given in 4.6.
/// 
/// Note: Not every procedure declaration defines rules for determining the value
/// of a function designator (cf. 5.4.1).
/// 
/// <b>Array elements:</b>
/// 
/// Subscripted variables designate values which are components of multi-
/// dimensional arrays. Each arithmetic expression of the subscript list occupies
/// one subscript position of the subscripted variable and is called a subscript.
/// The complete list of subscripts is enclosed by the subscript parentheses ( ).
/// The array component referred to by a subscripted variable is specified by
/// the actual value of its subscripts.
/// 
/// Each subscript position acts like a variable of type integer and the
/// evaluation of the subscript is understood to be equivalent to an assignment
/// to this fictitious variable. The value of the subscripted variable is defined
/// only if the actual integer value of each subscript expression is within the
/// associated subscript bounds of the array. A subscript expression value
/// outside its associated bounds causes a run time error.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/expression/VariableExpression.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class VariableExpression extends Expression {

	/// The variable's identifier.
	public Identifier identifier;

	/// The meaning of this variable.
	public Meaning meaning;

	/// Indicates that this variable is remotely accessed.
	private boolean remotelyAccessed;

	/// The parsed parameters.
	Vector<Expression> params;

	/// The checked parameters set by doChecking.
	public Vector<Expression> checkedParams; // Set by doChecking

	/// Create a new Variable.
	/// @param identifier the variable's identifier
	public VariableExpression(final DocumentManager documentManager, final Identifier identifier) {
		super(documentManager);
		this.identifier = identifier;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("NEW Variable: " + identifier);
	}

	/// Returns true if this variable has arguments.
	/// @return true if this variable has arguments
	public boolean hasArguments() {
		return (params != null);
	}

	/// Returns a suitable java identifier for this variable.
	/// 
	/// @return a suitable java identifier
	public String getJavaIdentifier() {
		return (meaning.declaredAs.getJavaIdentifier());
	}

	/// This variable is remotely accessed through 'meaning'.
	/// @param meaning before dot
	void setRemotelyAccessed(final Meaning meaning) {
		this.meaning = meaning;
		remotelyAccessed = true;
		this.doChecking();
		SET_SEMANTICS_CHECKED(); // Checked as remote attribute
	}

	/// Returns the meaning.
	/// 
	/// If meaning is not set by setRemotelyAccessed then meaning by identifier will
	/// be set.
	/// @return the meaning
	public Meaning getMeaning() {
		if (meaning == null) {
			meaning = CoreGlobal.getCurrentScope().findMeaning(identifier);
		}
		return (meaning);
	}

	/// Parse Utility: Expect Variable.
	/// <pre>
	/// Variable  =  Identifier  |  SubscriptedVariable
	/// 	SubscriptedVariable  =  Identifier  "("  Expression  {  ,  Expression  }  ")"
	/// </pre>
	/// Precondition: Identifier  is already read.
	/// @param ident the variable identifier
	/// @return the created Variable
	public static VariableExpression expectVariable(final SimulaBuilder simBuilder, final Identifier identifier) {
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Parse Variable: current=" + Parse.getCurrentParserToken(simBuilder));
		VariableExpression variable = new VariableExpression(simBuilder.documentManager, identifier);
		if (Parse.accept(simBuilder, KeyWord.BEGPAR)) {
//			IO.println("VariableExpression.expectVariable: GOT BEGPAR");
			variable.params = new Vector<Expression>();
			do {
//				IO.println("VariableExpression.expectVariable: GOT BEGPAR OR COMMA");
				Expression par = acceptExpression(simBuilder);
				if (par == null)
					Util.syntaxError(simBuilder, "Missing procedure parameter");
				else{
					variable.params.add(par);
					par.backLink = variable;
				}
			} while (Parse.accept(simBuilder, KeyWord.COMMA));
			Parse.expect(simBuilder, KeyWord.ENDPAR);
//			IO.println("VariableExpression.expectVariable: GOT ENDPAR");
		}
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("NEW Variable: " + variable);
		return (variable);
	}

	@Override
	// Is redefined in Variable, RemoteVariable and TypeConversion
	public VariableExpression getWriteableVariable() {
		return (this);
	}

	@Override
	public void doChecking() {
//		IO.println("VariableExpression.deChecking BEGIN Variable(" + identifier.value + ").doChecking: type=" + type);
//		Util.IERR("");
		if (IS_SEMANTICS_CHECKED())
			return;
		if (Option.internal.TRACE_CHECKER)
			Util.TRACE("BEGIN Variable(" + identifier.value + ").doChecking: type=" + type);
		CoreGlobal.sourceLineNumber = firstLineNumber();
		Declaration declaredAs = getMeaning().declaredAs;
		if (declaredAs != null)
			this.type = declaredAs.type;

		if (type != null && this.type.getRefIdent() != null && meaning.declaredIn instanceof ConnectionBlock conn) {
			if (type != null)
				type = new Type(type, conn);
		}

		switch(declaredAs) {
			case UndefinedDeclaration undef -> {
				Util.semanticError(this, "Undefined variable: " + undef.identifierValue());
			}
			case StandardProcedure sproc -> {
				if (Util.equals(sproc.identifierValue(), "detach")) {
//					if (meaning.declaredIn instanceof ConnectionBlock conn)
//						conn.classDeclaration.detachUsed = true;
//					else if (meaning.declaredIn instanceof ClassDeclaration cdecl)
//						cdecl.detachUsed = true;
//					else
//						Util.semanticError(this, "Variable(" + identifierValue() + ").doChecking:INTERNAL ERROR, "
//								+ meaning.declaredIn.getClass().getSimpleName());
					switch(meaning.declaredIn) {
						case ConnectionBlock  conn  -> { conn.classDeclaration.detachUsed = true; }
						case ClassDeclaration cdecl -> { cdecl.detachUsed = true; }
						default -> { Util.semanticError(this, "Variable(" + identifier.value + ").doChecking:INTERNAL ERROR, "
									+ meaning.declaredIn.getClass().getSimpleName()); }
					}
				}
			}
			default -> {}
		}
		
		if (declaredAs != null)
			switch (declaredAs.declarationKind) {
			case ObjectKind.ArrayDeclaration:
				ArrayDeclaration array = (ArrayDeclaration) declaredAs;
				this.type = array.type;
				if(params != null) {
					// Check parameters
					if (params.size() != array.nDim)
						Util.semanticError(this, "Wrong number of indices to " + array);
					checkedParams = new Vector<Expression>();
					for (Expression actualParameter : params) {
						actualParameter.doChecking();
						Expression checkedParameter = TypeConversion.testAndCreate(documentManager, Type.Integer, actualParameter);
						checkedParameter.backLink = this;
						checkedParams.add(checkedParameter);
					}
				}
				break;

			case ObjectKind.Class:
//			case ObjectKind.PrefixedBlock:
			case ObjectKind.StandardClass:
			case ObjectKind.Procedure:
			case ObjectKind.ContextFreeMethod:
			case ObjectKind.MemberMethod:
				this.type = declaredAs.type;
				Type overloadedType = this.type;
				Iterator<Parameter> paramIterator = null;
				if (declaredAs instanceof ClassDeclaration cdecl) {
					paramIterator = cdecl.new ClassParameterIterator();
				} else if (declaredAs instanceof ProcedureDeclaration) {
					paramIterator = ((ProcedureDeclaration) declaredAs).parameterList.iterator();
					if(! documentManager.compileViaJavaSource) {
						if(declaredAs instanceof StandardProcedure prc) {
							if(prc.identifierValue().equalsIgnoreCase("histd")) ; // NOTHING
							else if(prc.identifierValue().equalsIgnoreCase("discrete")) ; // NOTHING
							else if(prc.identifierValue().equalsIgnoreCase("linear")) ; // NOTHING
							else {
								ProcedureSpecification overLoadMatch = prc.getOverLoadMatch(documentManager, params);
								if(overLoadMatch != null)
									overloadedType = overLoadMatch.type;
							}
						}
					}
				} else Util.IERR();
				if (params == null) {
					if(declaredAs.declarationKind != ObjectKind.Procedure) {
						if (paramIterator.hasNext())
							Util.semanticError(this, "Missing parameter(s) to " + ObjectKind.edit(declaredAs.declarationKind) + " " + declaredAs.identifierValue());
					} else {
						if(!(declaredAs instanceof SwitchDeclaration)) {
							if(backLink == null && paramIterator.hasNext())
								Util.semanticError(this, "Missing parameter(s) to Switch " + declaredAs.identifierValue());
						}
					}
				} else {
					// Check parameters
					checkedParams = new Vector<Expression>();
					Iterator<Expression> actualIterator = params.iterator();
					LOOP: while (actualIterator.hasNext()) {
						if (!paramIterator.hasNext()) {
							Util.semanticError(this, "Too many parameters to " + declaredAs.identifierValue());
							break LOOP;
						}
						Parameter formalParameter = (Parameter) paramIterator.next();
						Type formalType = formalParameter.type;
						Expression actualParameter = actualIterator.next();
						actualParameter.doChecking();
						if (formalType instanceof OverLoad) {
							if(identifier.value.equalsIgnoreCase("addepsilon") || identifier.value.equalsIgnoreCase("subepsilon")) {
								formalType = actualParameter.type; // AD'HOC for add/subepsilon
								overloadedType = formalType;
							}
						}
						
						if (formalParameter.kind == Parameter.Kind.Array) {
							if (formalType != null && (!formalType.equals(actualParameter.type))
									&& formalType.isArithmeticType())
								Util.semanticError(this, "Parameter Array " + actualParameter + " must be of Type " + formalType);
						} else {
							if(actualParameter instanceof VariableExpression pvar) {
								if(! pvar.hasArguments()) {
									Declaration pdecl = pvar.meaning.declaredAs;
									if(pdecl instanceof ArrayDeclaration)
										Util.semanticError(this, "Array identifier '" + actualParameter + "' as actual parameter does not match formal type " + formalParameter);
								}
							}
						}
						Expression checkedParameter = TypeConversion.testAndCreate(documentManager, formalType, actualParameter);
						checkedParameter.backLink = this;
						checkedParams.add(checkedParameter);
					}
					if (paramIterator.hasNext())
						Util.semanticError(this, "Missing parameter(s) to " + declaredAs.identifierValue());
				}
				if (type instanceof OverLoad)
					this.type = overloadedType;
				break;

			case ObjectKind.Parameter:
				Parameter spec = (Parameter) declaredAs;
				int kind = spec.kind;
				this.type = spec.type;
				if(params != null) {
					if (kind == Parameter.Kind.Array)
						spec.nDim = params.size();
					Iterator<Expression> actualIterator = params.iterator();
					checkedParams = new Vector<Expression>();
					while (actualIterator.hasNext()) {
						Expression actualParameter = actualIterator.next();
						actualParameter.doChecking();
						if (kind == Parameter.Kind.Array) {
							if (!actualParameter.type.isArithmeticType())
								Util.semanticError(this, "Illegal index-type");
							Expression checkedParameter = TypeConversion.testAndCreate(documentManager, Type.Integer, actualParameter);
							checkedParameter.backLink = this;
							checkedParams.add(checkedParameter);
						} else
							checkedParams.add(actualParameter);
					}
				}
				break;
			case ObjectKind.VirtualSpecification:
				VirtualSpecification vspec = (VirtualSpecification) declaredAs;
				this.type = vspec.type;
				if(params != null) {
					Iterator<Expression> pactualIterator = params.iterator();
					checkedParams = new Vector<Expression>();
					while (pactualIterator.hasNext()) {
						Expression actualParameter = pactualIterator.next();
						actualParameter.doChecking();
						if(checkedParams == null) checkedParams = new Vector<Expression>();
						checkedParams.add(actualParameter);
					}
				}
				break;

			case ObjectKind.SimpleVariableDeclaration:
			case ObjectKind.UndefinedDeclaration:
				if(params != null) {
//					IO.println("VariableExpression.doChecking: " + ObjectKind.edit(declaredAs.declarationKind) + " " + declaredAs);
					Util.semanticError(this, "Illegal subscription of variable " + this.identifier.value);
				}
				break;
				
			case ObjectKind.ExternalDeclaration:
			case ObjectKind.InspectVariableDeclaration:
			case ObjectKind.LabelDeclaration:
				break;
				
			default:
				Util.IERR("VariableExpression.doChecking: END Variable(" + identifier.value + ").doChecking: type=" + type + ", kind=" + ObjectKind.edit(declaredAs.declarationKind));
			}

		if (Option.internal.TRACE_CHECKER)
			Util.TRACE("END Variable(" + identifier.value + ").doChecking: type=" + type + ", kind=" + ObjectKind.edit(declaredAs.declarationKind));
//		IO.println("VariableExpression.doChecking: DONE: " + ObjectKind.edit(declaredAs.declarationKind) + ": " + declaredAs);
		SET_SEMANTICS_CHECKED();
	}

	// Returns true if this variable may be used as a statement.
	@Override
	public boolean maybeStatement() {
		ASSERT_SEMANTICS_CHECKED();
		if (meaning == null)
			return (false); // Error Recovery
		Declaration declaredAs = meaning.declaredAs;
		if (declaredAs == null)
			return (false); // Error Recovery
		switch (declaredAs.declarationKind) {
			case ObjectKind.Procedure:
//			case ObjectKind.Switch:
			case ObjectKind.ContextFreeMethod:
			case ObjectKind.MemberMethod:
				return (true);
				
			case ObjectKind.Parameter:
				Parameter par = (Parameter) declaredAs;
				return (par.kind == Parameter.Kind.Procedure);
			case ObjectKind.VirtualSpecification:
				VirtualSpecification vir = (VirtualSpecification) declaredAs;
				return (vir.kind == VirtualSpecification.Kind.Procedure);
			default:
		}
		return (false); // Variable, Parameter, Array, Class, Switch
	}

	// ******************************************************************
	// *** Coding: toJavaCode
	// ******************************************************************
	@Override
	public String toJavaCode() {
		ASSERT_SEMANTICS_CHECKED();
		return (get());
	}

	// ******************************************************************
	// *** Coding: doGetELEMENT
	// ******************************************************************
	/// Java coding utility: doGetELEMENT.
	/// @param var the variable
	/// @return a suitable java code
	public String doGetELEMENT(final String var) {
		StringBuilder s = new StringBuilder();
		s.append(var);
		char sep = '(';
		// A.getELEMENT(i);
		s.append(".getELEMENT");
		for (Expression ix : checkedParams) {
			String index = ix.toJavaCode();
			s.append(sep).append(index);
			sep = ',';
		}
		s.append(")");
		return (s.toString());
	}

	// ******************************************************************
	// *** Coding: doPutELEMENT
	// ******************************************************************
	/// Java coding utility: doPutELEMENT.
	/// @param var       the variable
	/// @param rightPart the right hand side
	/// @return a suitable java code
	public String doPutELEMENT(final String var, final String rightPart) {
		StringBuilder s = new StringBuilder();
		s.append(var);
		char sep = '(';
		// A.putELEMENT(rightPart,i);
		s.append(".putELEMENT(").append(var).append(".index");
		for (Expression ix : checkedParams) {
			String index = ix.toJavaCode();
			s.append(sep).append(index);
			sep = ',';
		}
		s.append("),").append(rightPart).append(")");
		return (s.toString());
	}

	// ******************************************************************
	// *** Coding: put
	// ******************************************************************
	// Generate code for putting an value(expression) into this Variable
	@Override
	public String put(final String rightPart) {
		ASSERT_SEMANTICS_CHECKED();
		String edited = this.editVariable(documentManager.simCoder, rightPart); // Is a Destination
		return (edited);
	}

	// ******************************************************************
	// *** Coding: get
	// ******************************************************************
	// Generate code for getting the value of this Variable
	@Override
	public String get() {
		ASSERT_SEMANTICS_CHECKED();
		String rightPart = null;
		String result = this.editVariable(documentManager.simCoder, rightPart); // Not a destination
		return (result);
	}

	// ******************************************************************
	// *** Coding: editVariable
	// ******************************************************************
	/// Java Coding Utility: Edit this Variable.
	/// @param rightPart When destination, this is the right part of the assignment
	/// @return the resulting Java source code
	private String editVariable(final SimulaCoder simCoder, final String rightPart) {
		ASSERT_SEMANTICS_CHECKED();
		boolean destination = (rightPart != null);
		if(meaning == null) Util.IERR("NO MEANING: " + identifier.value);
		Declaration declaredAs = meaning.declaredAs;
//		IO.println("VariableExpression.editVariable: " + identifierValue()+" "+meaning);
//		ASSERT_SEMANTICS_CHECKED();
		StringBuilder s;
		switch (declaredAs.declarationKind) {
			case ObjectKind.ArrayDeclaration:
				s = new StringBuilder();
				if (this.hasArguments()) { // Array Element Access
					String var = edIdentifierAccess(false);
					if (rightPart != null)
						return (doPutELEMENT(var, rightPart));
					else
						return (doGetELEMENT(var));
				} else {
					if (rightPart != null) {
						s.append(edIdentifierAccess(false)).append('=').append(rightPart);
					} else
						s.append(edIdentifierAccess(false));
				}
				return (s.toString());
	
			case ObjectKind.Class:
			case ObjectKind.StandardClass:
				Util.codingError(this, "Illegal use of class identifier: " + declaredAs.identifierValue());
				return (edIdentifierAccess(destination));
	
			case ObjectKind.LabelDeclaration:
				if (rightPart != null)
					Util.IERR();
				VirtualSpecification virtSpec = VirtualSpecification.getVirtualSpecification(declaredAs);
				if (virtSpec != null)
					return (edIdentifierAccess(virtSpec.getVirtualIdentifier(), destination));
				return (edIdentifierAccess(destination));
	
			case ObjectKind.Parameter:
				s = new StringBuilder();
				Parameter par = (Parameter) declaredAs;
				switch (par.kind) {
				case Parameter.Kind.Array: // Parameter Array
					String var = edIdentifierAccess(false);
					if (par.mode == Parameter.Mode.name)
						var = var + ".get()";
					if (this.hasArguments()) {
						String arrType = type.toJavaArrayType();
						String castedVar = "((" + arrType + ")" + var + ")";
						if (rightPart != null)
							return (doPutELEMENT(castedVar, rightPart));
						else
							return (doGetELEMENT(castedVar));
					} else {
						if (rightPart != null) {
							s.append(var).append('=').append(rightPart);
						} else {
							s.append(var);
						}
					}
					break;
				case Parameter.Kind.Procedure: // Parameter Procedure
					if (destination)
						Util.IERR();
					Expression inspectedVariable = meaning.getTypedInspectedVariable();
					if (inspectedVariable != null)
						s.append(inspectedVariable.toJavaCode()).append('.');
					if (par.mode == Parameter.Mode.value)
						Util.codingError(this, "Parameter " + this + " by Value is not allowed - Rewrite Program");
					else // Procedure By Reference or Name.
						s.append(CallProcedure.formal(simCoder, this, par));
					if (rightPart != null) {
						s.append('=').append(rightPart);
					}
					break;
				case Parameter.Kind.Simple:
				case Parameter.Kind.Label:
					var = edIdentifierAccess(destination); // Kind: Simple/Label
					if (!destination && par.mode == Parameter.Mode.name) {
						s.append(var).append(".get()");
					} else if (rightPart != null) {
						if (par.mode == Parameter.Mode.name) {
							s.append(var + ".put(" + rightPart + ')');
						} else
							s.append(var).append('=').append(rightPart);
					} else {
						s.append(edIdentifierAccess(destination)); // Kind: Simple/Label
					}
				}
				return (s.toString());
	
			case ObjectKind.ContextFreeMethod:
				// Standard Library Procedure
				if (Util.equals(identifier.value, "sourceline")) {
					int lno = this.firstLineNumber() + 1;
					if(lno <= 0) Util.IERR("VariableExpressiopn.editVariable: Illegal lineNumber: " + lno);
					return "" + lno;
				}
				if (destination) {
					Util.IERR();
					return ("_RESULT=" + rightPart);
				}
				return (CallProcedure.asStaticMethod(this, true));
	
			case ObjectKind.MemberMethod:
				if (destination) {
					Util.IERR();
					return ("_RESULT=" + rightPart);
				}
				return (CallProcedure.asNormalMethod(this));
	
			case ObjectKind.Procedure:
				// This Variable is a Procedure-Identifier.
				// When 'destination' it is a variable used to carry the resulting value until the final return.
				// otherwise; it is a ordinary procedure-call.
				if (destination) { // return("_RESULT");
					ProcedureDeclaration proc = (ProcedureDeclaration) meaning.declaredAs;
					if (proc.getRTBlockLevel() == CoreGlobal.getCurrentScope().getRTBlockLevel()) {
						return "_RESULT" + "=" + rightPart;
					} else {
						String cast = proc.getJavaIdentifier();
						return "((" + cast + ")" + proc.edCTX() + ")._RESULT" + "=" + rightPart;
					}
				} else {
					ProcedureDeclaration procedure = (ProcedureDeclaration) declaredAs;
					if (procedure.myVirtual != null)
						return CallProcedure.virtual(simCoder, this, procedure.myVirtual.virtualSpec, remotelyAccessed);
					else
						return CallProcedure.normal(this);
				}
	
			case ObjectKind.SimpleVariableDeclaration:
			case ObjectKind.InspectVariableDeclaration:
				if (rightPart != null)
					return edIdentifierAccess(destination) + '=' + rightPart;
				else
					return edIdentifierAccess(destination);
	
			case ObjectKind.VirtualSpecification:
				if (rightPart != null)
					Util.IERR();
				VirtualSpecification virtual = (VirtualSpecification) declaredAs;
				return CallProcedure.virtual(simCoder, this, virtual, remotelyAccessed);

			case ObjectKind.UndefinedDeclaration:
				if (rightPart != null)
					return declaredAs.toString() + '=' + rightPart;
				else
					return declaredAs.toString();
	
			default:
				Util.IERR(""+ObjectKind.edit(declaredAs.declarationKind));
		}
		return null;

	}

	// ***********************************************************************
	// *** Coding: edIdentifierAccess
	// ***********************************************************************
	/// Java coding utility: Edit identifier access.
	/// @param destination true if this variable is a destination
	/// @return a suitable java code
	public String edIdentifierAccess(boolean destination) {
		Declaration declaredAs = meaning.declaredAs;
		String id = declaredAs.getJavaIdentifier();
		String res = edIdentifierAccess(id, destination);
		return (res);
	}

	/// Java Coding Utility: Edit identifier access.
	/// @param id the identifier
	/// @param destination true if destination
	/// @return a suitable java code
	private String edIdentifierAccess(String id, boolean destination) {
		Expression constantElement = meaning.getConstant();
		if (constantElement != null) {
			if (constantElement instanceof Constant constant) {
				return (constant.toJavaCode());
			}
		}
		if (remotelyAccessed) {
			return (id);
		}
		if (meaning.isConnected()) {
			Expression inspectedVariable = ((ConnectionBlock) meaning.declaredIn).getTypedInspectedVariable();
			if (meaning.foundBehindInvisible) {
				String remoteCast = meaning.foundIn.getJavaIdentifier();
				id = "((" + remoteCast + ")(" + inspectedVariable.toJavaCode() + "))." + id;
			} else {
				id = inspectedVariable.toJavaCode() + "." + id;
			}
		} else if (!(meaning.declaredIn.declarationKind == ObjectKind.ContextFreeMethod
				|| meaning.declaredIn.declarationKind == ObjectKind.MemberMethod)) {
			
			String cast = meaning.declaredIn.getJavaIdentifier();
			int n = meaning.declaredIn.getRTBlockLevel();
			if (meaning.foundBehindInvisible)
				cast = meaning.foundIn.getJavaIdentifier();
			else if (n == CoreGlobal.getCurrentScope().getRTBlockLevel())
				return (id); // currentScope may be a sub-block
			id = "((" + cast + ")" + meaning.declaredIn.edCTX() + ")." + id;
		}
		return (id);
	}


	/// ClassFile Coding Utility: Edit identifier access.
	/// @param destination true if destination
	/// @param codeBuilder the CodeBuilder
	public void buildIdentifierAccess(final SimulaCoder simCoder, final boolean destination, final CodeBuilder codeBuilder) {
		if (remotelyAccessed) return;
		meaning.buildIdentifierAccess(simCoder, destination, codeBuilder);
	}

	// ******************************************************************
	// *** Coding: buildEvaluation
	// ******************************************************************
	/// ClassFile Coding Utility: Build this Variable.
	/// @param rightPart When destination, this is the right part of the assignment
	/// @param codeBuilder the CodeBuilder
	@Override
	public void buildEvaluation(final SimulaCoder simCoder, final Expression rightPart, final CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		Declaration declaredAs=meaning.declaredAs;
		ConstantPoolBuilder pool=codeBuilder.constantPool();
		boolean destination = (rightPart != null);
		VariableExpression inspectedVariable = meaning.getInspectedVariable();
		switch (declaredAs.declarationKind) {
			case ObjectKind.ArrayDeclaration:
				ArrayDeclaration arr=(ArrayDeclaration)declaredAs;
				buildIdentifierAccess(simCoder, false, codeBuilder);
				if (this.hasArguments())
					 arr.arrayGetElement(simCoder, this, false, codeBuilder);
				else codeBuilder.getfield(pool.fieldRefEntry(arr.declaredIn.getClassDesc(), arr.identifierValue(), ArrayDeclaration.getClassDesc(type)));
				break;

			case ObjectKind.Class:
			case ObjectKind.StandardClass:
				Util.codingError(this, "Illegal use of class identifier: " + declaredAs.identifierValue());
				break;

			case ObjectKind.LabelDeclaration:
				if (destination) Util.IERR();
				buildIdentifierAccess(simCoder, false, codeBuilder);
				LabelDeclaration lab=(LabelDeclaration)declaredAs;
				VirtualSpecification virtSpec = VirtualSpecification.getVirtualSpecification(declaredAs);
				if (virtSpec == null) {
					codeBuilder.getfield(lab.getFieldRefEntry(pool));
				} else {
					String ident = virtSpec.getSimpleVirtualIdentifier();
					ClassDesc owner = virtSpec.declaredIn.getClassDesc();
					codeBuilder.invokevirtual(owner, ident, MethodTypeDesc.ofDescriptor("()Lsimula/runtime/RTS_LABEL;"));
				}
				break;

			case ObjectKind.Parameter:
				buildEvaluateParameter(simCoder, (Parameter) declaredAs,inspectedVariable,rightPart,codeBuilder);
				break;

			case ObjectKind.ContextFreeMethod:
				if (Util.equals(identifier.value, "sourceline")) {
					int lno = this.firstLineNumber() + 1;
					if(lno <= 0) Util.IERR("VariableExpressiopn.buildEvaluation: Illegal lineNumber: " + lno);
					Constant.buildIntConst(codeBuilder, lno);
				}
				else BuildCP.staticStandardProcedure(simCoder, this, codeBuilder);
				break;

			case ObjectKind.MemberMethod:
				BuildCP.normalStandardProcedure(simCoder, this, codeBuilder);
				break;

			case ObjectKind.Procedure:
//			case ObjectKind.Switch:
				ProcedureDeclaration procedure = (ProcedureDeclaration) declaredAs;
				if (destination) {
					codeBuilder
						.aload(0)
						.getfield(procedure.result.getFieldRefEntry(pool));
					Util.IERR();
				} //else
				if (procedure.myVirtual != null)
					 BuildCPV.virtual(simCoder, this, procedure.myVirtual.virtualSpec, remotelyAccessed, codeBuilder);
				else {
					BuildCP.normal(simCoder, this, procedure, codeBuilder);
				}
				break;

			case ObjectKind.SimpleVariableDeclaration:
				SimpleVariableDeclaration var=(SimpleVariableDeclaration)declaredAs;
				if(var.constantElement != null) {
					var.constantElement.buildEvaluation(simCoder, null,codeBuilder);
					break;
				}
				if(inspectedVariable != null) {
					ConnectionBlock cblk=(ConnectionBlock)meaning.declaredIn;
					boolean withFollowSL = meaning.declaredIn.buildCTX(codeBuilder);
					if(withFollowSL) {
						DeclarationScope declaredIn = cblk.declaredIn;
						int bl = declaredIn.getRTBlockLevel();
						if(bl == 0) { // Accessing _USR
//							ClassDesc main = Global.programModule.mainModule.getClassDesc();
							
//							ClassDesc main = Global.currentModule.getSyntaxTree().mainModule.getClassDesc();
//							codeBuilder.checkcast(main);
							Util.IERR("SJEKK DETTE - BRUK SourceDocumentItem.getSyntaxTree !!!");
							
							
						} else {
							while(declaredIn.declaredIn.getRTBlockLevel() == bl) declaredIn = declaredIn.declaredIn;
							codeBuilder.checkcast(declaredIn.getClassDesc());
						}
					}
					codeBuilder
						.getfield(inspectedVariable.getFieldRefEntry(pool))
						.checkcast( ((DeclarationScope)var.declaredIn).getClassDesc())
						.getfield(var.getFieldRefEntry(pool));
				} else {
					buildIdentifierAccess(simCoder, destination, codeBuilder);
					codeBuilder.getfield(var.getFieldRefEntry(pool));
				}
				break;

			case ObjectKind.InspectVariableDeclaration:
				InspectVariableDeclaration ivar=(InspectVariableDeclaration)declaredAs;
				if(inspectedVariable != null) {
//					ConnectionBlock cblk=(ConnectionBlock)meaning.declaredIn;
//					boolean withFollowSL = meaning.declaredIn.buildCTX(codeBuilder);
//					if(withFollowSL) {
//						codeBuilder.checkcast(cblk.declaredIn.getClassDesc());
//					}
//					
//					codeBuilder
//						.getfield(inspectedVariable.getFieldRefEntry(pool))
//						.checkcast( ((DeclarationScope)ivar.declaredIn).getClassDesc())
//						.getfield(ivar.getFieldRefEntry(pool));
					Util.IERR("DON'T BELIEVE THIS WILL EVER HAPPEN !");
				} else {
					buildIdentifierAccess(simCoder, destination, codeBuilder);
					codeBuilder.getfield(ivar.getFieldRefEntry(pool));
				}
				break;

			case ObjectKind.VirtualSpecification:
				VirtualSpecification virtual = (VirtualSpecification) declaredAs;
				BuildCPV.virtual(simCoder, this, virtual, remotelyAccessed,codeBuilder);
				break;

			default:
				Util.IERR(""+ObjectKind.edit(declaredAs.declarationKind));
		}
	}


	// ***************************************************************************************
	// *** JVM CODING: getFieldRefEntry
	// ***************************************************************************************
	/// ClassFile Coding Utility: Return the field ref entry for this variable.
	/// @param pool the ConstantPoolBuilder to use.
	/// @return the field ref entry for this variable.
	public FieldRefEntry getFieldRefEntry(ConstantPoolBuilder pool) {
		ClassDesc owner=meaning.declaredIn.getClassDesc();
		Declaration declaredAs = meaning.declaredAs;
		return(pool.fieldRefEntry(owner, declaredAs.getJavaIdentifier(), declaredAs.type.toClassDesc()));
	}
	
	
	// ******************************************************************
	// *** Coding: buildEvaluateParameter
	// ******************************************************************
	/// ClassFile Coding Utility: Build this Parameter.
	/// @param par the Parameter
	/// @param inspectedVariable then inspected variable or null
	/// @param rightPart When destination, this is the right part of the assignment
	/// @param codeBuilder the CodeBuilder
	private void buildEvaluateParameter(final SimulaCoder simCoder, final Parameter par,
			final Expression inspectedVariable, final Expression rightPart, final CodeBuilder codeBuilder) {
		ConstantPoolBuilder pool=codeBuilder.constantPool();
		boolean destination = (rightPart != null);
		switch (par.kind) {
		case Parameter.Kind.Array: // Parameter Array
			buildIdentifierAccess(simCoder, destination, codeBuilder);
			if (par.mode == Parameter.Mode.name) {
				codeBuilder.getfield(par.getFieldRefEntry(pool));
				RTS.invokevirtual_NAME_get(codeBuilder);
				codeBuilder.checkcast(RTS.CD.RTS_ARRAY(type));
				if(checkedParams != null)
					ArrayDeclaration.arrayGetElement2(simCoder, type,par.getFieldIdentifier(),checkedParams,codeBuilder);
			} else {
				if (this.hasArguments()) {
					if (destination)
						 ArrayDeclaration.arrayPutElement(simCoder, meaning,par.getFieldIdentifier(),true,this.checkedParams,rightPart,codeBuilder);
					else ArrayDeclaration.arrayGetElement(simCoder, type,par.getFieldIdentifier(),true,this.checkedParams,null,par.declaredIn,codeBuilder);
				} else {
					if (destination) Util.IERR();
					ClassDesc owner = (inspectedVariable == null)
							? par.declaredIn.getClassDesc()
									: inspectedVariable.type.getQual().getClassDesc();
					codeBuilder.getfield(owner, par.getFieldIdentifier(), RTS.CD.RTS_ARRAY);
				}
			}
			break;

		case Parameter.Kind.Procedure: // Parameter Procedure
			if (destination)               Util.IERR();
			if (inspectedVariable != null) Util.IERR();
			if (par.mode == Parameter.Mode.value)
				Util.codingError(this, "Parameter " + this + " by Value is not allowed - Rewrite Program");
			else { // Procedure By Reference or Name.
				BuildCPF.formal(simCoder, this, par, codeBuilder);
				if(par.type == null) codeBuilder.pop();
			}
			break;

		case Parameter.Kind.Simple, Parameter.Kind.Label:
			buildIdentifierAccess(simCoder, destination,codeBuilder); // Kind: Simple/Label
			codeBuilder.getfield(par.getFieldRefEntry(pool));
			if (!destination && par.mode == Parameter.Mode.name) {
				RTS.invokevirtual_NAME_get(codeBuilder);
				par.type.checkCast(codeBuilder);
				par.type.valueToPrimitiveType(codeBuilder);

			}
			break;
		}
	}

	@Override
	public void printTree(final int indent) {
		IO.println(edTreeIndent(indent)+this);
	}
	
	public static String edParams(Vector<Expression> par) {
		if(par == null) return "par==null";
		StringBuilder sb = new StringBuilder();
//		sb.append("TEST: "+par+" ");
		String sep = "(";
		for(Expression p:par) {
			sb.append(sep).append(p); sep = ", ";
		}
		sb.append(")");
		return sb.toString();
	}

	@Override
	public String toString() {
		String ID = (identifier == null)? "UNKNOWN_Variable" : identifier.value;
		StringBuilder sb = new StringBuilder(ID);
		Vector<Expression> par = (checkedParams != null)? checkedParams : params;
//		sb.append("TEST: "+par+" ");
		if (par == null) {
			if(type != null) sb.append("  type=").append(type);
		} else {
//			return (("" + identifier + params).replace('[', '(').replace(']', ')') );
//			sb.append(params);
			sb.append(edParams(par));
		}
		return sb.toString();
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O.
	public VariableExpression(final DocumentManager documentManager) {
		super(documentManager);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("BEGIN Write VariableExpression: "+this);
		oupt.writeKind(ObjectKind.VariableExpression);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** Expression
		oupt.writeType(type);
		oupt.writeObj(backLink);
		// *** VariableExpression
		oupt.writeIdentifier(identifier);
		oupt.writeBoolean(remotelyAccessed);
		
		if(params == null) {
			oupt.writeShort(0);			
		} else {
			oupt.writeShort(params.size());
			for(Expression par:params) oupt.writeObj(par);
		}
	}
	
	/// Read and return a VariableExpression object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the VariableExpression object read from the stream.
	/// @throws IOException if something went wrong.
	public static VariableExpression readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		VariableExpression var = new VariableExpression(documentManager);
		var.OBJECT_SEQU = inpt.readSEQU(var);
		// *** SyntaxElement
		var.astData = readAstData(inpt);
		// *** Expression
		var.type = inpt.readType();
		var.backLink = (SyntaxElement) inpt.readObj(documentManager);
		// *** VariableExpression
		var.identifier = inpt.readIdentifier();
		var.remotelyAccessed = inpt.readBoolean();
		
		int n = inpt.readShort();
		if(n > 0) {
			var.params = new Vector<Expression>();
			for(int i=0;i<n;i++)
				var.params.add((Expression) inpt.readObj(documentManager));
		}
		
		Util.TRACE_INPUT("readVariableExpression: " + var);
		return(var);
	}

}
