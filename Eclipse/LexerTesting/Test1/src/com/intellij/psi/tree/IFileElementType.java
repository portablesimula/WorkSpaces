package com.intellij.psi.tree;

import com.intellij.lang.ASTNode;
import com.intellij.lang.Language;
import com.intellij.psi.PsiElement;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class IFileElementType extends ILazyParseableElementType {
 public IFileElementType(@Nullable Language language) {
     super("FILE", language);
 }

 public IFileElementType(@NonNls @NotNull String debugName, @Nullable Language language) {
     super(debugName, language);
 }

 public IFileElementType(@NonNls @NotNull String debugName, @Nullable Language language, boolean register) {
     super(debugName, language, register);
 }

 public @Nullable ASTNode parseContents(@NotNull ASTNode chameleon) {
     PsiElement psi = chameleon.getPsi();
     if (psi == null) {
         throw new AssertionError("Bad chameleon: " + chameleon + " of type " + chameleon.getElementType() + " in #" + chameleon.getElementType().getLanguage());
     } else {
         return this.doParseContents(chameleon, psi);
     }
 }
}
