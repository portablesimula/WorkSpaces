package com.intellij.lang;

//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;
//
//import com.intellij.openapi.fileTypes.FileTypeRegistry;
//import com.intellij.openapi.fileTypes.LanguageFileType;
//
//public class Language {
//
//	public static final Language ANY = null;
//
//	public Language(String string, String string2) {
//		// TODO Auto-generated constructor stub
//	}
//
//	public boolean isKindOf(@NotNull Language baseLanguage) {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	public @Nullable LanguageFileType getAssociatedFileType() {
//		return FileTypeRegistry.getInstance().findFileTypeByLanguage(this);
//	}
//
//	public @NotNull String getDisplayName() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//}


import com.intellij.diagnostic.ImplementationConflictException;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.extensions.PluginDescriptor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.fileTypes.FileTypeRegistry;
import com.intellij.openapi.fileTypes.LanguageFileType;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.UserDataHolderBase;
import com.intellij.openapi.util.text.Strings;
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry;
import com.intellij.psi.tree.IElementType;
import com.intellij.util.ArrayUtilRt;
import com.intellij.util.containers.Java11Shim;
import com.intellij.util.containers.UtilKt;

import kotlinx.collections.immutable.ExtensionsKt;
import kotlinx.collections.immutable.PersistentList;
import kotlinx.collections.immutable.PersistentSet;
import simula.compiler.utilities.LOG;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
//import kotlinx.collections.immutable.ExtensionsKt;
//import kotlinx.collections.immutable.PersistentList;
//import kotlinx.collections.immutable.PersistentSet;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Unmodifiable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class Language extends UserDataHolderBase {
    public static final Language[] EMPTY_ARRAY = new Language[0];
    private static final Object staticLock = new Object();
    private static volatile Map<Class<? extends Language>, @NotNull Language> registeredLanguages;
    private static volatile Map<String, PersistentList<Language>> registeredMimeTypes;
    private static volatile Map<String, Language> registeredIds;
    private final Language myBaseLanguage;
    private final String myID;
    private final String[] myMimeTypes;
    private final Object instanceLock;
    private volatile PersistentList<Language> dialects;
    private volatile PersistentSet<@NotNull Language> transitiveDialects;
    public static final Language ANY;

    protected Language(@NonNls @NotNull String ID) {
        this(ID, ArrayUtilRt.EMPTY_STRING_ARRAY);
    }

    protected Language(@NonNls @NotNull String ID, @NotNull String... mimeTypes) {
        this((Language)null, ID, mimeTypes);
    }

    protected Language(@Nullable Language baseLanguage, @NonNls @NotNull String ID, @NotNull String... mimeTypes) {
        this.instanceLock = new Object();
        System.out.println("NEW Language: CODE REMOVED-1");
//        this.dialects = ExtensionsKt.persistentListOf();
//        this.transitiveDialects = ExtensionsKt.persistentHashSetOf();
        if (baseLanguage instanceof MetaLanguage) {
            throw new ImplementationConflictException("MetaLanguage cannot be a base language.\nThis language: '" + ID + "'\nBase language: '" + baseLanguage.getID() + "'", (Throwable)null, new Object[]{this, baseLanguage});
        } else {
            this.myBaseLanguage = baseLanguage;
            this.myID = ID;
            this.myMimeTypes = mimeTypes.length == 0 ? ArrayUtilRt.EMPTY_STRING_ARRAY : mimeTypes;
            System.out.println("NEW Language: CODE REMOVED-2");
//            Class<? extends Language> langClass = this.getClass();
//            synchronized(staticLock) {
//                Language existing = (Language)registeredLanguages.get(langClass);
//                if (existing != null) {
//                    throw new ImplementationConflictException("Language of '" + langClass + "' is already registered: " + existing, (Throwable)null, new Object[]{existing, this});
//                }
//
//                existing = (Language)registeredIds.get(ID);
//                if (existing != null) {
//                    throw new ImplementationConflictException("Language with ID '" + ID + "' is already registered: " + existing.getClass(), (Throwable)null, new Object[]{existing, this});
//                }
//
//                registeredLanguages = UtilKt.with(registeredLanguages, langClass, this);
//                registeredIds = UtilKt.with(registeredIds, ID, this);
//
//                for(String mimeType : mimeTypes) {
//                    if (!Strings.isEmpty(mimeType)) {
//                        PersistentList<Language> list = (PersistentList)registeredMimeTypes.get(mimeType);
//                        registeredMimeTypes = UtilKt.with(registeredMimeTypes, mimeType, list == null ? ExtensionsKt.persistentListOf(new Language[]{this}) : list.add(this));
//                    }
//                }
//            }

            if (baseLanguage != null) {
                synchronized(baseLanguage.instanceLock) {
                    baseLanguage.dialects = baseLanguage.dialects.add(this);
                }

//                for(; baseLanguage != null; baseLanguage = baseLanguage.getBaseLanguage()) {
//                    synchronized(baseLanguage.instanceLock) {
//                        baseLanguage.transitiveDialects = baseLanguage.transitiveDialects.add(this);
//                    }
//                }
            }

        }
    }

    public static @Unmodifiable @NotNull Collection<Language> getRegisteredLanguages() {
        return registeredLanguages.values();
    }

    @Internal
    public static void unregisterAllLanguagesIn(@NotNull ClassLoader classLoader, @NotNull PluginDescriptor pluginDescriptor) {
    	System.out.println("Language.unregisterAllLanguagesIn: CODE REMOVED");
//        for(Map.Entry<Class<? extends Language>, Language> e : registeredLanguages.entrySet()) {
//            Class<? extends Language> clazz = (Class)e.getKey();
//            Language language = (Language)e.getValue();
//            if (clazz.getClassLoader() == classLoader) {
//                language.unregisterLanguage(pluginDescriptor);
//            }
//        }
//
//        IElementType.unregisterElementTypes(classLoader, pluginDescriptor);
    }

    @Internal
    public void unregisterLanguage(@NotNull PluginDescriptor pluginDescriptor) {
    	System.out.println("Language.unregisterLanguage: CODE REMOVED");
//        IElementType.unregisterElementTypes(this, pluginDescriptor);
//        Application application = ApplicationManager.getApplication();
//        ReferenceProvidersRegistry referenceProvidersRegistry = application == null ? null : (ReferenceProvidersRegistry)application.getServiceIfCreated(ReferenceProvidersRegistry.class);
//        if (referenceProvidersRegistry != null) {
//            referenceProvidersRegistry.unloadProvidersFor(this);
//        }
//
//        synchronized(staticLock) {
//            registeredLanguages = UtilKt.without(registeredLanguages, this.getClass());
//            registeredIds = UtilKt.without(registeredIds, this.getID());
//
//            for(String mimeType : this.getMimeTypes()) {
//                registeredMimeTypes = UtilKt.without(registeredMimeTypes, mimeType);
//            }
//        }
//
//        Language baseLanguage = this.getBaseLanguage();
//        if (baseLanguage != null) {
//            baseLanguage.unregisterDialect(this);
//        }
    }

    @Internal
    public void unregisterDialect(@NotNull Language language) {
        synchronized(this.instanceLock) {
            this.dialects = this.dialects.remove(language);
        }

        for(Language baseLanguage = this; baseLanguage != null; baseLanguage = baseLanguage.getBaseLanguage()) {
            synchronized(baseLanguage.instanceLock) {
                baseLanguage.transitiveDialects = baseLanguage.transitiveDialects.remove(language);
            }
        }

    }

    public static <T extends Language> T findInstance(@NotNull Class<T> klass) {
        return (T)(registeredLanguages.get(klass));
    }

    public static @Unmodifiable @NotNull Collection<Language> findInstancesByMimeType(@Nullable String mimeType) {
        List<Language> result = mimeType == null ? null : (List)registeredMimeTypes.get(mimeType);
        return result == null ? Collections.emptyList() : result;
    }

    public String toString() {
        return "Language: " + this.myID;
    }

    public String @NotNull [] getMimeTypes() {
        String[] var10000 = this.myMimeTypes;
        if (var10000 == null) {
            LOG.error("$$$reportNull$$$0(12)");
        }

        return var10000;
    }

    public @NotNull @NlsSafe String getID() {
        return this.myID;
    }

    public @Nullable LanguageFileType getAssociatedFileType() {
        return FileTypeRegistry.getInstance().findFileTypeByLanguage(this);
    }

    @Internal
    public @Nullable LanguageFileType findMyFileType(FileType @NotNull [] types) {
        if (types == null) {
            LOG.error("$$$reportNull$$$0(14)");
        }

        for(FileType fileType : types) {
            if (fileType instanceof LanguageFileType) {
                LanguageFileType languageFileType = (LanguageFileType)fileType;
                if (languageFileType.getLanguage() == this && !languageFileType.isSecondary()) {
                    return languageFileType;
                }
            }
        }

        for(FileType fileType : types) {
            if (fileType instanceof LanguageFileType) {
                LanguageFileType languageFileType = (LanguageFileType)fileType;
                if (this.isKindOf(languageFileType.getLanguage()) && !languageFileType.isSecondary()) {
                    return languageFileType;
                }
            }
        }

        return null;
    }

    public @Nullable Language getBaseLanguage() {
        return this.myBaseLanguage;
    }

    public @NotNull @NlsSafe String getDisplayName() {
        return this.getID();
    }

    public final boolean is(Language another) {
        return this == another;
    }

    public boolean isCaseSensitive() {
        return this.myBaseLanguage != null && this.myBaseLanguage.isCaseSensitive();
    }

    @Contract(
        pure = true
    )
    public final boolean isKindOf(Language another) {
        for(Language l = this; l != null; l = l.getBaseLanguage()) {
            if (l.is(another)) {
                return true;
            }
        }

        return false;
    }

    public final boolean isKindOf(@NotNull @NonNls String anotherLanguageId) {
        for(Language l = this; l != null; l = l.getBaseLanguage()) {
            if (l.getID().equals(anotherLanguageId)) {
                return true;
            }
        }

        return false;
    }

    public @Unmodifiable @NotNull List<Language> getDialects() {
    	System.out.println("Language.getDialects: CODE CHANGED: return null");
//        return this.dialects;
    	return null;
    }

    public static @Nullable Language findLanguageByID(@NonNls String id) {
        return id == null ? null : (Language)registeredIds.get(id);
    }

    protected Language(@NotNull String ID, boolean register) {
        this.instanceLock = new Object();
        this.dialects = ExtensionsKt.persistentListOf();
        this.transitiveDialects = ExtensionsKt.persistentHashSetOf();
        Language language = findLanguageByID(ID);
        if (language != null) {
            throw new IllegalArgumentException("Language with ID=" + ID + " already registered: " + language + "; " + language.getClass());
        } else {
            this.myID = ID;
            this.myBaseLanguage = null;
            this.myMimeTypes = null;
        }
    }

    @Internal
    protected void registerDialect(@NotNull Language dialect) {
    	System.out.println("Language.registerDialect: CODE CHANGED:");
//        synchronized(this.instanceLock) {
//            this.dialects = this.dialects.add(dialect);
//        }
//
//        for(Language baseLanguage = this; baseLanguage != null; baseLanguage = baseLanguage.getBaseLanguage()) {
//            synchronized(baseLanguage.instanceLock) {
//                baseLanguage.transitiveDialects = baseLanguage.transitiveDialects.add(dialect);
//            }
//        }
    }

    public @Unmodifiable @NotNull Collection<@NotNull Language> getTransitiveDialects() {
        return this.transitiveDialects;
    }

    static {
    	System.out.println("Language'static block: CODE CHANGED:");
//        registeredLanguages = Java11Shim.Companion.getINSTANCE().mapOf();
//        registeredMimeTypes = Java11Shim.Companion.getINSTANCE().mapOf();
//        registeredIds = Java11Shim.Companion.getINSTANCE().mapOf();
        ANY = new Language("") {
            public String toString() {
                return "Language: ANY";
            }

            public @Nullable LanguageFileType getAssociatedFileType() {
                return null;
            }
        };
    }
}
