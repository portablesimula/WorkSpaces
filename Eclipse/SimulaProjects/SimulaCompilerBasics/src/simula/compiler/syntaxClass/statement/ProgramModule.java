/// (CC) This work is licensed under a Creative Commons
/// Attribution 4.0 International License.
/// 
/// You find a copy of the License on the following
/// page: https://creativecommons.org/licenses/by/4.0/
package simula.compiler.syntaxClass.statement;

import java.io.IOException;
import java.util.Vector;

import simula.compiler.syntaxClass.Comment;
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
import simula.compiler.utilities.Global;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.ObjectKind;
import simula.compiler.utilities.Option;
import simula.compiler.utilities.Util;
import simula.psi.LexToken;
import simula.psi.AstBuilder;
import simula.psi.PsiParse;
import simula.psi.PsiTree;

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
	public String getIdentifier() { return(mainModule.identifier); }

	/// Returns the relative file name.
	/// @return the relative file name
	public String getRelativeAttributeFileName() {
		if(mainModule.declarationKind==ObjectKind.Class) return(Global.packetName+"/CLASS.AF");
		if(mainModule.declarationKind==ObjectKind.Procedure) return(Global.packetName+"/PROCEDURE.AF");
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
	public ProgramModule(AstBuilder astBuilder) {
		super(astBuilder);
//		String debugName = "ProgramModule";
//		astBuilder.startSubtree(PsiTree.Kind.programModule, debugName);

		sysin=new VariableExpression(null, "sysin");   sysin.SET_SEMANTICS_CHECKED();
		sysout=new VariableExpression(null, "sysout"); sysout.SET_SEMANTICS_CHECKED();
//		try	{
			if(Option.internal.TRACE_PARSE) PsiParse.TRACE("Parse Program");
			Global.setScope(StandardClass.BASICIO);		    	// BASICIO Begin
			new ConnectionBlock(null, sysin, null)                     	//    Inspect sysin do
			     .setClassDeclaration(StandardClass.Infile);
			new ConnectionBlock(null, sysout, null)                    	//    Inspect sysout do
			     .setClassDeclaration(StandardClass.Printfile);
			Global.getCurrentScope().sourceBlockLevel=0;
			
			astBuilder.startSubtree(PsiTree.Kind.declaration  , "ExternalDeclaration");
			
			while(PsiParse.accept(astBuilder, KeyWord.EXTERNAL)) {
//				externalHead = ExternalDeclaration.expectExternalDeclaration(astBuilder, StandardClass.BASICIO);		
				Vector<SyntaxElement> external = ExternalDeclaration.expectExternalDeclaration(astBuilder);	
				if(externalHead == null) externalHead = new Vector<SyntaxElement>();
				externalHead.addAll(external);
				PsiParse.expect(astBuilder, KeyWord.SEMICOLON);
				astBuilder.doneSubtree(PsiTree.Kind.declaration, external);
				astBuilder.startSubtree(PsiTree.Kind.declaration, "May be NextDeclaration");
			}
			astBuilder.dropSubtree(PsiTree.Kind.declaration, " not ExternalDeclaration");
			
			// Now: Looking for ( program | procedure-declaration | class-declaration )
			
			
			astBuilder.startSubtree(PsiTree.Kind.declaration, "May be class declaration");
			LexToken mayBeClassIdent=PsiParse.acceptIdentifier(astBuilder);
			if(mayBeClassIdent!=null) {
				if(PsiParse.accept(astBuilder, KeyWord.CLASS)) {
					mainModule=ClassDeclaration.expectClassDeclaration(astBuilder, mayBeClassIdent.edText());
				}
			    else {
					astBuilder.dropSubtree(PsiTree.Kind.declaration, " is not a class identifier");
					astBuilder.startSubtree(PsiTree.Kind.declaration, " MainModule");
			    	mainModule = doParseProgram(astBuilder);
			    }
			}
			else if(PsiParse.accept(astBuilder, KeyWord.CLASS)) mainModule=ClassDeclaration.expectClassDeclaration(astBuilder, null);
			else {
				Type type=PsiParse.acceptType(astBuilder);
			    if(PsiParse.accept(astBuilder, KeyWord.PROCEDURE)) mainModule=ProcedureDeclaration.expectProcedureDeclaration(astBuilder, type);
			    else mainModule = doParseProgram(astBuilder);
			}
			astBuilder.doneSubtree(PsiTree.Kind.declaration, mainModule);
			
			
			StandardClass.BASICIO.declarationList.add(mainModule);
			
			LexToken token = PsiParse.getCurrentParserToken(astBuilder);
			if(token != null && token.keyWord != KeyWord.EOF) {
				astBuilder.startSubtree(PsiTree.Kind.textAfterProgramEnd, "TextAfterProgramEnd");
				Comment dum = new Comment(astBuilder);
				while(!astBuilder.eof()) astBuilder.advanceLexer(); // consume tokens  (add it to 'current tree')
				astBuilder.doneSubtree(PsiTree.Kind.textAfterProgramEnd, dum);
				
				IO.println("NEW ProgramModule - NOTE: FINN EN ANNEN MÅTE Å GJØRE DETTE PÅ");
				
//				IO.println("NEW ProgramModule: TextAfterEnd: \"" + dum.psiTree.getText().replace("\n", "\\n") + '"');
//				String textAfterEnd = dum.getPsiTree().getText().replaceAll("\\s+", ""); // Remove WhiteSpaces			
//				IO.println("NEW ProgramModule: TextAfterEnd: \"" + textAfterEnd + '"');
//				astBuilder.psiRoot.printTree("");
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
	private DeclarationScope doParseProgram(final AstBuilder astBuilder) {
//		BlockDeclaration mainBlock = new MaybeBlockDeclaration(astBuilder, "MainBlock: " + Global.sourceName);
		BlockDeclaration mainBlock = new MaybeBlockDeclaration(astBuilder, Global.sourceName);
		
		mainBlock.isMainModule = true;
		mainBlock.declarationKind = ObjectKind.SimulaProgram;
		Statement program = Statement.acceptStatement(astBuilder);
		mainBlock.statements.add(program);
//		astBuilder.getRoot().printPsiTree("DONE ProgramModule.doParseProgram");
		return mainBlock;
	}

	@Override
	public void doChecking() {
		if(IS_SEMANTICS_CHECKED()) return;
		Global.enterScope(mainModule);
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
		Global.sourceLineNumber = firstLineNumber();
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
		return (mainModule==null)?"MAIN":mainModule.identifier;
	}
	
}
