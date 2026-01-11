// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.vfs.limits;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.PersistentFSConstants;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
@ApiStatus.Internal
public interface FileSizeLimit {
    
    @NotNull
    List<String> getAcceptableExtensions();

    @NotNull
    ExtensionSizeLimitInfo getLimits();

    ExtensionPointName<FileSizeLimit> EP = ExtensionPointName.create("com.intellij.fileEditor.fileSizeChecker");

    AtomicReference<Map<String, ExtensionSizeLimitInfo>> limitsByExtension = new AtomicReference<>(null);

    // Statisk initialisering tilsvarer Kotlin's companion object init-blokk
    static void init() {
        var application = ApplicationManager.getApplication();
        if (application != null) {
            var extensionPoint = application.getExtensionArea().getExtensionPointIfRegistered(EP.getName());
            if (extensionPoint != null) {
                extensionPoint.addChangeListener(() -> limitsByExtension.set(null), null);
            }
        }
    }

    private static Map<String, ExtensionSizeLimitInfo> getLimitsByExtension() {
        Map<String, ExtensionSizeLimitInfo> current = limitsByExtension.get();
        if (current != null) return current;
        
        return limitsByExtension.updateAndGet(old -> old != null ? old : calculateLimits());
    }

    /**
     * Fingerprint of the current state (to contribute to indexing fingerprint)
     */
    static int getFingerprint() {
        return getLimitsByExtension().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.toList())
                .hashCode();
    }

    static boolean isTooLarge(long fileSize, @Nullable String extension) {
        int fileContentLoadLimit = getContentLoadLimit(extension);
        return fileSize > fileContentLoadLimit;
    }

    static int getContentLoadLimit(@Nullable String extension) {
        ExtensionSizeLimitInfo applicable = findApplicable(extension != null ? extension : "");
        return applicable != null ? applicable.getContent() : getDefaultContentLoadLimit();
    }

    static int getDefaultContentLoadLimit() {
        return FileUtilRt.LARGE_FOR_CONTENT_LOADING;
    }

    static int getIntellisenseLimit() {
        return getIntellisenseLimit(null);
    }

    static int getIntellisenseLimit(@Nullable String extension) {
        ExtensionSizeLimitInfo applicable = findApplicable(extension != null ? extension : "");
        return applicable != null ? applicable.getIntellijSense() : PersistentFSConstants.getMaxIntellisenseFileSize();
    }

    static int getPreviewLimit(@Nullable String extension) {
        ExtensionSizeLimitInfo applicable = findApplicable(extension != null ? extension : "");
        return applicable != null ? applicable.getPreview() : FileUtilRt.LARGE_FILE_PREVIEW_SIZE;
    }

    private static Map<String, ExtensionSizeLimitInfo> calculateLimits() {
        List<FileSizeLimit> extensions = EP.getExtensionList();

        Map<String, Long> counts = extensions.stream()
                .flatMap(e -> e.getAcceptableExtensions().stream())
                .collect(Collectors.groupingBy(ext -> ext, Collectors.counting()));

        counts.forEach((element, count) -> {
            if (count > 1) {
                String contributors = extensions.stream()
                        .filter(e -> e.getAcceptableExtensions().contains(element))
                        .map(e -> e.getClass().getName() + ": " + String.join(", ", e.getAcceptableExtensions()))
                        .collect(Collectors.joining("; "));
                
                Logger.getInstance(FileSizeLimit.class).warn("For file type " + element + " " + count + " limits are registered. Extensions: " + contributors);
            }
        });

        return extensions.stream()
                .flatMap(extension -> extension.getAcceptableExtensions().stream()
                        .map(ext -> Map.entry(ext, extension.getLimits())))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (existing, replacement) -> existing));
    }

    @Nullable
    private static ExtensionSizeLimitInfo findApplicable(@NotNull String extension) {
        return getLimitsByExtension().get(extension);
    }
}
