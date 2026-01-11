package com.intellij.navigation;

import com.intellij.openapi.util.NlsSafe;
import javax.swing.Icon;
import org.jetbrains.annotations.Nullable;

public interface ItemPresentation {
    @NlsSafe @Nullable String getPresentableText();

    default @NlsSafe @Nullable String getLocationString() {
        return null;
    }

    @Nullable Icon getIcon(boolean var1);
}
