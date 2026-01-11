
// Her er RegistryValue oversatt fra Kotlin til Java.
//Jeg har tatt hensyn til JetBrains-spesifikke annotasjoner og
// brukt standard Java-konvensjoner for felt, gettere og settere.

//Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.openapi.util.registry;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.util.Disposer;
import com.intellij.openapi.util.NlsSafe;
import com.intellij.ui.ColorHexUtil;
import com.intellij.util.containers.ContainerUtil;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Objects;

public class RegistryValue {
 private static final Logger LOG = Logger.getInstance(RegistryValue.class);

 private final Registry registry;
 private final String key;
 private final RegistryKeyDescriptor keyDescriptor;
 private final List<RegistryValueListener> listeners = ContainerUtil.createLockFreeCopyOnWriteList();

 private boolean isChangedSinceAppStart = false;

 private String stringCachedValue = null;
 private Integer intCachedValue = null;
 private double doubleCachedValue = Double.NaN;
 private Boolean booleanCachedValue = null;

 @Internal
 public RegistryValue(@NotNull Registry registry, @NotNull @NonNls String key, @Nullable RegistryKeyDescriptor keyDescriptor) {
     this.registry = registry;
     this.key = key;
     this.keyDescriptor = keyDescriptor;
 }

 @NotNull
 public String getKey() {
     return key;
 }

 public boolean isChangedSinceAppStart() {
     return isChangedSinceAppStart;
 }

 @ApiStatus.Experimental
 @Nullable
 public RegistryValueSource getSource() {
     ValueWithSource v = registry.getStoredProperties().get(key);
     return v != null ? v.getSource() : null;
 }

 @NotNull
 @NlsSafe
 public String asString() {
     String result = stringCachedValue;
     if (result == null) {
         result = resolveRequiredValue(key);
         stringCachedValue = result;
     }
     return result;
 }

 public boolean asBoolean() {
     Boolean result = booleanCachedValue;
     if (result == null) {
         result = Boolean.parseBoolean(resolveRequiredValue(key));
         booleanCachedValue = result;
     }
     return result;
 }

 public int asInteger() {
     Integer result = intCachedValue;
     if (result == null) {
         try {
             result = Integer.parseInt(resolveRequiredValue(key));
         } catch (NumberFormatException e) {
             result = Integer.parseInt(registry.getBundleValue(key, keyDescriptor));
         }
         intCachedValue = result;
     }
     return result;
 }

 public boolean isMultiValue() {
     return getSelectedOption() != null;
 }

 @NotNull
 public List<String> asOptions() {
     String value = registry.getBundleValue(key, keyDescriptor);
     if (value.startsWith("[") && value.endsWith("]")) {
         String content = value.substring(1, value.length() - 1);
         return List.of(content.split("\\|", -1));
     }
     return Collections.emptyList();
 }

 @Nullable
 @NlsSafe
 public String getSelectedOption() {
     String value = asString();
     int length = value.length();
     if (length < 3 || value.charAt(0) != '[' || value.charAt(length - 1) != ']') return null;

     int pos = 1;
     while (pos < length) {
         int end = value.indexOf('|', pos);
         if (end == -1) {
             end = length - 1;
         }
         if (end > 0 && value.charAt(end - 1) == '*') {
             return value.substring(pos, end - 1);
         }
         pos = end + 1;
     }
     return null;
 }

 public void setSelectedOption(@Nullable String selected) {
     setSelectedOption(selected, RegistryValueSource.SYSTEM);
 }

 @ApiStatus.Experimental
 public void setSelectedOption(@Nullable String selected, @NotNull RegistryValueSource source) {
     List<String> options = new java.util.ArrayList<>(asOptions());
     for (int i = 0; i < options.size(); i++) {
         String option = options.get(i);
         String v = option.endsWith("*") ? option.substring(0, option.length() - 1) : option;
         if (Objects.equals(v, selected)) {
             options.set(i, v + "*");
         } else {
             options.set(i, v);
         }
     }
     setValue("[" + String.join("|", options) + "]", source);
 }

 public boolean isOptionEnabled(@NotNull String option) {
     return Objects.equals(getSelectedOption(), option);
 }

 public double asDouble() {
     if (Double.isNaN(doubleCachedValue)) {
         doubleCachedValue = computeDouble();
     }
     return doubleCachedValue;
 }

 private double computeDouble() {
     String value = resolveNotRequiredValue(key);
     if (value != null) {
         try { return Double.parseDouble(value); } catch (NumberFormatException ignored) {}
     }
     if (keyDescriptor != null && keyDescriptor.getDefaultValue() != null) {
         try { return Double.parseDouble(keyDescriptor.getDefaultValue()); } catch (NumberFormatException ignored) {}
     }
     String bundleValue = registry.getBundleValueOrNull(key);
     if (bundleValue != null) {
         try { return Double.parseDouble(bundleValue); } catch (NumberFormatException ignored) {}
     }
     return 0.0;
 }

 @Nullable
 public Color asColor(@Nullable Color defaultValue) {
     String s = getAsValue(key);
     if (s == null) return defaultValue;

     Color color = ColorHexUtil.fromHex(s, null);
     if (color != null && (key.endsWith(".color") || key.endsWith(".color.dark") || key.endsWith(".color.light"))) {
         return color;
     }

     String[] rgb = s.split(",");
     if (rgb.length == 3) {
         try {
             return new Color(Integer.parseInt(rgb[0].trim()), Integer.parseInt(rgb[1].trim()), Integer.parseInt(rgb[2].trim()));
         } catch (Exception ignored) {}
     }
     return defaultValue;
 }

 @NotNull
 @NlsSafe
 public String getDescription() {
     if (keyDescriptor != null && keyDescriptor.getDescription() != null) {
         return keyDescriptor.getDescription();
     }
     String desc = resolveNotRequiredValue(key + ".description");
     return desc != null ? desc : "";
 }

 public boolean isRestartRequired() {
     if (keyDescriptor == null) {
         return Boolean.parseBoolean(resolveNotRequiredValue(key + ".restartRequired"));
     }
     return keyDescriptor.isRestartRequired();
 }

 public boolean isChangedFromDefault() {
     String current = stringCachedValue != null ? stringCachedValue : resolveNotRequiredValue(key);
     return !Objects.equals(current, registry.getBundleValueOrNull(key));
 }

 @Nullable
 public String getPluginId() {
     return keyDescriptor != null ? keyDescriptor.getPluginId() : null;
 }

 @Nullable
 private String getAsValue(@NotNull @NonNls String key) {
     if (stringCachedValue == null) {
         stringCachedValue = resolveNotRequiredValue(key);
     }
     return (stringCachedValue != null && !stringCachedValue.isEmpty()) ? stringCachedValue : null;
 }

 @Internal
 @Nullable
 public String resolveNotRequiredValue(@NotNull @NonNls String key) {
     ValueWithSource stored = registry.getStoredProperties().get(key);
     if (stored != null) return stored.getValue();

     String sysProp = System.getProperty(key);
     if (sysProp != null) return sysProp;

     checkIsLoaded(key);
     return registry.getBundleValueOrNull(key);
 }

 @NotNull
 private String resolveRequiredValue(@NotNull @NonNls String key) throws MissingResourceException {
     ValueWithSource stored = registry.getStoredProperties().get(key);
     if (stored != null) return stored.getValue();

     String sysProp = System.getProperty(key);
     if (sysProp != null) return sysProp;

     checkIsLoaded(key);
     return registry.getBundleValue(key, keyDescriptor);
 }

 private void checkIsLoaded(@NotNull String key) {
     if (registry.isLoaded()) return;

     String message = "Attempt to load key '" + key + "' for not yet loaded registry";
     if (Disposer.isDebugMode()) {
         LOG.error(message + ". Use system properties instead of registry values to configure behaviour at early startup stages.");
     } else {
         LOG.warn(message);
     }
 }

 public void setValue(boolean value) {
     setValue(String.valueOf(value));
 }

 public void setValue(int value) {
     setValue(String.valueOf(value));
 }

 public void setValue(@NotNull String value) {
     setValue(value, RegistryValueSource.SYSTEM);
 }

 @ApiStatus.Experimental
 public void setValue(@NotNull String value, @NotNull RegistryValueSource source) {
     RegistryValueListener globalListener = registry.getValueChangeListener();
     globalListener.beforeValueChanged(this);
     for (RegistryValueListener listener : listeners) {
         listener.beforeValueChanged(this);
     }

     resetCache();
     registry.getStoredProperties().put(key, new ValueWithSource(value, source));
     LOG.info("Registry value '" + key + "' has changed to '" + value + "' by " + source.name());

     if (LOG.isDebugEnabled()) {
         LOG.debug("Registry change stacktrace", new Throwable());
     }

     globalListener.afterValueChanged(this);
     for (RegistryValueListener listener : listeners) {
         listener.afterValueChanged(this);
     }

     if (!isRestartRequired() && Objects.equals(resolveNotRequiredValue(key), registry.getBundleValueOrNull(key))) {
         registry.getStoredProperties().remove(key);
     }

     isChangedSinceAppStart = true;
 }

 private void resetCache() {
     stringCachedValue = null;
     intCachedValue = null;
     doubleCachedValue = Double.NaN;
     booleanCachedValue = null;
 }

 public void setValue(boolean value, @NotNull Disposable parentDisposable) {
     boolean prev = asBoolean();
     setValue(value);
     Disposer.register(parentDisposable, () -> setValue(prev));
 }

 public void setValue(int value, @NotNull Disposable parentDisposable) {
     int prev = asInteger();
     setValue(value);
     Disposer.register(parentDisposable, () -> setValue(prev));
 }

 @TestOnly
 public void setValue(@NotNull String value, @NotNull Disposable parentDisposable) {
     String prev = stringCachedValue != null ? stringCachedValue : resolveRequiredValue(key);
     setValue(value);
     Disposer.register(parentDisposable, () -> setValue(prev));
 }
}
