/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.core.syntaxClass.statement;

import java.io.IOException;
import java.util.Vector;

import simula.Option;
import simula.core.CoreGlobal;
import simula.core.DocumentManager;
import simula.core.CoreGlobal2;
import simula.core.builder.Parse;
import simula.core.builder.SimulaBuilder;
import simula.core.builder.token.Identifier;
import simula.core.builder.token.LexToken;
import simula.core.syntaxClass.SyntaxElement;
import simula.core.syntaxClass.Type;
import simula.core.syntaxClass.declaration.BlockDeclaration;
import simula.core.syntaxClass.declaration.ClassDeclaration;
import simula.core.syntaxClass.declaration.ConnectionBlock;
import simula.core.syntaxClass.declaration.Declaration;
import simula.core.syntaxClass.declaration.DeclarationScope;
import simula.core.syntaxClass.declaration.ExternalDeclaration;
import simula.core.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.core.syntaxClass.declaration.ProcedureDeclaration;
import simula.core.syntaxClass.declaration.StandardClass;
import simula.core.syntaxClass.declaration.StandardProcedure;
import simula.core.syntaxClass.expression.VariableExpression;
import simula.core.utilities.KeyWord;
import simula.core.utilities.ObjectKind;
import simula.core.utilities.Util;
import simula.lsp.util.SimPosition;

/// Simula Program Module.
/// 
/// Link to GitHub: <a href=
/// "https://github.com/portablesimula/WorkSpaces/blob/main/Eclipse/SimulaProjects/Simula/src/simula/compiler/syntaxClass/statement/ProgramModule.java">
/// <b>Source File</b></a>.
/// 
/// <pre>
/// 
/// Simula Standard: Chapter 6 Program Modules
/// 
///	     SIMULA-source-module
///         = [ external-head ] ( program | procedure-declaration | class-declaration )
///
///         external-head = external-declaration ; { external-declaration ; }
///         
///            external-declaration = external-procedure-declaration | external-class-declaration
///            
/// 		program = statement
/// 
/// 		procedure-declaration = [ type ] PROCEDURE procedure-identifier procedure-head procedure-body
/// </pre>
/// 
/// @author SIMULA Standards Group
/// @author Øystein Myhre Andersen
public final class ProgramModule extends Statement {
	
	/// The Variable SYSIN.
	final private VariableExpression sysin;
	
	/// The Variable SYSOUT.
	final private VariableExpression sysout;
	
	/// The mainModule declaration.
	public DeclarationScope mainModule;

	/// The external head
	public Vector<SyntaxElement> externalHead;

	/// Returns the mainModule identifier.
	/// @return the mainModule identifier
	public Identifier getIdentifier() { return(mainModule.identifier); }

	/// Returns the relative file name.
	/// @return the relative file name
	public String getRelativeAttributeFileName() {
		if(mainModule.declarationKind==ObjectKind.Class) return(CoreGlobal2.packetName+"/CLASS.AF");
		if(mainModule.declarationKind==ObjectKind.Procedure) return(CoreGlobal2.packetName+"/PROCEDURE.AF");
		else return(null);
	}
	  
	/// Returns true if this program mainModule is executable.
	/// @return true if this program mainModule is executable
	public boolean isExecutable() {
		if(mainModule.declarationKind==ObjectKind.SimulaProgram) return(true);
		if(mainModule.declarationKind==ObjectKind.PrefixedBlock) return(true);
		else return(false);
	}

	/// Create a new ProgramModule.
	public ProgramModule(SimulaBuilder simBuilder) {
		super(simBuilder);
		Identifier sysinID = new Identifier("sysin");
		sysin = new  VariableExpression(null, sysinID);
		sysin.meaning = StandardClass.BASICIO.findMeaning(sysinID);
		sysin.SET_SEMANTICS_CHECKED();
		Identifier sysoutID = new Identifier("sysout");
		sysout = new VariableExpression(null, sysoutID);
		sysout.meaning = StandardClass.BASICIO.findMeaning(sysoutID);
		sysout.SET_SEMANTICS_CHECKED();
//		IO.println("NEW ProgramModule: sysout.meaning="+sysout.meaning);
	}
	
	public void doBuild() {
//		try	{
			if(Option.internal.TRACE_PARSE) Parse.TRACE("Parse Program");
			CoreGlobal.setScope(StandardClass.BASICIO);		  // BASICIO Begin
			new ConnectionBlock(null, sysin, null)            //    Inspect sysin do
			     .setClassDeclaration(StandardClass.Infile);
			new ConnectionBlock(null, sysout, null)           //    Inspect sysout do
			     .setClassDeclaration(StandardClass.Printfile);
			CoreGlobal.getCurrentScope().sourceBlockLevel=0;
			
			while(Parse.accept(simBuilder, KeyWord.EXTERNAL)) {
//				externalHead = ExternalDeclaration.expectExternalDeclaration(simBuilder, StandardClass.BASICIO);		
				Vector<SyntaxElement> external = ExternalDeclaration.expectExternalDeclaration(simBuilder);	
				if(externalHead == null) externalHead = new Vector<SyntaxElement>();
				externalHead.addAll(external);
				Parse.expect(simBuilder, KeyWord.SEMICOLON);
			}
			
			// Now: Looking for ( program | procedure-declaration | class-declaration )
			
			
			Identifier mayBeClassIdent = Parse.acceptIdentifier(simBuilder);
			if(mayBeClassIdent!=null) {
				if(Parse.accept(simBuilder, KeyWord.CLASS)) {
					mainModule=ClassDeclaration.expectClassDeclaration(simBuilder, mayBeClassIdent);
				}
			    else {
//			    	IO.println("ProgramModule.doBuild: IDENTIFIER ...");
			    	Parse.saveCurrentToken(simBuilder);
			    	mainModule = doParseProgram(simBuilder);
			    }
			}
			else if(Parse.accept(simBuilder, KeyWord.CLASS)) mainModule=ClassDeclaration.expectClassDeclaration(simBuilder, null);
			else {
				Type type=Parse.acceptType(simBuilder);
			    if(Parse.accept(simBuilder, KeyWord.PROCEDURE)) mainModule=ProcedureDeclaration.expectProcedureDeclaration(simBuilder, type);
			    else mainModule = doParseProgram(simBuilder);
			}
			
			StandardClass.BASICIO.declarationList.add(mainModule);
			
			LexToken token = Parse.getCurrentParserToken(simBuilder);
			if(token != null && token.keyWord != KeyWord.EOF) {
				int mrk = simBuilder.tokenList.size();
				while(!simBuilder.eof()) simBuilder.getNextParserToken(); // consume tokens  (add it to tokenList)
				int n = simBuilder.tokenList.size();
				StringBuilder sb = new StringBuilder();
				for(int i=mrk-1;i<n;i++) {
//					IO.println("NEW ProgramModule: tokenAfter: " + simBuilder.tokenList.get(i));
					sb.append(simBuilder.tokenList.get(i).getText());
				}
				String textAfterEnd = Util.printable(sb.toString());
//				IO.println("NEW ProgramModule: TextAfterEnd: " + Util.printable(textAfterEnd));
				SimPosition start = simBuilder.tokenList.get(mrk-1).getPosition();
				SimPosition end = simBuilder.tokenList.get(n-1).getPosition();
				if(! textAfterEnd.equals(";")) Util.warning(simBuilder, start, end, "Text after Program end: \"" + textAfterEnd + '"');
			}
			
			if(CoreGlobal2.verbose) Util.TRACE("ProgramModule: END NEW SimulaProgram: "+toString());
//		} catch(Throwable e) {
//			e.printStackTrace();
//			Util.IERR();
//		}
	}
	
	/// Parse Simula Program by expecting a Statement.
	/// @return the Program Statement.
	private DeclarationScope doParseProgram(final SimulaBuilder simBuilder) {
//		BlockDeclaration mainBlock = new MaybeBlockDeclaration(simBuilder, "MainBlock: " + Global.sourceName);
		String sourceName = DocumentManager.sourceName;
		BlockDeclaration mainBlock = new MaybeBlockDeclaration(simBuilder, new Identifier(sourceName));
		
		mainBlock.isMainModule = true;
		mainBlock.declarationKind = ObjectKind.SimulaProgram;
		Statement program = Statement.acceptStatement(simBuilder);
		mainBlock.statements.add(program);
//		simBuilder.getRoot().printPsiTree("DONE ProgramModule.doParseProgram");
		return mainBlock;
	}

	@Override
	public void doChecking() {
		if(IS_SEMANTICS_CHECKED()) return;
		CoreGlobal.enterScope(mainModule);
		sysin.doChecking();
//		IO.println("ProgramModule.doChecking: BEFORE SYSOUT: sysout.meaning="+sysout.meaning);
		sysout.doChecking();
//		IO.println("ProgramModule.doChecking: AFTER SYSOUT: sysout.meaning="+sysout.meaning);
//		Util.IERR("");
		mainModule.doChecking();
		SET_SEMANTICS_CHECKED();
	}
  
	@Override
	public void doJavaCoding() { mainModule.doJavaCoding(); }

	/// Create Java ClassFile.
	/// @throws IOException if something went wrong
	public void createJavaClassFile() throws IOException {
		CoreGlobal.sourceLineNumber = firstLineNumber();
		mainModule.createJavaClassFile();
	}

	@Override
	public void print(final int indent) { mainModule.print(0); }

	@Override
	public void printTree(final int indent) {
		IO.println("BASICIO");
		IO.println("    ... Standard Classes and Procedures");
		for(Declaration decl:StandardClass.BASICIO.declarationList) {
			if(decl instanceof StandardProcedure) ; // Nothing
			else if(decl instanceof StandardClass) ; // Nothing
			else decl.printTree(1);
		}
		IO.println("=================================================================");
	}
	
	@Override
	public String toString() {
		return (mainModule==null)?"MAIN":mainModule.identifierValue();
	}
	
}
