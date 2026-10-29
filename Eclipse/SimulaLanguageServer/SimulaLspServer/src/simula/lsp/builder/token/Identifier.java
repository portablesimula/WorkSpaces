package simula.lsp.builder.token;

import java.util.List;

import javax.lang.model.SourceVersion;

import simula.lsp.builder.SimulaLexer;
import simula.lsp.syntaxClass.declaration.DeclarationScope;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.LspGlobal;
import simula.lsp.utilities.Option;
import simula.lsp.utilities.Util;

public class Identifier extends LexToken {
	public DeclarationScope scope;
	public String value;

	public Identifier(int tokenStartLine, List<String> sourceLines, int column, int length, SimulaLexer lexer) {
		super(tokenStartLine, sourceLines, column, length, KeyWord.IDENTIFIER, SemanticTokenTypes.Type, lexer);
		this.value = this.edTokenText(lexer);
		if(SourceVersion.isKeyword(value)) value = "_" + value;
		if(Option.internal.TRACE_NEW_LEXTOKEN > 0) TRACE_NEW_LEXTOKEN();
		if(value == null) Util.IERR("");
	}
	
	public Identifier(String value) {
		super(KeyWord.IDENTIFIER);
		this.scope = LspGlobal.getCurrentScope();
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

	@Override
	public String toString() {
		return super.toString() + ", Value|" + value + '|';
	}
}
