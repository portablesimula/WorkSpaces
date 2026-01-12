package simula.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import com.intellij.psi.tree.IElementType;

import simula.compiler.utilities.KeyWord;
import simula.compiler.utilities.Util;
import simula.lexer.KeyWordToken;
import simula.lexer.SimulaToken;

public class SimPsiBuilder {
	PsiBuilder builder;
	
	public SimPsiBuilder(PsiBuilder builder, String text) {
		System.out.println("*** NEW SimPsiBuilder");
		this.builder = builder;
	}
	
	public PsiBuilder psiBuilder() { return builder; }

	public IElementType getTokenType() {
		return builder.getTokenType();
	}

	public String getTokenText() {
		return builder.getTokenText();
	}

	public Marker mark() {
		return builder.mark();
	}

	public void advanceLexer() {
		builder.advanceLexer();
	}

	public boolean eof() {
		return builder.eof();
	}
	

//  IElementType tokenType = builder.getTokenType();
//  switch(((KeyWordToken)tokenType).keyWord) {
//  switch(simBuilder.getSimToken().keyWord) {
	public SimulaToken getSimToken() {
		IElementType tokenType = builder.getTokenType();
		return (SimulaToken) tokenType;
	}

    public void consumeUntilSemicolon() {
//        while (!builder.eof() && builder.getTokenType() != KeyWord.SEMICOLON) {
//        Util.IERR("SimPsiBuilder.consumeUntilSemicolon: "+getSimToken());
        while (!builder.eof() && getSimToken().keyWord != KeyWord.SEMICOLON) {
            builder.advanceLexer();
        }
//        if (builder.getTokenType() == KeyWord.SEMICOLON) {
        if (getSimToken().keyWord == KeyWord.SEMICOLON) {
            builder.advanceLexer();
        }
    }

}
