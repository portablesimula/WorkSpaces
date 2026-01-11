// Her er Kotlin-koden oversatt til Java.
// Merk at Kotlin-spesifikke konsepter som companion object, default parameters og extension
// functions i Java må implementeres via henholdsvis static felt/metoder og metode-overloading.

// Copyright 2000-2024 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.ui;

import com.intellij.openapi.util.DummyIcon;
import com.intellij.openapi.util.Iconable;
import com.intellij.openapi.util.ScalableIcon;
import com.intellij.ui.icons.IconReplacer;
import com.intellij.ui.icons.RowIcon;
import kotlin.Pair;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;

import javax.swing.*;
import java.awt.*;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.Supplier;

public interface IconManager {
    AtomicBoolean isActivated = new AtomicBoolean();
    
    // Volatile for trådsikker singleton-tilgang i Java
    class Holder {
        private static volatile IconManager instance = null;
    }

    @NotNull
    static IconManager getInstance() {
        IconManager inst = Holder.instance;
        return inst != null ? inst : DummyIconManager.INSTANCE;
    }

    static void activate(@Nullable IconManager manager) {
        if (!isActivated.compareAndSet(false, true)) {
            return;
        }

        if (manager == null) {
            try {
                Class<?> implClass = IconManager.class.getClassLoader().loadClass("com.intellij.ui.icons.CoreIconManager");
                Holder.instance = (IconManager) MethodHandles.lookup()
                        .findConstructor(implClass, MethodType.methodType(void.class))
                        .invoke();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        } else {
            Holder.instance = manager;
        }
    }

    @TestOnly
    static void deactivate() {
        if (isActivated.compareAndSet(true, false)) {
            Holder.instance = null;
        }
    }

    @Internal
    Icon getPlatformIcon(@NotNull PlatformIcons id);

    @Deprecated
    Icon getIcon(@NotNull String path, @NotNull Class<?> aClass);

    Icon getIcon(@NotNull String path, @NotNull ClassLoader classLoader);

    @Internal
    Icon loadRasterizedIcon(@NotNull String path, @NotNull ClassLoader classLoader, int cacheKey, int flags);

    @Internal
    Icon loadRasterizedIcon(@NotNull String path, @Nullable String expUIPath, @NotNull ClassLoader classLoader, int cacheKey, int flags);

    default Icon createEmptyIcon(@NotNull Icon icon) { return icon; }

    default Icon createOffsetIcon(@NotNull Icon icon) { return icon; }

    Icon createLayered(@NotNull Icon... icons);

    default Icon colorize(@NotNull Graphics2D g, @NotNull Icon source, @NotNull Color color) { return source; }

    <T> Icon createDeferredIcon(@Nullable Icon base, T param, @NotNull Function<T, Icon> iconProducer);

    RowIcon createLayeredIcon(@NotNull Iconable instance, @NotNull Icon icon, int flags);

    default RowIcon createRowIcon(int iconCount) {
        return createRowIcon(iconCount, RowIcon.Alignment.TOP);
    }

    RowIcon createRowIcon(int iconCount, @NotNull RowIcon.Alignment alignment);

    RowIcon createRowIcon(@NotNull Icon... icons);

    void registerIconLayer(int flagMask, @NotNull Icon icon);

    Icon tooltipOnlyIfComposite(@NotNull Icon icon);

    default Icon withIconBadge(@NotNull Icon icon, @NotNull Paint color) { return icon; }

    @ApiStatus.Experimental
    default Icon colorizedIcon(@NotNull Icon baseIcon, @NotNull Supplier<Color> colorProvider) { return baseIcon; }

    @Internal
    default long hashClass(@NotNull Class<?> aClass) { return (long) aClass.hashCode(); }

    default Pair<String, String> getPluginAndModuleId(@NotNull ClassLoader classLoader) {
        return new Pair<>("com.intellij", null);
    }

    default ClassLoader getClassLoader(@NotNull String pluginId, @Nullable String moduleId) {
        return IconManager.class.getClassLoader();
    }

    @Internal
    default ClassLoader getClassLoaderByClassName(@NotNull String className) {
        return IconManager.class.getClassLoader();
    }
}

// Dummy-implementasjoner (tilsvarer "private object" og "private class" i Kotlin-filen)
class DummyIconManager implements IconManager {
    static final DummyIconManager INSTANCE = new DummyIconManager();

    @Override
    public Icon getPlatformIcon(PlatformIcons id) {
        return new DummyIconImpl(id.getTestId() != null ? id.getTestId() : id.name());
    }

    @Override
    @SuppressWarnings("deprecation")
    public Icon getIcon(String path, Class<?> aClass) {
        return new DummyIconImpl(path);
    }

    @Override
    public Icon getIcon(String path, ClassLoader classLoader) {
        return new DummyIconImpl(path);
    }

    @Override
    public Icon loadRasterizedIcon(String path, ClassLoader classLoader, int cacheKey, int flags) {
        return new DummyIconImpl(path);
    }

    @Override
    public Icon loadRasterizedIcon(String path, String expUIPath, ClassLoader classLoader, int cacheKey, int flags) {
        return new DummyIconImpl(path, expUIPath);
    }

    @Override
    public RowIcon createLayeredIcon(Iconable instance, Icon icon, int flags) {
        Icon[] icons = new Icon[2];
        icons[0] = icon;
        return new DummyRowIcon(icons);
    }

    @Override
    public void registerIconLayer(int flagMask, Icon icon) {}

    @Override
    public Icon tooltipOnlyIfComposite(Icon icon) { return icon; }

    @Override
    public <T> Icon createDeferredIcon(Icon base, T param, Function<T, Icon> iconProducer) {
        Icon icon = iconProducer.apply(param);
        return icon != null ? icon : base;
    }

    @Override
    public RowIcon createRowIcon(int iconCount, RowIcon.Alignment alignment) {
        return new DummyRowIcon(iconCount);
    }

    @Override
    public Icon createLayered(Icon... icons) {
        return new DummyRowIcon(icons);
    }

    @Override
    public RowIcon createRowIcon(Icon... icons) {
        return new DummyRowIcon(icons);
    }
}

class DummyRowIcon extends DummyIconImpl implements RowIcon {
    private Icon[] icons;

    DummyRowIcon(int iconCount) {
        super("<DummyRowIcon>");
        this.icons = new Icon[iconCount];
    }

    DummyRowIcon(Icon[] icons) {
        super("<DummyRowIcon>");
        this.icons = icons;
    }

    @Override
    public int getIconCount() { return icons == null ? 0 : icons.length; }

    @Override
    public Icon getIcon(int index) { return icons[index]; }

    @Override
    public void setIcon(Icon icon, int i) {
        if (icons == null) icons = new Icon[4];
        icons[i] = icon;
    }

    @Override
    public Icon getDarkIcon(boolean isDark) { return this; }

    @Override
    public List<Icon> getAllIcons() {
        List<Icon> list = new ArrayList<>();
        for (Icon icon : icons) {
            if (icon != null) list.add(icon);
        }
        return list;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DummyRowIcon)) return false;
        return Arrays.equals(icons, ((DummyRowIcon) o).icons);
    }

    @Override
    public int hashCode() {
        return (icons != null && icons.length > 0 && icons[0] != null) ? icons[0].hashCode() : 0;
    }

    @Override
    public String toString() {
        return "RowIcon(icons=" + (icons != null ? Arrays.asList(icons) : "null") + ")";
    }

    @Override
    public Icon replaceBy(IconReplacer replacer) { return this; }
}

class DummyIconImpl implements ScalableIcon, DummyIcon {
    private final String originalPath;
    private final String expUIPath;

    DummyIconImpl(String path) {
        this(path, null);
    }

    DummyIconImpl(String path, String expUIPath) {
        this.originalPath = path;
        this.expUIPath = expUIPath;
    }

    @Override public String getOriginalPath() { return originalPath; }
    @Override public String getExpUIPath() { return expUIPath; }

    @Override public void paintIcon(Component c, Graphics g, int x, int y) {}
    @Override public int getIconWidth() { return 16; }
    @Override public int getIconHeight() { return 16; }

    @Override
    public int hashCode() { return originalPath.hashCode(); }

    @Override
    public boolean equals(Object other) {
        return this == other || (other instanceof DummyIconImpl && ((DummyIconImpl) other).originalPath.equals(originalPath));
    }

    @Override public String toString() { return originalPath; }
    @Override public float getScale() { return 1.0f; }
    @Override public Icon scale(float scaleFactor) { return this; }
}
