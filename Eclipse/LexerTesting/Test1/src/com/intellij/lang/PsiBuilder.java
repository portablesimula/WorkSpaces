package com.intellij.lang;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.util.NlsContexts.ParsingError;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.diff.FlyweightCapableTreeStructure;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public interface PsiBuilder extends SyntaxTreeBuilder, UserDataHolder {
    Project getProject();

    @NotNull ASTNode getTreeBuilt();

    @NotNull FlyweightCapableTreeStructure<LighterASTNode> getLightTree();

    @NotNull Marker mark();

    /** @deprecated */
    @Deprecated
    @ScheduledForRemoval
    default <T> @Nullable T getUserDataUnprotected(@NotNull Key<T> key) {
        return (T)this.getUserData(key);
    }

    /** @deprecated */
    @Deprecated
    @ScheduledForRemoval
    default <T> void putUserDataUnprotected(@NotNull Key<T> key, @Nullable T value) {
        this.putUserData(key, value);
    }

    public interface Marker extends SyntaxTreeBuilder.Marker {
        @NotNull Marker precede();

        default void doneBefore(@NotNull IElementType type, SyntaxTreeBuilder.@NotNull Marker before) {
            if (before == null) {
                $$$reportNull$$$0(1);
            }

            this.doneBefore(type, (Marker)before);
        }

        default void doneBefore(@NotNull IElementType type, SyntaxTreeBuilder.@NotNull Marker before, @NotNull @ParsingError String errorMessage) {
            if (before == null) {
                $$$reportNull$$$0(4);
            }

            this.doneBefore(type, (Marker)before, errorMessage);
        }

        default void errorBefore(@NotNull @ParsingError String message, SyntaxTreeBuilder.@NotNull Marker before) {
            if (before == null) {
                $$$reportNull$$$0(6);
            }

            this.errorBefore(message, (Marker)before);
        }

        void doneBefore(@NotNull IElementType var1, @NotNull Marker var2);

        void doneBefore(@NotNull IElementType var1, @NotNull Marker var2, @NotNull @ParsingError String var3);

        void errorBefore(@NotNull @ParsingError String var1, @NotNull Marker var2);
    }
}
