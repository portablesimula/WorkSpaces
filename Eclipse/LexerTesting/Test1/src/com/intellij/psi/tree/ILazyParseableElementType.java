package com.intellij.psi.tree;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.lang.LanguageParserDefinitions;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.PsiParser;
import com.intellij.lexer.Lexer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.psi.ParsingDiagnostics;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ILazyParseableElementType extends IElementType implements ILazyParseableElementTypeBase {
    public static final Key<Language> LANGUAGE_KEY = Key.create("LANGUAGE_KEY");

    public ILazyParseableElementType(@NotNull @NonNls String debugName) {
        this(debugName, (Language)null);
    }

    public ILazyParseableElementType(@NotNull @NonNls String debugName, @Nullable Language language) {
        super(debugName, language);
    }

    public ILazyParseableElementType(@NotNull @NonNls String debugName, @Nullable Language language, boolean register) {
        super(debugName, language, register);
    }

    public ASTNode parseContents(@NotNull ASTNode chameleon) {
        PsiElement parentElement = chameleon.getTreeParent().getPsi();

        assert parentElement != null : "parent psi is null: " + chameleon;

        return this.doParseContents(chameleon, parentElement);
    }

    protected ASTNode doParseContents(@NotNull ASTNode chameleon, @NotNull PsiElement psi) {
        Project project = psi.getProject();
        Language languageForParser = this.getLanguageForParser(psi);
        PsiBuilder builder = PsiBuilderFactory.getInstance().createBuilder(project, chameleon, (Lexer)null, languageForParser, chameleon.getChars());
        PsiParser parser = ((ParserDefinition)LanguageParserDefinitions.INSTANCE.forLanguage(languageForParser)).createParser(project);
        long startTime = System.nanoTime();
        ASTNode node = parser.parse(this, builder);
        ParsingDiagnostics.registerParse(builder, languageForParser, System.nanoTime() - startTime);
        return node.getFirstChildNode();
    }

    protected @NotNull Language getLanguageForParser(@NotNull PsiElement psi) {
        return this.getLanguage();
    }

    public ASTNode createNode(CharSequence text) {
        return null;
    }
}
