// Copyright 2000-2025 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.ide.startup.impl;

import com.intellij.diagnostic.LoadingState;
import com.intellij.ide.lightEdit.LightEditCompatible;
import com.intellij.ide.startup.StartupManagerEx;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointListener;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.extensions.PluginDescriptor;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.platform.diagnostic.telemetry.Scope;
import com.intellij.platform.diagnostic.telemetry.TelemetryManager;
import com.intellij.platform.diagnostic.telemetry.Tracer;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.common.Attributes;
import io.opentelemetry.api.trace.Span;
import kotlinx.coroutines.CompletableDeferred;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

import java.util.ArrayDeque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.intellij.platform.ide.progress.TasksKt.withBackgroundProgress;

// Orginal Kotlin code:
// https://github.com/JetBrains/intellij-community/blob/idea/252.27397.103/platform/platform-impl/src/com/intellij/ide/startup/impl/StartupManagerImpl.kt
@ApiStatus.Internal
public class StartupManagerImpl extends StartupManagerEx {
    private static final Logger LOG = Logger.getInstance(StartupManagerImpl.class);
    private static final Tracer tracer = TelemetryManager.getTracer(new Scope("startup", null));
    private static final ExtensionPointName<Object> BACKGROUND_POST_STARTUP_ACTIVITY = 
        new ExtensionPointName<>("com.intellij.backgroundPostStartupActivity");

    private static final int DUMB_AWARE_PASSED = 1;
    private static final int ALL_PASSED = 2;

    private final Project project;
    private final CoroutineScope coroutineScope;
    private final Object lock = new Object();
    private final ArrayDeque<Runnable> initProjectStartupActivities = new ArrayDeque<>();
    private final ArrayDeque<Runnable> postStartupActivities = new ArrayDeque<>();
    private final Map<Class<?>, Job> runningProjectActivities = new ConcurrentHashMap<>();

    private volatile boolean freezePostStartupActivities = false;
    private volatile int postStartupActivitiesPassed = 0;
    
    private final CompletableDeferred<Object> allActivitiesPassed = new CompletableDeferred<>(null);
    private volatile CompletableDeferred<Boolean> isInitProjectActivitiesPassed;

    public StartupManagerImpl(@NotNull Project project, @NotNull CoroutineScope coroutineScope) {
        this.project = project;
        this.coroutineScope = coroutineScope;
        this.isInitProjectActivitiesPassed = new CompletableDeferred<>(JobKt.getJob(coroutineScope.getCoroutineContext()));
    }

    @VisibleForTesting
    public static void addActivityEpListener(@NotNull Project project) {
        StartupActivity.POST_STARTUP_ACTIVITY.addExtensionPointListener(new ExtensionPointListener<Object>() {
            @Override
            public void extensionAdded(@NotNull Object extension, @NotNull PluginDescriptor pluginDescriptor) {
                if (project instanceof LightEditCompatible && !(extension instanceof LightEditCompatible)) {
                    return;
                }

                StartupManagerImpl startupManager = (StartupManagerImpl) getInstance(project);
                if (extension instanceof ProjectActivity) {
                    // I Java må Coroutine-launch håndteres via Helpers eller JpsPlugin-metoder
                    // Her forenklet til logikk-flyt:
                    startupManager.runProjectActivity((ProjectActivity) extension);
                } else if (extension instanceof DumbAware) {
                    startupManager.runOldActivity((StartupActivity) extension);
                } else {
                    DumbService.getInstance(project).runWhenSmart(() -> {
                        startupManager.runOldActivity((StartupActivity) extension);
                    });
                }
            }
        }, project);
    }

    private void checkNonDefaultProject() {
        if (project.isDefault()) {
            LOG.error("Please don't register startup activities for the default project: they won't ever be run");
        }
    }

    @Override
    public void registerStartupActivity(@NotNull Runnable runnable) {
        checkNonDefaultProject();
        if (!isInitProjectActivitiesPassed.isActive()) {
            LOG.error("Registering startup activity that will never be run");
        }
        synchronized (lock) {
            initProjectStartupActivities.add(runnable);
        }
    }

    @Override
    public void registerPostStartupActivity(@NotNull Runnable runnable) {
        Span.current().addEvent("register startup activity", 
            Attributes.of(AttributeKey.stringKey("runnable"), runnable.toString()));

        if (runnable instanceof DumbAware) {
            runAfterOpened(runnable);
        } else {
            LOG.error("Activities registered via registerPostStartupActivity must be dumb-aware: " + runnable);
        }
    }

    @Override
    public boolean startupActivityPassed() {
        return isInitProjectActivitiesPassed.isCompleted();
    }

    @Override
    public Object waitForInitProjectActivities(@Nullable String progressTitle, @NotNull kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        if (isInitProjectActivitiesPassed.isCompleted()) {
            return kotlin.Unit.INSTANCE;
        }

        if (progressTitle == null) {
            return isInitProjectActivitiesPassed.join($completion);
        } else {
            return withBackgroundProgress(project, progressTitle, true, (scope, continuation) -> 
                isInitProjectActivitiesPassed.join(continuation), $completion);
        }
    }

    @Override
    public boolean postStartupActivityPassed() {
        int state = postStartupActivitiesPassed;
        if (state == ALL_PASSED) return true;
        if (state == -1) throw new RuntimeException("Aborted; check the log for a reason");
        return false;
    }

    @Override
    @NotNull
    public CompletableDeferred<Object> getAllActivitiesPassedFuture() {
        return allActivitiesPassed;
    }

    // Interne hjelpemetoder for Coroutine-håndtering i Java
    private void runProjectActivity(ProjectActivity activity) {
        // Implementasjon krever tilgang til Coroutine-interop
    }

    private void runOldActivity(StartupActivity activity) {
        // Implementasjon for å kjøre gamle StartupActivity-grensesnitt
    }
}
