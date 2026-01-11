package com.intellij.openapi.project;

import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.extensions.AreaInstance;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.SystemDependent;
import org.jetbrains.annotations.SystemIndependent;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface Project extends ComponentManager, AreaInstance {
    String DIRECTORY_STORE_FOLDER = ".idea";

    @NotNull @NlsSafe String getName();

    /** @deprecated */
    @Deprecated
    VirtualFile getBaseDir();

    @Nullable @SystemIndependent @NonNls String getBasePath();

    @Nullable VirtualFile getProjectFile();

    @Nullable @SystemIndependent @NonNls String getProjectFilePath();

    default @Nullable @SystemDependent @NonNls String getPresentableUrl() {
        return null;
    }

    @Nullable VirtualFile getWorkspaceFile();

    @NotNull @NonNls String getLocationHash();

    void save();

    default void scheduleSave() {
        this.save();
    }

    boolean isOpen();

    boolean isInitialized();

    default boolean isDefault() {
        return false;
    }

    @Internal
    default ComponentManager getActualComponentManager() {
        return this;
    }
}
