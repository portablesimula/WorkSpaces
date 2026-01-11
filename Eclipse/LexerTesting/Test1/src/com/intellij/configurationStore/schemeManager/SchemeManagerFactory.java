// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.configurationStore.schemeManager;

import com.intellij.configurationStore.SettingsSavingComponent;
import com.intellij.configurationStore.StreamProvider;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ComponentManager;
import com.intellij.openapi.components.RoamingType;
import com.intellij.openapi.components.SettingsCategory;
import com.intellij.openapi.components.impl.stores.IProjectStore;
import com.intellij.openapi.components.impl.stores.IComponentStore;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.options.Scheme;
import com.intellij.openapi.options.SchemeManager;
import com.intellij.openapi.options.SchemeManagerFactory;
import com.intellij.openapi.options.SchemeProcessor;
import com.intellij.openapi.progress.ProcessCanceledException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.RefreshQueue;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.intellij.util.ExceptionUtil;
import com.intellij.util.containers.ContainerUtil;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlinx.coroutines.CoroutineScope;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;

class ApplicationSchemeManagerFactory extends SchemeManagerFactoryBase {
    ApplicationSchemeManagerFactory(CoroutineScope coroutineScope) {
        super(ApplicationManager.getApplication(), coroutineScope);
    }

    @NotNull
    @Override
    String checkPath(@NotNull String originalPath) {
        String path = super.checkPath(originalPath);
        if (path.startsWith(ROOT_CONFIG)) {
            path = path.substring(ROOT_CONFIG.length() + 1);
            String message = "Path must not contains ROOT_CONFIG macro, corrected: " + path;
            if (ApplicationManager.getApplication().isUnitTestMode()) {
                throw new AssertionError(message);
            } else {
                // LOG defineres vanligvis statisk i klassen
                Logger.getInstance(ApplicationSchemeManagerFactory.class).warn(message);
            }
        }
        return path;
    }

    @Override
    protected Path pathToFile(@NotNull String path) {
        // Forenklet tilgang til stateStore i Java
        return ApplicationManager.getApplication().getService(IComponentStore.class)
                .getStorageManager().expandMacro(ROOT_CONFIG).resolve(path);
    }
}

class ProjectSchemeManagerFactory extends SchemeManagerFactoryBase {
    private final Project project;

    ProjectSchemeManagerFactory(@NotNull Project project, CoroutineScope coroutineScope) {
        super(project, coroutineScope);
        this.project = project;
    }

    @Override
    protected Function1<SchemeManagerImpl<?, ?>, Unit> createFileChangeSubscriber() {
        return (schemeManager) -> {
            // LISTEN_SCHEME_VFS_CHANGES_IN_TEST_MODE må være tilgjengelig som en statisk variabel
            if (!ApplicationManager.getApplication().isUnitTestMode() || Boolean.TRUE.equals(project.getUserData(SchemeManagerKt.LISTEN_SCHEME_VFS_CHANGES_IN_TEST_MODE))) {
                project.getMessageBus().simpleConnect().subscribe(VirtualFileManager.VFS_CHANGES, new SchemeFileTracker(schemeManager, project));
            }
            return Unit.INSTANCE;
        };
    }

    @Override
    protected Path pathToFile(@NotNull String path) {
        if (project.isDefault()) {
            return Path.of("__not_existent_path__");
        }

        IProjectStore projectStore = (IProjectStore) project.getService(IComponentStore.class);
        Path projectFileDir = projectStore != null ? projectStore.getDirectoryStorePath() : null;

        if (projectFileDir != null) {
            return projectFileDir.resolve(path);
        } else if (projectStore != null) {
            return projectStore.getProjectBasePath().resolve("." + path);
        } else {
            return Path.of(project.getBasePath(), "." + path);
        }
    }
}
