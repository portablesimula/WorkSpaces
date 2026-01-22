package simula.editor;

import simula.compiler.syntaxClass.SyntaxClass;
import simula.compiler.syntaxClass.declaration.MaybeBlockDeclaration;
import simula.compiler.syntaxClass.statement.Statement;
import simula.lexer.SimulaToken;

public class PsiBuilder {

	public class Marker {

		public void error(String string) {
			// TODO Auto-generated method stub
			
		}

		public void done(SyntaxClass cls) {
			// TODO Auto-generated method stub
			
		}

	}

	public Marker mark() {
		// TODO Auto-generated method stub
		return null;
	}

	public void advanceLexer() {
		// TODO Auto-generated method stub
		
	}

	public boolean eof() {
		// TODO Auto-generated method stub
		return false;
	}

	public SimulaToken getTokenType() {
		// TODO Auto-generated method stub
		return null;
	}

}
