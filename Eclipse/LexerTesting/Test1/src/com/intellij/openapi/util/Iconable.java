package com.intellij.openapi.util;

import javax.swing.Icon;

public interface Iconable {
    int ICON_FLAG_VISIBILITY = 1;
    int ICON_FLAG_READ_STATUS = 2;

    Icon getIcon(@Iconable.IconFlags int var1);

    public @interface IconFlags {
    }
}
