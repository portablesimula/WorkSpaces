package com.intellij.diagnostic;

import com.intellij.ide.plugins.PluginUtil;
import com.intellij.openapi.extensions.PluginId;
import com.intellij.util.containers.ContainerUtil;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public final class ImplementationConflictException extends RuntimeException {
    private static final @NotNull PluginId CORE_PLUGIN_ID = PluginId.getId("com.intellij");
    private final @NotNull Set<PluginId> myConflictingPluginIds;

    public ImplementationConflictException(@NotNull String message, Throwable cause, @NotNull Object... implementationObjects) {
        if (implementationObjects == null) {
            $$$reportNull$$$0(1);
        }

        super(message + ". Conflicting plugins: " + calculateConflicts(implementationObjects), cause);
        this.myConflictingPluginIds = calculateConflicts(implementationObjects);
    }

    private static @NotNull Set<PluginId> calculateConflicts(@NotNull Object... implementationObjects) {
        if (implementationObjects == null) {
            $$$reportNull$$$0(2);
        }

        Set<PluginId> myConflictingPluginIds = new HashSet();

        for(Object object : implementationObjects) {
            ClassLoader classLoader = object.getClass().getClassLoader();
            myConflictingPluginIds.add(PluginUtil.getPluginId(classLoader));
        }

        return myConflictingPluginIds;
    }

    public @NotNull Set<PluginId> getConflictingPluginIds() {
        return new HashSet(ContainerUtil.subtract(this.myConflictingPluginIds, Collections.singleton(CORE_PLUGIN_ID)));
    }

    public boolean isConflictWithPlatform() {
        return this.myConflictingPluginIds.contains(CORE_PLUGIN_ID);
    }
}
