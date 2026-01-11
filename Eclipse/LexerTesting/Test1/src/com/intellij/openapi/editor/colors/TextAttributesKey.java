package com.intellij.openapi.editor.colors;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.JDOMExternalizerUtil;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.openapi.util.NullableLazyValue;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ObjectUtils;
import com.intellij.util.containers.JBIterable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.jdom.Element;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public final class TextAttributesKey implements Comparable<TextAttributesKey> {
    public static final TextAttributesKey[] EMPTY_ARRAY = new TextAttributesKey[0];
    private static final Logger LOG = Logger.getInstance(TextAttributesKey.class);
    private static final String TEMP_PREFIX = "TEMP::";
    private static final TextAttributes NULL_ATTRIBUTES = new TextAttributes();
    private static final ConcurrentMap<String, TextAttributesKey> ourRegistry = new ConcurrentHashMap();
    private static final NullableLazyValue<TextAttributeKeyDefaultsProvider> ourDefaultsProvider = NullableLazyValue.volatileLazyNullable(() -> (TextAttributeKeyDefaultsProvider)ApplicationManager.getApplication().getService(TextAttributeKeyDefaultsProvider.class));
    private final @NotNull String myExternalName;
    private final TextAttributes myDefaultAttributes;
    private final TextAttributesKey myFallbackAttributeKey;
    private static final ThreadLocal<Set<String>> CALLED_RECURSIVELY = ThreadLocal.withInitial(() -> new HashSet());
    /** @deprecated */
    @Deprecated
    static final TextAttributesKey DUMMY_DEPRECATED_ATTRIBUTES = createTextAttributesKey("__deprecated__");

    private TextAttributesKey(@NotNull String externalName, TextAttributes defaultAttributes, TextAttributesKey fallbackAttributeKey) {
        this.myExternalName = externalName;
        this.myDefaultAttributes = defaultAttributes;
        this.myFallbackAttributeKey = fallbackAttributeKey;
        if (fallbackAttributeKey != null) {
            this.checkForCycle(fallbackAttributeKey);
        }

    }

    private void checkForCycle(@NotNull TextAttributesKey fallbackAttributeKey) {
        for(TextAttributesKey key = fallbackAttributeKey; key != null; key = key.myFallbackAttributeKey) {
            if (this.equals(key)) {
                throw new IllegalArgumentException("Can't use this fallback key: " + fallbackAttributeKey + ": Cycle detected: " + StringUtil.join(JBIterable.generate(this.myFallbackAttributeKey, (o) -> o == this ? null : o.myFallbackAttributeKey), "->"));
            }
        }

    }

    public TextAttributesKey(@NotNull Element element) {
        String name = JDOMExternalizerUtil.readField(element, "myExternalName");
        Element myDefaultAttributesElement = JDOMExternalizerUtil.readOption(element, "myDefaultAttributes");
        TextAttributes defaultAttributes = myDefaultAttributesElement == null ? null : new TextAttributes(myDefaultAttributesElement);
        this.myExternalName = (String)Objects.requireNonNull(name);
        this.myDefaultAttributes = defaultAttributes;
        this.myFallbackAttributeKey = null;
    }

    public static @NotNull TextAttributesKey find(@NotNull @NonNls String externalName) {
        return (TextAttributesKey)ourRegistry.computeIfAbsent(externalName, (name) -> new TextAttributesKey(name, (TextAttributes)null, (TextAttributesKey)null));
    }

    public @NlsSafe String toString() {
        return this.myExternalName + (this.myFallbackAttributeKey == null && this.myDefaultAttributes == null ? "" : " (") + (this.myFallbackAttributeKey == null ? "" : "fallbackKey: " + this.myFallbackAttributeKey) + (this.myDefaultAttributes == null ? "" : "; defaultAttributes: " + this.myDefaultAttributes) + (this.myFallbackAttributeKey == null && this.myDefaultAttributes == null ? "" : ")");
    }

    public @NotNull @NlsSafe String getExternalName() {
        return this.myExternalName;
    }

    public int compareTo(@NotNull TextAttributesKey key) {
        return this.myExternalName.compareTo(key.myExternalName);
    }

    public static @NotNull TextAttributesKey createTextAttributesKey(@NonNls @NotNull String externalName) {
        return find(externalName);
    }

    public void writeExternal(Element element) {
        JDOMExternalizerUtil.writeField(element, "myExternalName", this.myExternalName);
        if (this.myDefaultAttributes != null) {
            Element option = JDOMExternalizerUtil.writeOption(element, "myDefaultAttributes");
            this.myDefaultAttributes.writeExternal(option);
        }

    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            TextAttributesKey that = (TextAttributesKey)o;
            return this.myExternalName.equals(that.myExternalName);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.myExternalName.hashCode();
    }

    public TextAttributes getDefaultAttributes() {
        TextAttributes defaultAttributes = this.myDefaultAttributes;
        if (defaultAttributes == null) {
            TextAttributeKeyDefaultsProvider provider = (TextAttributeKeyDefaultsProvider)ourDefaultsProvider.getValue();
            if (provider != null) {
                Set<String> called = (Set)CALLED_RECURSIVELY.get();
                if (!called.add(this.myExternalName)) {
                    return null;
                }

                TextAttributes var4;
                try {
                    var4 = (TextAttributes)ObjectUtils.notNull(provider.getDefaultAttributes(this), NULL_ATTRIBUTES);
                } finally {
                    called.remove(this.myExternalName);
                }

                return var4;
            }
        }

        return defaultAttributes;
    }

    /** @deprecated */
    @Deprecated
    public static @NotNull TextAttributesKey createTextAttributesKey(@NonNls @NotNull String externalName, TextAttributes defaultAttributes) {
        return getOrCreate(externalName, defaultAttributes, (TextAttributesKey)null);
    }

    public static @NotNull TextAttributesKey createTextAttributesKey(@NonNls @NotNull String externalName, TextAttributesKey fallbackAttributeKey) {
        return getOrCreate(externalName, (TextAttributes)null, fallbackAttributeKey);
    }

    private static @NotNull TextAttributesKey getOrCreate(@NotNull @NonNls String externalName, TextAttributes defaultAttributes, TextAttributesKey fallbackAttributeKey) {
        TextAttributesKey existing = (TextAttributesKey)ourRegistry.get(externalName);
        return existing == null || defaultAttributes != null && !Comparing.equal(existing.myDefaultAttributes, defaultAttributes) || fallbackAttributeKey != null && !Comparing.equal(existing.myFallbackAttributeKey, fallbackAttributeKey) ? (TextAttributesKey)ourRegistry.compute(externalName, (oldName, oldKey) -> mergeKeys(oldName, oldKey, defaultAttributes, fallbackAttributeKey)) : existing;
    }

    private static @NotNull TextAttributesKey mergeKeys(@NonNls @NotNull String externalName, @Nullable TextAttributesKey oldKey, TextAttributes defaultAttributes, TextAttributesKey fallbackAttributeKey) {
        if (oldKey == null) {
            return new TextAttributesKey(externalName, defaultAttributes, fallbackAttributeKey);
        } else {
            if (oldKey.myFallbackAttributeKey != null && !oldKey.myFallbackAttributeKey.equals(fallbackAttributeKey)) {
                LOG.error(new IllegalStateException("TextAttributeKey(name:'" + externalName + "', fallbackAttributeKey:'" + fallbackAttributeKey + "')  was already registered with the other fallback attribute key: " + oldKey.myFallbackAttributeKey));
            }

            if (oldKey.myDefaultAttributes != null && !oldKey.myDefaultAttributes.equals(defaultAttributes)) {
                LOG.error(new IllegalStateException("TextAttributeKey(name:'" + externalName + "', defaultAttributes:'" + defaultAttributes + "')  was already registered with the other defaultAttributes: " + oldKey.myDefaultAttributes));
            }

            TextAttributes newDefaults = (TextAttributes)ObjectUtils.chooseNotNull(defaultAttributes, oldKey.myDefaultAttributes);
            TextAttributesKey newFallback = (TextAttributesKey)ObjectUtils.chooseNotNull(fallbackAttributeKey, oldKey.myFallbackAttributeKey);
            return new TextAttributesKey(externalName, newDefaults, newFallback);
        }
    }

    public static @NotNull TextAttributesKey createTempTextAttributesKey(@NonNls @NotNull String externalName, TextAttributes defaultAttributes) {
        return createTextAttributesKey("TEMP::" + externalName, defaultAttributes);
    }

    public @Nullable TextAttributesKey getFallbackAttributeKey() {
        return this.myFallbackAttributeKey;
    }

    /** @deprecated */
    @Deprecated
    @ScheduledForRemoval
    public void setFallbackAttributeKey(@Nullable TextAttributesKey fallbackAttributeKey) {
    }

    @TestOnly
    static void removeTextAttributesKey(@NonNls @NotNull String externalName) {
        ourRegistry.remove(externalName);
    }

    public static boolean isTemp(@NotNull TextAttributesKey key) {
        return key.getExternalName().startsWith("TEMP::");
    }

    @Experimental
    @Internal
    public static @NotNull List<TextAttributesKey> getAllKeys() {
        return new ArrayList(ourRegistry.values());
    }

    @FunctionalInterface
    public interface TextAttributeKeyDefaultsProvider {
        @Nullable TextAttributes getDefaultAttributes(@NotNull TextAttributesKey var1);
    }
}
