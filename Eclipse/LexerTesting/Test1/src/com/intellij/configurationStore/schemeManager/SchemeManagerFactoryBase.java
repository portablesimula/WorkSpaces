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

public abstract class SchemeManagerFactoryBase extends SchemeManagerFactory implements SettingsSavingComponent {
    private static final Logger LOG = Logger.getInstance(SchemeManagerFactoryBase.class);
    public static final String ROOT_CONFIG = "$ROOT_CONFIG$";

    private final ComponentManager componentManager;
    private final CoroutineScope coroutineScope;
    private final List<SchemeManagerImpl<Scheme, Scheme>> managers = ContainerUtil.createLockFreeCopyOnWriteList();

    protected SchemeManagerFactoryBase(@Nullable ComponentManager componentManager, @Nullable CoroutineScope coroutineScope) {
        super(componentManager instanceof Project ? (Project) componentManager : null);
        this.componentManager = componentManager;
        this.coroutineScope = coroutineScope;
    }

    protected Function1<SchemeManagerImpl<?, ?>, Unit> createFileChangeSubscriber() {
        return null;
    }

    @ApiStatus.Internal
    @NotNull
    @Override
    public <T extends Scheme, MutableT extends T> SchemeManager<T> create(
            @NotNull String directoryName,
            @NotNull SchemeProcessor<T, MutableT> processor,
            @Nullable String presentableName,
            @NotNull RoamingType roamingType,
            @NotNull SchemeNameToFileName schemeNameToFileName,
            @Nullable StreamProvider streamProvider,
            @Nullable Path directoryPath,
            boolean isAutoSave,
            @NotNull SettingsCategory settingsCategory
    ) {
        String path = checkPath(directoryName);
        Function1<SchemeManagerImpl<?, ?>, Unit> fileChangeSubscriber = null;

        if (streamProvider == null || !streamProvider.isApplicable(path, roamingType)) {
            fileChangeSubscriber = createFileChangeSubscriber();
        }

        if (streamProvider == null && componentManager != null) {
            IComponentStore store = (IComponentStore) ApplicationManager.getApplication().getService(IComponentStore.class); // Forenklet tilgang
            streamProvider = store.getStorageManager().getStreamProvider();
        }

        Path ioDirectory = (directoryPath != null) ? directoryPath : pathToFile(path);

        SchemeManagerImpl<T, MutableT> manager = new SchemeManagerImpl<>(
                getProject(),
                path,
                processor,
                streamProvider,
                ioDirectory,
                roamingType,
                presentableName,
                schemeNameToFileName,
                fileChangeSubscriber,
                settingsCategory,
                coroutineScope
        );

        if (isAutoSave) {
            //noinspection unchecked
            managers.add((SchemeManagerImpl<Scheme, Scheme>) manager);
        }
        return manager;
    }

    @Override
    public void dispose(@NotNull SchemeManager<?> schemeManager) {
        managers.remove(schemeManager);
    }

    @NotNull
    String checkPath(@NotNull String originalPath) {
        if (originalPath.contains("\\")) {
            LOG.error("Path must be system-independent, use forward slash instead of backslash");
        }
        if (originalPath.isEmpty()) {
            LOG.error("Path must not be empty");
        }
        return originalPath;
    }

    protected abstract Path pathToFile(@NotNull String path);

    public void process(@NotNull Function1<SchemeManagerImpl<Scheme, Scheme>, Unit> processor) {
        for (SchemeManagerImpl<Scheme, Scheme> manager : managers) {
            try {
                processor.invoke(manager);
            } catch (CancellationException | ProcessCanceledException e) {
                throw e;
            } catch (Throwable e) {
                LOG.error("Cannot reload settings for " + manager.getClass().getName(), e);
            }
        }
    }

    // Merk: Java støtter ikke 'suspend' direkte. Denne metoden må kalles fra en Coroutine eller gjøres om til en Future.
    @Override
    public Object save(kotlin.coroutines.Continuation<? super Unit> $completion) {
        Throwable error = null;
        List<VFileEvent> events = new ArrayList<>();

        for (SchemeManagerImpl<Scheme, Scheme> registeredManager : managers) {
            try {
                registeredManager.saveImpl(events);
            } catch (CancellationException | ProcessCanceledException e) {
                throw e;
            } catch (Throwable e) {
                error = ExceptionUtil.addSuppressed(error, e);
            }
        }

        if (!events.isEmpty()) {
            RefreshQueue.getInstance().processEvents(false, events);
        }

        if (error != null) {
            throw new RuntimeException(error); // Eller håndter kasteren iht. Java-standard
        }
        return Unit.INSTANCE;
    }

    @TestOnly
    @ApiStatus.Internal
    public static final class TestSchemeManagerFactory extends SchemeManagerFactoryBase {
        private final Path basePath;

        public TestSchemeManagerFactory(@NotNull Path basePath) {
            super(null, null);
            this.basePath = basePath;
        }

        @Override
        protected Path pathToFile(@NotNull String path) {
            return basePath.resolve(path);
        }
    }
}
