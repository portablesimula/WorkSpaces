package com.intellij.lang;

import com.intellij.util.CharTable;
import org.jetbrains.annotations.NotNull;

public interface FileASTNode extends ASTNode {
    @NotNull CharTable getCharTable();

    boolean isParsed();

    @NotNull LighterAST getLighterAST();
}
