
// Her er koden oversatt til Java:
	
	// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
	package com.intellij.ide.plugins;

	import com.intellij.openapi.progress.ProcessCanceledException;
	import com.intellij.util.messages.Topic;
	import org.jetbrains.annotations.ApiStatus;
	import org.jetbrains.annotations.NotNull;

	@Deprecated(since = "Use DynamicPluginVetoer instead")
	class CannotUnloadPluginException extends ProcessCanceledException {
	    public CannotUnloadPluginException(@NotNull String value) {
	        super(new RuntimeException(value));
	    }
	}

	public interface DynamicPluginListener {
	    @Topic.AppLevel
	    Topic<DynamicPluginListener> TOPIC = new Topic<>(
	        DynamicPluginListener.class, 
	        Topic.BroadcastDirection.TO_DIRECT_CHILDREN, 
	        true
	    );

	    @ApiStatus.Experimental
	    default void beforePluginsLoaded() {
	    }

	    default void beforePluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
	    }

	    default void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
	    }

	    @ApiStatus.Experimental
	    default void pluginsLoaded() {
	    }

	    /**
	     * @param isUpdate {@code true} if the plugin is being unloaded as part of an update installation and a new version will be loaded afterwards
	     */
	    default void beforePluginUnload(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
	    }

	    default void pluginUnloaded(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
	    }

	    @Deprecated(since = "Use DynamicPluginVetoer instead")
	    default void checkUnloadPlugin(@NotNull IdeaPluginDescriptor pluginDescriptor) throws CannotUnloadPluginException {
	    }
	}
