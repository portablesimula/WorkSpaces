package simula.lsp.builder.token;

import javax.lang.model.SourceVersion;

import simula.lsp.builder.SimulaBuilder;
import simula.lsp.syntaxClass.declaration.DeclarationScope;
import simula.lsp.utilities.KeyWord;
import simula.lsp.utilities.LspGlobal;
import simula.lsp.utilities.Option;
import simula.lsp.utilities.Util;

public class Identifier extends LexToken {
	public DeclarationScope scope;
	public String value;

	public Identifier(SimulaBuilder simBuilder, int tokenStartLine, int column, int length) {
		super(simBuilder, tokenStartLine, column, length, KeyWord.IDENTIFIER, SemanticTokenTypes.Type);
		this.value = this.edTokenText(simBuilder.lexer);
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
		if(other == null) return false;
		return this.value.equals(other.value);
	}
	
	public boolean equals(String other) {
		return this.value.equals(other);
	}
	
	public boolean equalsIgnoreCase(Identifier other) {
		if(other == null) return false;
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
