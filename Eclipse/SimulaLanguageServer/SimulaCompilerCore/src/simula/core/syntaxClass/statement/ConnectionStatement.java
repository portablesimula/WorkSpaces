/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.Label;
import java.lang.classfile.constantpool.ConstantPoolBuilder;
import java.lang.classfile.constantpool.FieldRefEntry;
import java.lang.constant.ClassDesc;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.builder.AttributeInputStream;
import simula.core.builder.AttributeOutputStream;
import simula.core.builder.JavaSourceFileCoder;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.util.Identifier;
import simula.core.coder.SimulaCoder;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.BlockDeclaration;
import simula.core.syntaxClass.declaration.ConnectionBlock;
import simula.core.syntaxClass.declaration.DeclarationScope;
import simula.core.syntaxClass.declaration.InspectVariableDeclaration;
import simula.core.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.core.syntaxClass.expression.AssignmentOperation;
import simula.core.syntaxClass.expression.Expression;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.ObjectList;
import simula.core.utilities.Util;

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
	private ConnectionStatement(final DocumentManager documentManager) {
		super(documentManager);
	}
	/// 
	/// Pre-Condition: INSPECT  is already read.
	/// @param line the source line number
	static ConnectionStatement of(final DocumentManager documentManager) {
		ConnectionStatement stm = new ConnectionStatement(documentManager);
		SimulaBuilder simBuilder = documentManager.simBuilder;
		simBuilder.consume(KeyWord.INSPECT); //  (add it to tokenList)

		if (Option.internal.TRACE_PARSE)
			Parse.TRACE("Parse ConnectionStatement");
		stm.objectExpression = Expression.expectExpression(simBuilder, "connected object");
		stm.objectExpression.backLink = stm;
		Identifier ident = new Identifier("_inspect_" + stm.firstLineNumber() + '_' + (SEQUX++));
		stm.inspectedVariable = new VariableExpression(documentManager, ident);
		DeclarationScope scope = CoreGlobal.getCurrentScope();
		stm.inspectVariableDeclaration = new InspectVariableDeclaration(documentManager, Type.Ref("RTObject"), ident, scope, stm);
		
		LOOP: while (scope instanceof ConnectionBlock
				|| (scope instanceof MaybeBlockDeclaration && scope.declarationList.size() == 0 )) {
			if(scope instanceof BlockDeclaration blk && blk.isMainModule) break LOOP;
			DeclarationScope declaredIn = scope.declaredIn;
			scope = declaredIn;
		}
			
//		IO.println("NEW ConnectionStatement: add inspectVariableDeclaration to "+scope);
		scope.declarationList.add(stm.inspectVariableDeclaration);
		stm.inspectVariableDeclaration.declaredIn = scope;

		boolean hasDoPart=false;
		boolean hasWhenPart=false;
		if (Parse.accept(simBuilder, KeyWord.DO)) {
			hasDoPart = true;
			ConnectionBlock connectionBlock = new ConnectionBlock(documentManager, stm.inspectedVariable, null);
			DeclarationScope prevScope = CoreGlobal.getCurrentScope();
			CoreGlobal.setScope(connectionBlock);
			Statement statement = Statement.acceptStatement(simBuilder);
			CoreGlobal.setScope(prevScope);
			
			stm.connectionPart.add(new ConnectionDoPart(documentManager, stm,connectionBlock, statement));
			connectionBlock.end();
		} else {
			while (Parse.accept(simBuilder, KeyWord.WHEN)) {
				Identifier classIdentifier = Parse.expectIdentifier(simBuilder);
				Parse.expect(simBuilder, KeyWord.DO);
				ConnectionBlock connectionBlock = new ConnectionBlock(documentManager, stm.inspectedVariable, classIdentifier);
				hasWhenPart = true;
				Statement statement = Statement.acceptStatement(simBuilder);
				ConnectionWhenPart whenPart = new ConnectionWhenPart(documentManager, stm,classIdentifier, connectionBlock, statement);
				stm.connectionPart.add(whenPart);
				connectionBlock.end();
			}

		}
		if(!(hasDoPart | hasWhenPart)) Util.syntaxError(simBuilder, "Incomplete Inspect statement: "+stm.objectExpression + ", missing DO or WHEN");
		Statement otherwise = null;
		if (Parse.accept(simBuilder, KeyWord.OTHERWISE)) otherwise = Statement.acceptStatement(simBuilder);
		stm.otherwise=otherwise;
		stm.hasWhenPart=hasWhenPart;
		if (Option.internal.TRACE_PARSE)
			Util.TRACE("Line "+stm.firstLineNumber()+": ConnectionStatement: "+stm);
		return stm;
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
	public void doJavaCoding(final SimulaCoder simCoder) {
		CoreGlobal.sourceLineNumber = firstLineNumber();
//		IO.println("ConnectionStatement.doJavaCoding: "+Global.sourceLineNumber);
		ASSERT_SEMANTICS_CHECKED();
		JavaSourceFileCoder.code(simCoder,"{");
		JavaSourceFileCoder.debug(simCoder,"// BEGIN INSPECTION ");
		Expression assignment = new AssignmentOperation(documentManager, inspectedVariable, KeyWord.ASSIGNREF, objectExpression);
		assignment.doChecking();
		JavaSourceFileCoder.code(simCoder,assignment.toJavaCode() + ';');
		if (!hasWhenPart) JavaSourceFileCoder.code(simCoder,"if(" + inspectedVariable.toJavaCode() + "!=null) {","INSPECT " + inspectedVariable);
		boolean first = true;
		for(ConnectionDoPart part:connectionPart) { part.doCoding(simCoder, first);	first = false; }
		if (!hasWhenPart) JavaSourceFileCoder.code(simCoder,"}");
		if (otherwise != null) {
			JavaSourceFileCoder.code(simCoder,"else {","OTHERWISE ");
			otherwise.doJavaCoding(simCoder);
			JavaSourceFileCoder.code(simCoder,"}","END OTHERWISE ");
		}
		// JavaModule.debug("// END INSPECTION ");
		JavaSourceFileCoder.code(simCoder,"}","END INSPECTION");
	}

	@Override
	public void buildByteCode(SimulaCoder simCoder, CodeBuilder codeBuilder) {
		ASSERT_SEMANTICS_CHECKED();
		ConstantPoolBuilder pool=codeBuilder.constantPool();
		Label otwLabel = null;
		endLabel = codeBuilder.newLabel();
		codeBuilder.aload(0);
		objectExpression.buildEvaluation(simCoder, null, codeBuilder);
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
			part.buildByteCode(simCoder, codeBuilder);
		
		if (otherwise != null) {
			if(otwLabel != null) {
				codeBuilder.labelBinding(otwLabel);	
			}
			otherwise.buildByteCode(simCoder, codeBuilder);
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
		IO.println(spc + "INSPECT " + inspectedVariable + " = " + objectExpression);
		for (ConnectionDoPart doPart : connectionPart) doPart.printTree(indent);
		if (otherwise != null) IO.println(spc + "   OTHERWISE " + otherwise + ';');
	}

	@Override
	public String toString() {
		String otherwisePart = (otherwise == null)?"":" OTHERWISE " + otherwise;
		return "INSPECT " + inspectedVariable + " " + connectionPart + otherwisePart;
	}

	// ***********************************************************************************************
	// *** Attribute File I/O
	// ***********************************************************************************************

	@Override
	public void writeObject(AttributeOutputStream oupt) throws IOException {
		Util.TRACE_OUTPUT("writeConnectionStatement: " + this);
		oupt.writeKind(ObjectKind.ConnectionStatement);
		oupt.writeShort(OBJECT_SEQU);
		// *** SyntaxElement
		
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
	public static ConnectionStatement readObject(final DocumentManager documentManager, final AttributeInputStream inpt) throws IOException {
		ConnectionStatement stm = new ConnectionStatement(documentManager);
		stm.OBJECT_SEQU = inpt.readSEQU(stm);
		// *** SyntaxElement
		
		// *** ConnectionStatement
		stm.objectExpression = (Expression) inpt.readObj(documentManager);
		stm.inspectedVariable = (VariableExpression) inpt.readObj(documentManager);
		stm.inspectVariableDeclaration = (InspectVariableDeclaration) inpt.readObj(documentManager);
		stm.connectionPart = (ObjectList<ConnectionDoPart>) inpt.readObjectList(documentManager);
		stm.otherwise = (Statement) inpt.readObj(documentManager);
		stm.hasWhenPart = inpt.readBoolean();
		Util.TRACE_INPUT("ConnectionStatement: " + stm);
		return(stm);
	}

}
