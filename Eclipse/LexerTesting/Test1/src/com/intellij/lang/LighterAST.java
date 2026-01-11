package com.intellij.lang;

import com.intellij.util.CharTable;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;

public abstract class LighterAST {
    private final CharTable myCharTable;

    public LighterAST(@NotNull CharTable charTable) {
        this.myCharTable = charTable;
    }

    public @NotNull CharTable getCharTable() {
        return this.myCharTable;
    }

    public abstract @NotNull LighterASTNode getRoot();

    public abstract @Nullable LighterASTNode getParent(@NotNull LighterASTNode var1);

    public abstract @Unmodifiable @NotNull List<LighterASTNode> getChildren(@NotNull LighterASTNode var1);
}
