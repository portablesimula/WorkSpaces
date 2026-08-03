package simula.token;

import javax.lang.model.SourceVersion;

import simula.Option;
import simula.builder.SimulaLexer;
import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Util;
import simula.lsp.compiler.TokenManager;

public class Identifier extends LexToken {
	public String value;

	public Identifier(int tokenStartLine, CharSequence sourceText, int column, int length, SimulaLexer lexer) {
//		super(tokenStartLine, sourceText, startOffset, endOffset, KeyWord.CHARACTERKONST);
		super(tokenStartLine, sourceText, column, length, KeyWord.IDENTIFIER, lexer);
		this.value = this.edTokenText(lexer);
		if(SourceVersion.isKeyword(value)) value = "_" + value;
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
		if(value == null) Util.IERR("");
	}
	
	public Identifier(String value) {
		super(KeyWord.IDENTIFIER);
		this.value = value;
		this.tokenText = value;
		if(value == null) Util.IERR("");
	}

	@Override
	public String edText() {
		return value;
	}
	
	public boolean equals(Identifier other) {
		return this.value.equals(other.value);
	}
	
	public boolean equals(String other) {
		return this.value.equals(other);
	}
	
	public boolean equalsIgnoreCase(Identifier other) {
		return this.value.equalsIgnoreCase(other.value);
	}
	
	public boolean equalsIgnoreCase(String other) {
		return this.value.equalsIgnoreCase(other);
	}

	public int getLspTokenType() {
		return TokenManager.OTHER.index; 
	}

	@Override
	public String toString() {
		return super.toString() + ", Value: \"" + value + '"';
	}
}
