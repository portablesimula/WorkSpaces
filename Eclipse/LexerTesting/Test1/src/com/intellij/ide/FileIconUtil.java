
// Her er koden oversatt fra Kotlin til Java.
// Vær oppmerksom på at Kotlin-funksjoner som runCatching og getOrLogException ikke finnes i Java-standardbiblioteket,
// så disse er skrevet om til tradisjonelle try-catch-blokker som speiler logikken i IntelliJ-plattformen. 

//Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.ide;

import com.intellij.diagnostic.PluginException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.IndexNotReadyException;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.extensions.ExtensionPointAnchorableBean;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

@Internal
public final class FileIconUtil {
//	private static final Logger LOG = Logger.getInstance(FileIconUtil.class);

	private FileIconUtil() {
	}

	@Nullable
	public static Icon getIconFromProviders(@NotNull VirtualFile file, @Iconable.IconFlags int flags, @Nullable Project project) {
		for (var extension : FileIconProvider.EP_NAME.getIterable()) {
			try {
				FileIconProvider instance = extension.getInstance();
				if (instance != null) {
					Icon icon = instance.getIcon(file, flags, project);
					if (icon != null) {
						return icon;
					}
				}
			} catch (Throwable t) {
				handleException(t, extension);
			}
		}
		return null;
	}

	@NotNull
	public static Icon patchIconByIconPatchers(@NotNull Icon icon, @NotNull VirtualFile file, @Iconable.IconFlags int flags, @Nullable Project project) {
		Icon patched = icon;
		for (var extension : FileIconPatcher.EP_NAME.getIterable()) {
			try {
				FileIconPatcher instance = extension.getInstance();
				if (instance != null) {
					Icon result = instance.patchIcon(patched, file, flags, project);
					if (result != null) {
						patched = result;
					}
				}
			} catch (Throwable t) {
				handleException(t, extension);
			}
		}
		return patched;
	}

	private static void handleException(Throwable t, Object extension) {
		if (!(t instanceof IndexNotReadyException)) {
			// Merk: PluginException-håndteringen her emulerer Kotlins getOrLogException
			LOG.warn(new PluginException(
					"Extension " + extension + " threw an exception", 
					t, 
					null // Her hentes normalt pluginId fra extension bean
					));
		}
	}
}
