package com.intellij.navigation;

import com.intellij.openapi.util.NlsSafe;
import com.intellij.pom.Navigatable;
import org.jetbrains.annotations.Nullable;

public interface NavigationItem extends Navigatable {
    NavigationItem[] EMPTY_NAVIGATION_ITEM_ARRAY = new NavigationItem[0];

    @Nullable @NlsSafe String getName();

    @Nullable ItemPresentation getPresentation();
}
