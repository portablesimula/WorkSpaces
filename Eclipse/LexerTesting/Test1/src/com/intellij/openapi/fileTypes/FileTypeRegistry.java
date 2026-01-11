package com.intellij.openapi.fileTypes;

import com.intellij.ide.plugins.DynamicPluginListener;
import com.intellij.ide.plugins.IdeaPluginDescriptor;
import com.intellij.lang.Language;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.ExtensionPointName;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.io.ByteSequence;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.messages.MessageBus;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public abstract class FileTypeRegistry {
    private static volatile FileTypeRegistry instance;

    @Internal
    public static void setInstanceSupplier(@NotNull Supplier<? extends FileTypeRegistry> supplier, @NotNull Disposable parentDisposable) {
        FileTypeRegistry oldInstance = instance;
        instance = (FileTypeRegistry)supplier.get();
        Disposer.register(parentDisposable, () -> instance = oldInstance);
    }

    public FileTypeRegistry() {
        Application application = ApplicationManager.getApplication();
        MessageBus messageBus;
        if (application != null && !application.isDisposed() && !(messageBus = application.getMessageBus()).isDisposed()) {
            messageBus.simpleConnect().subscribe(DynamicPluginListener.TOPIC, new DynamicPluginListener() {
                public void pluginUnloaded(@NotNull IdeaPluginDescriptor pluginDescriptor, boolean isUpdate) {
                    CharsetUtil.clearFileTypeCaches();
                }

                public void pluginLoaded(@NotNull IdeaPluginDescriptor pluginDescriptor) {
                    CharsetUtil.clearFileTypeCaches();
                }
            });
        }

    }

    @Internal
    public static boolean isInstanceSupplierSet() {
        return instance != null;
    }

    public abstract boolean isFileIgnored(@NotNull VirtualFile var1);

    public boolean isFileOfType(@NotNull VirtualFile file, @NotNull FileType type) {
        return file.getFileType() == type;
    }

    public @Nullable LanguageFileType findFileTypeByLanguage(@NotNull Language language) {
        return language.findMyFileType(this.getRegisteredFileTypes());
    }

    public static FileTypeRegistry getInstance() {
        FileTypeRegistry cached = instance;
        if (cached == null) {
            Application application = ApplicationManager.getApplication();
            Class<? extends FileTypeRegistry> aClass = null;

            try {
                aClass = (Class<? extends FileTypeRegistry>) Class.forName("com.intellij.openapi.fileTypes.FileTypeManager");
            } catch (ClassNotFoundException var4) {
            }

            Object var10000 = application != null && aClass != null && application.hasComponent(aClass) ? (FileTypeRegistry)application.getService(aClass) : new EmptyFileTypeRegistry();
            cached = (FileTypeRegistry)var10000;
            instance = (FileTypeRegistry)var10000;
        }

        return cached;
    }

    public abstract FileType @NotNull [] getRegisteredFileTypes();

    public abstract @NotNull FileType getFileTypeByFile(@NotNull VirtualFile var1);

    public @NotNull FileType getFileTypeByFile(@NotNull VirtualFile file, byte @Nullable [] content) {
        return this.getFileTypeByFile(file);
    }

    public @NotNull FileType getFileTypeByFileName(@NotNull CharSequence fileNameSeq) {
        return this.getFileTypeByFileName(fileNameSeq.toString());
    }

    public abstract @NotNull FileType getFileTypeByFileName(@NotNull String var1);

    public abstract @NotNull FileType getFileTypeByExtension(@NotNull String var1);

    public abstract FileType findFileTypeByName(@NotNull String var1);

    static {
        ApplicationManager.registerCleaner(() -> instance = null);
    }

    public interface FileTypeDetector {
        ExtensionPointName<FileTypeDetector> EP_NAME = new ExtensionPointName("com.intellij.fileTypeDetector");

        @Nullable FileType detect(@NotNull VirtualFile var1, @NotNull ByteSequence var2, @Nullable CharSequence var3);

        default int getDesiredContentPrefixLength() {
            return 1024;
        }

        /** @deprecated */
        @Deprecated
        @ScheduledForRemoval
        default int getVersion() {
            return 0;
        }
    }
}
