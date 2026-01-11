package com.intellij.openapi.extensions;

import com.intellij.openapi.util.NlsSafe;
import java.io.File;
import java.nio.file.Path;
import java.util.Date;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;

public interface PluginDescriptor {
    @NotNull PluginId getPluginId();

    @Nullable ClassLoader getPluginClassLoader();

    @Experimental
    default @NotNull ClassLoader getClassLoader() {
        ClassLoader classLoader = this.getPluginClassLoader();
        return classLoader == null ? this.getClass().getClassLoader() : classLoader;
    }

    default boolean isBundled() {
        return false;
    }

    /** @deprecated */
    @Deprecated
    default File getPath() {
        Path path = this.getPluginPath();
        return path == null ? null : path.toFile();
    }

    Path getPluginPath();

    @Nullable @Nls String getDescription();

    @Nullable String getChangeNotes();

    @NlsSafe String getName();

    @Nullable String getProductCode();

    @Nullable Date getReleaseDate();

    int getReleaseVersion();

    boolean isLicenseOptional();

    @Nullable @NlsSafe String getVendor();

    default @Nullable @NlsSafe String getOrganization() {
        return null;
    }

    @NlsSafe String getVersion();

    @Nullable String getResourceBundleBaseName();

    @Nullable @NlsSafe String getCategory();

    default @Nullable @Nls String getDisplayCategory() {
        return this.getCategory();
    }

    @Nullable String getVendorEmail();

    @Nullable String getVendorUrl();

    @Nullable String getUrl();

    @Nullable @NlsSafe String getSinceBuild();

    @Nullable @NlsSafe String getUntilBuild();

    default boolean allowBundledUpdate() {
        return false;
    }

    @Internal
    default boolean isImplementationDetail() {
        return false;
    }

    default boolean isRequireRestart() {
        return false;
    }

    boolean isEnabled();

    void setEnabled(boolean var1);
}
