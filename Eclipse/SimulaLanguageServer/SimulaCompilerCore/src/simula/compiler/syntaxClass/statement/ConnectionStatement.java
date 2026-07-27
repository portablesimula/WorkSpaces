/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.classfile.constantpool.FieldRefEntry;
import java.lang.constant.ClassDesc;

import simula.builder.SimulaBuilder;
import simula.Option;
import simula.builder.Parse;
import simula.compiler.AttributeInputStream;
import simula.compiler.AttributeOutputStream;
import simula.compiler.JavaSourceFileCoder;
import simula.compiler.syntaxClass.Type;
import simula.compiler.syntaxClass.declaration.BlockDeclaration;
import simula.compiler.syntaxClass.declaration.ConnectionBlock;
import simula.compiler.syntaxClass.declaration.DeclarationScope;
import simula.compiler.syntaxClass.declaration.InspectVariableDeclaration;
import simula.compiler.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.compiler.syntaxClass.expression.AssignmentOperation;
import simula.compiler.syntaxClass.expression.Expression;
import simula.compiler.syntaxClass.expression.VariableExpression;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.ObjectList;
import simula.compiler.utilities.Util;
import simula.token.Identifier;

/// Connection Statement.
/// 
/// <pre>
/// 
/// Simula Standard: 4.8 Connection statement
/// 
/// 	connection-statement
/// 			= INSPECT object-expression when-clause { when-clause } [ otherwise-clause ]
/// 			| INSPECT object-expression DO statement [ otherwise-clause ]
/// 
/// 			when-clause = WHEN class-identifier DO statement
/// 
/// 			otherwise-clause = OTHERWISE statement
/// 
/// 
/// The connection statement is implemented using Java's <b>instanceof</b> operator and the
/// if statement. For example, the connection statement:
/// 
///         <b>inspect</b> x <b>do</b> image:-t;
///         
/// Where 'x' is declared as a reference to an ImageFile, is compiled to:
/// 
///         if(x!=null) x.image=t;
///         
/// Other examples that also use '<b>ref</b>(Imagefile) x' may be:
/// 
///      1) <b>inspect</b> x <b>do</b> image:-t <b>otherwise</b> t:-<b>notext</b>;
///      
///      2) <b>inspect</b> x
///            <b>when</b> infile <b>do</b> t:-intext(12)
///            <b>when</b> outfile <b>do</b> outtext(t);
///            
///      3) <b>inspect</b> x
///            <b>when</b> infile <b>do</b> t:-intext(12)
///            <b>when</b> outfile <b>do</b> outtext(t)
///         <b>otherwise</b> t:-<b>notext</b>;
/// 
/// These examples are compiled to:
/// 
///      1) <b>if</b>(x!=<b>null</b>) x.image=t; <b>else</b> t=null;
///      
///      2) <b>if</b>(x <b>instanceof</b> RTS_Infile) t=((RTS_Infile)x).intext(12);
///         <b>else</b> <b>if</b>(x <b>instanceof</b> RTS_Outfile) ((RTS_Outfile)x).outtext(t);
///          
///      3) <b>if</b>(x <b>instanceof</b> RTS_Infile) t=((RTS_Infile)x).intext(12);
///         <b>else</b> <b>if</b>(x <b>instanceof</b> RTS_Outfile) ((RTS_Outfile)x).outtext(t);
///         <b>else</b> t=null;
/// 
/// </pre>
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/ConnectionStatement.java">
/// <b>Source File</b></a>.
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class ConnectionStatement extends Statement {
	
	/// The inspected object.
	Expression objectExpression;
	
	/// Utility Variable to hold the evaluated object-expression.
	VariableExpression inspectedVariable;
	
	/// The inspected variable's declaration.
	public InspectVariableDeclaration inspectVariableDeclaration;
	
	/// The connection parts. A ConnectionDoPart or a list of WhenParts.
	private ObjectList<ConnectionDoPart> connectionPart = new ObjectList<ConnectionDoPart>();
	
	/// The otherwise statement.
	private Statement otherwise;
	
	/// True if this connection statement contains ConnectionWhenPart(s).
	private boolean hasWhenPart;
	
	/// Utility to help generate unique identifiers to the inspected variable.
	private static int SEQUX = 1;
	
	/// Utility to be used in when-parts
	/// @return a unique identifier
	public static String getUniqueConnID() {
		return "_connID_" + (SEQUX++);
	}

	/// The end Label.
	Label endLabel;

	/// Create a new ConnectionStatement.
	/// 
	/// Pre-Condition: INSPECT  is already read.
	/// @param line the source line number
	ConnectionStatement(final SimulaBuilder simBuilder) {
		super(simBuilder);
		simBuilder.consume(KeyWord.INSPECT); //  (add it to 'current tree')

		if (Option.internal.TRACE_PARSE)
			Parse.TRACE("Parse ConnectionStatement");
		objectExpression = Expression.expectExpression(simBuilder, "connected object");
		objectExpression.backLink = this;
		Identifier ident = new Identifier("_inspect_" + firstLineNumber() + '_' + (SEQUX++));
		inspectedVariable = new VariableExpression(simBuilder, ident);
		DeclarationScope scope = CoreGlobal.getCurrentScope();
		inspectVariableDeclaration = new InspectVariableDeclaration(simBuilder, Type.Ref("RTObject"), ident, scope, this);
		
		LOOP: while (scope instanceof ConnectionBlock
				|| (scope instanceof MaybeBlockDeclaration && scope.declarationList.size() == 0 )) {
			if(scope instanceof BlockDeclaration blk && blk.isMainModule) break LOOP;
			DeclarationScope declaredIn = scope.declaredIn;
			scope = declaredIn;
		}
			
//		IO.println("NEW ConnectionStatement: add inspectVariableDeclaration to "+scope);
		scope.declarationList.add(inspectVariableDeclaration);
		inspectVariableDeclaration.declaredIn = scope;

		boolean hasDoPart=false;
		boolean hasWhenPart=false;
		if (Parse.accept(simBuilder, KeyWord.DO)) {
			hasDoPart = true;
			ConnectionBlock connectionBlock = new ConnectionBlock(simBuilder, inspectedVariable, null);
			DeclarationScope prevScope = CoreGlobal.getCurrentScope();
			CoreGlobal.setScope(connectionBlock);
			Statement statement = Statement.acceptStatement(simBuilder);
			CoreGlobal.setScope(prevScope);
			
			connectionPart.add(new ConnectionDoPart(simBuilder, this,connectionBlock, statement));
			connectionBlock.end();
		} else {
			while (Parse.accept(simBuilder, KeyWord.WHEN)) {
				Identifier classIdentifier = Parse.expectIdentifier(simBuilder);
				Parse.expect(simBuilder, KeyWord.DO);
				ConnectionBlock connectionBlock = new ConnectionBlock(simBuilder, inspectedVariable, classIdentifier);
				hasWhenPart = true;
				Statement statement = Statement.acceptStatement(simBuilder);
				ConnectionWhenPart whenPart = new ConnectionWhenPart(simBuilder, this,classIdentifier, connectionBlock, statement);
				connectionPart.add(whenPart);
				connectionBlock.end();
			}

		}
		if(!(hasDoPart | hasWhenPart)) Util.syntaxError(simBuilder, "Incomplete Inspect statement: "+objectExpression + ", missing DO or WHEN");
		Statement otherwise = null;
		if (Parse.accept(simBuilder, KeyWord.OTHERWISE)) otherwise = Statement.acceptStatement(simBuilder);
		this.otherwise=otherwise;
		this.hasWhenPart=hasWhenPart;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Line "+this.firstLineNumber()+": ConnectionStatement: "+this);
	}

	@Override
	public void doChecking() {
		if (IS_SEMANTICS_CHECKED())	return;
		CoreGlobal.sourceLineNumber = firstLineNumber();
		if (Option.internal.TRACE_CHECKER)
			Util.TRACE("BEGIN ConnectionStatement(" + toString() + ").doChecking - Current Scope Chain: " + CoreGlobal.getCurrentScope().edScopeChain());		
		objectExpression.doChecking();
		Type exprType = objectExpression.type;
		exprType.doChecking(CoreGlobal.getCurrentScope(), this);
		inspectVariableDeclaration.type = exprType;
		inspectedVariable.type = exprType;
		inspectedVariable.doChecking();

		for(ConnectionDoPart part:connectionPart) part.doChecking();
		if (otherwise != null) otherwise.doChecking();
		
		inspectedVariable.identifier = new Identifier(inspectVariableDeclaration.getFieldIdentifier());
		inspectVariableDeclaration.identifier = inspectedVariable.identifier;
		SET_SEMANTICS_CHECKED();
	}

	@Override
	public void doJavaCoding() {
		CoreGlobal.sourceLineNumber = firstLineNumber();
//		IO.println("ConnectionStatement.doJavaCoding: "+Global.sourceLineNumber);
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code("{");
		JavaSourceFileCoder.debug("// BEGIN INSPECTION ");
		Expression assignment = new AssignmentOperation(null, inspectedVariable, KeyWord.ASSIGNREF, objectExpression);
//		Expression assignment = new AssignmentOperation(this.psiTree, inspectedVariable, KeyWord.ASSIGNREF, objectExpression);
		assignment.doChecking();
		JavaSourceFileCoder.code(assignment.toJavaCode() + ';');
		if (!hasWhenPart) JavaSourceFileCoder.code("if(" + inspectedVariable.toJavaCode() + "!=null) {","INSPECT " + inspectedVariable);
		boolean first = true;
		for(ConnectionDoPart part:connectionPart) { part.doCoding(first);	first = false; }
		if (!hasWhenPart) JavaSourceFileCoder.code("}");
		if (otherwise != null) {
			JavaSourceFileCoder.code("else {","OTHERWISE ");
			otherwise.doJavaCoding();
			JavaSourceFileCoder.code("}","END OTHERWISE ");
		}
		// JavaModule.debug("// END INSPECTION ");
		JavaSourceFileCoder.code("}","END INSPECTION");
	}

	@Override
	public void buildByteCode(CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		ConstantPoolBuilder pool=codeBuilder.constantPool();
		Label otwLabel = null;
		endLabel = codeBuilder.newLabel();
		codeBuilder.aload(0);
		objectExpression.buildEvaluation(null,codeBuilder);
		ClassDesc CD_type=inspectedVariable.type.toClassDesc();
		FieldRefEntry FRE=pool.fieldRefEntry(BlockDeclaration.currentClassDesc(),inspectedVariable.identifier.value, CD_type);
		codeBuilder.putfield(FRE);
		
		if (!hasWhenPart) {
			codeBuilder.aload(0);
			codeBuilder.getfield(FRE);
			if(otherwise != null) {
				otwLabel = codeBuilder.newLabel();
				codeBuilder.ifnull(otwLabel);
			} else codeBuilder.ifnull(endLabel);
		}
		
		for(ConnectionDoPart part:connectionPart) 
			part.buildByteCode(codeBuilder);
		
		if (otherwise != null) {
			if(otwLabel != null) {
				codeBuilder.labelBinding(otwLabel);	
			}
			otherwise.buildByteCode(codeBuilder);
		}
	
		codeBuilder.labelBinding(endLabel);
	}

	
	@Override
	public void printTree(final int indent) {
		IO.println(edTreeIndent(indent)+"INSPECT " + inspectedVariable + " = " + objectExpression);
		for (ConnectionDoPart doPart : connectionPart) doPart.printTree(indent + 1);
		if(otherwise != null) {
			IO.println(edTreeIndent(indent)+"OTHERWISE");
			otherwise.printTree(indent + 1);
		}
	}

	// ***********************************************************************************************
	// *** Printing Utility: print
	// ***********************************************************************************************
	@Override
	public void print(final int indent) {
    	String spc=edIndent(indent);
		// if(assignment!=null) assignment.print(indent);
		Util.println(spc + "INSPECT " + inspectedVariable + " = " + objectExpression);
		for (ConnectionDoPart doPart : connectionPart) doPart.printTree(indent);
		if (otherwise != null) Util.println(spc + "   OTHERWISE " + otherwise + ';');
	}

	@Override
	public String toString() {
		String otherwisePart = (otherwise == null)?"":" OTHERWISE " + otherwise;
		return "INSPECT " + inspectedVariable + " " + connectionPart + otherwisePart;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************
	/// Default constructor used by Attribute File I/O
	private ConnectionStatement() {
		super(null);
	}

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeConnectionStatement: " + this);
		oupt.writeKind(ObjectKind.ConnectionStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		writeAstData(oupt);
		// *** ConnectionStatement
		oupt.writeObj(objectExpression);
		oupt.writeObj(inspectedVariable);
		oupt.writeObj(inspectVariableDeclaration);
		oupt.writeObjectList(connectionPart);
		oupt.writeObj(otherwise);
		oupt.writeBoolean(hasWhenPart);
	}

	/// Read and return a ConnectionStatement object.
	/// @param inpt the AttributeInputStream to read from
	/// @return the ConnectionStatement object read from the stream.
	/// @throws IOException if something went wrong.
	@SuppressWarnings("unchecked")
	public static ConnectionStatement readObject(AttributeInputStream inpt) throws IOException {
		ConnectionStatement stm = new ConnectionStatement();
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		stm.astData = readAstData(inpt);
		// *** ConnectionStatement
		stm.objectExpression = (Expression) inpt.readObj();
		stm.inspectedVariable = (VariableExpression) inpt.readObj();
		stm.inspectVariableDeclaration = (InspectVariableDeclaration) inpt.readObj();
		stm.connectionPart = (ObjectList<ConnectionDoPart>) inpt.readObjectList();
		stm.otherwise = (Statement) inpt.readObj();
		stm.hasWhenPart = inpt.readBoolean();
		Util.TRACE_INPUT("ConnectionStatement: " + stm);
		return(stm);
	}

}
