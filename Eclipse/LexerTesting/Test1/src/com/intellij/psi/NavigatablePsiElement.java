package com.intellij.psi;

import com.intellij.navigation.NavigationItem;

public interface NavigatablePsiElement extends PsiElement, NavigationItem {
    NavigatablePsiElement[] EMPTY_NAVIGATABLE_ELEMENT_ARRAY = new NavigatablePsiElement[0];
}
