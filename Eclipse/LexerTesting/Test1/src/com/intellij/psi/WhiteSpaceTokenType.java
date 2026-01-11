package com.intellij.psi;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
//import com.intellij.lang.LanguageParserDefinitions;
//import com.intellij.lang.ParserDefinition;
import com.intellij.psi.tree.IElementType;
//import com.intellij.psi.tree.IReparseableLeafElementType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class WhiteSpaceTokenType extends IElementType {// implements IReparseableLeafElementType<ASTNode> {
    WhiteSpaceTokenType() {
        super("WHITE_SPACE", Language.ANY);
    }

//    public @Nullable ASTNode reparseLeaf(@NotNull ASTNode leaf, @NotNull CharSequence newText) {
//        Language contextLanguage = leaf.getPsi().getLanguage();
//        if (contextLanguage == Language.ANY) {
//            return null;
//        } else {
//            ParserDefinition parserDefinition = (ParserDefinition)LanguageParserDefinitions.INSTANCE.forLanguage(contextLanguage);
//            if (parserDefinition == null) {
//                return null;
//            } else {
//                for(int i = 0; i < newText.length(); ++i) {
//                    if (!Character.isWhitespace(newText.charAt(i))) {
//                        return null;
//                    }
//                }
//
//                return parserDefinition.reparseSpace(leaf, newText);
//            }
//        }
//    }
}
