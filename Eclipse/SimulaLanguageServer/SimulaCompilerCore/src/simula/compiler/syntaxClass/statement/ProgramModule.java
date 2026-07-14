/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.io.IOException;
import java.util.Vector;

import simula.builder.SimulaBuilder;
import simula.Option;
import simula.builder.Parse;
import simula.compiler.syntaxClass.SyntaxElement;
import simula.compiler.syntaxClass.Type;
import simula.compiler.syntaxClass.declaration.BlockDeclaration;
import simula.compiler.syntaxClass.declaration.ClassDeclaration;
import simula.compiler.syntaxClass.declaration.ConnectionBlock;
import simula.compiler.syntaxClass.declaration.Declaration;
import simula.compiler.syntaxClass.declaration.DeclarationScope;
import simula.compiler.syntaxClass.declaration.ExternalDeclaration;
import simula.compiler.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.compiler.syntaxClass.declaration.ProcedureDeclaration;
import simula.compiler.syntaxClass.declaration.StandardClass;
import simula.compiler.syntaxClass.declaration.StandardProcedure;
import simula.compiler.syntaxClass.expression.VariableExpression;
import simula.compiler.utilities.CoreGlobal;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Util;
import simula.token.Identifier;
import simula.token.LexToken;

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
		if(mainModule.declarationKind==ObjectKind.Class) return(CoreGlobal.packetName+"/CLASS.AF");
		if(mainModule.declarationKind==ObjectKind.Procedure) return(CoreGlobal.packetName+"/PROCEDURE.AF");
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
		sysin = new  VariableExpression(null, new Identifier("sysin"));  sysin.SET_SEMANTICS_CHECKED();
		sysout = new VariableExpression(null, new Identifier("sysout")); sysout.SET_SEMANTICS_CHECKED();
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
			
			simBuilder.startTokenRange("ProgramModule.MayBeEXTERNAL", simBuilder.getCurrentParserToken());
			
			while(Parse.accept(simBuilder, KeyWord.EXTERNAL)) {
//				externalHead = ExternalDeclaration.expectExternalDeclaration(simBuilder, StandardClass.BASICIO);		
				Vector<SyntaxElement> external = ExternalDeclaration.expectExternalDeclaration(simBuilder);	
				if(externalHead == null) externalHead = new Vector<SyntaxElement>();
				externalHead.addAll(external);
				Parse.expect(simBuilder, KeyWord.SEMICOLON);
				simBuilder.doneTokenRange(external);
				simBuilder.startTokenRange("ProgramModule.EXTERNAL+", simBuilder.getCurrentParserToken());
			}
			simBuilder.dropTokenRange();
			
			// Now: Looking for ( program | procedure-declaration | class-declaration )
			
			
			simBuilder.startTokenRange("ProgramModule.MaybeClass", simBuilder.getCurrentParserToken());
			Identifier mayBeClassIdent = Parse.acceptIdentifier(simBuilder);
			if(mayBeClassIdent!=null) {
				if(Parse.accept(simBuilder, KeyWord.CLASS)) {
					mainModule=ClassDeclaration.expectClassDeclaration(simBuilder, mayBeClassIdent);
				}
			    else {
					simBuilder.dropTokenRange();
					simBuilder.startTokenRange("ProgramModule.PROGRAM", simBuilder.getCurrentParserToken());
			    	mainModule = doParseProgram(simBuilder);
			    }
			}
			else if(Parse.accept(simBuilder, KeyWord.CLASS)) mainModule=ClassDeclaration.expectClassDeclaration(simBuilder, null);
			else {
				Type type=Parse.acceptType(simBuilder);
			    if(Parse.accept(simBuilder, KeyWord.PROCEDURE)) mainModule=ProcedureDeclaration.expectProcedureDeclaration(simBuilder, type);
			    else mainModule = doParseProgram(simBuilder);
			}
			simBuilder.doneTokenRange(mainModule);
			
			
			StandardClass.BASICIO.declarationList.add(mainModule);
			
			LexToken token = Parse.getCurrentParserToken(simBuilder);
			if(token != null && token.keyWord != KeyWord.EOF) {
//				simBuilder.startTokenRange("ProgramModule.TAIL",token);
//				Comment dum = new Comment(simBuilder);
				while(!simBuilder.eof()) simBuilder.getNextParserToken(); // consume tokens  (add it to 'current tree')
//				simBuilder.doneTokenRange(dum);
				
				IO.println("NEW ProgramModule - NOTE: FINN EN ANNEN MÅTE Å GJØRE DETTE PÅ");
				
//				IO.println("NEW ProgramModule: TextAfterEnd: \"" + dum.psiTree.getText().replace("\n", "\\n") + '"');
//				String textAfterEnd = dum.getPsiTree().getText().replaceAll("\\s+", ""); // Remove WhiteSpaces			
//				IO.println("NEW ProgramModule: TextAfterEnd: \"" + textAfterEnd + '"');
//				simBuilder.psiRoot.printTree("");
//				if(! textAfterEnd.equals(";")) Util.warning("Text after Program end: \"" + textAfterEnd + '"');
//				Util.IERR();
			}
			
			if(Option.verbose) Util.TRACE("ProgramModule: END NEW SimulaProgram: "+toString());
//		} catch(Throwable e) {
//			e.printStackTrace();
//			Util.IERR();
//		}
	}
	
	/// Parse Simula Program by expecting a Statement.
	/// @return the Program Statement.
	private DeclarationScope doParseProgram(final SimulaBuilder simBuilder) {
//		BlockDeclaration mainBlock = new MaybeBlockDeclaration(simBuilder, "MainBlock: " + Global.sourceName);
		String sourceName = simBuilder.documentManager.sourceName;
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
		sysout.doChecking();
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
	public void printTree(final int indent, final Object head) {
		IO.println("BASICIO");
		IO.println("    ... Standard Classes and Procedures");
		for(Declaration decl:StandardClass.BASICIO.declarationList) {
			if(decl instanceof StandardProcedure) ; // Nothing
			else if(decl instanceof StandardClass) ; // Nothing
			else decl.printTree(1,this);
		}
		IO.println("=================================================================");
	}
	
	@Override
	public String toString() {
		return (mainModule==null)?"MAIN":mainModule.identifier.value;
	}
	
}
