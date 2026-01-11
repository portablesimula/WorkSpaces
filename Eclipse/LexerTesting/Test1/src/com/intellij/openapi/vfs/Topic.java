//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.intellij.openapi.vfs;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@NonExtendable
public class Topic<L> {
    private final String myDisplayName;
    private final Class<L> myListenerClass;
    private final BroadcastDirection myBroadcastDirection;
    private final boolean myImmediateDelivery;

    public Topic(@NonNls @NotNull String name, @NotNull Class<L> listenerClass) {
        this(name, listenerClass, Topic.BroadcastDirection.TO_CHILDREN);
    }

    public Topic(@NotNull Class<L> listenerClass) {
        this(listenerClass.getSimpleName(), listenerClass, Topic.BroadcastDirection.TO_CHILDREN);
    }

    public Topic(@NotNull Class<L> listenerClass, @NotNull BroadcastDirection broadcastDirection) {
        this(listenerClass.getSimpleName(), listenerClass, broadcastDirection);
    }

    public Topic(@NotNull Class<L> listenerClass, @NotNull BroadcastDirection broadcastDirection, boolean immediateDelivery) {
        this.myDisplayName = listenerClass.getSimpleName();
        this.myListenerClass = listenerClass;
        this.myBroadcastDirection = broadcastDirection;
        this.myImmediateDelivery = immediateDelivery;
    }

    public Topic(@NonNls @NotNull String name, @NotNull Class<L> listenerClass, @NotNull BroadcastDirection broadcastDirection) {
        if (broadcastDirection == null) {
            $$$reportNull$$$0(9);
        }

        super();
        this.myDisplayName = name;
        this.myListenerClass = listenerClass;
        this.myBroadcastDirection = broadcastDirection;
        this.myImmediateDelivery = false;
    }

    public @NonNls @NotNull String getDisplayName() {
        return this.myDisplayName;
    }

    @Internal
    public @NotNull Class<L> getListenerClass() {
        return this.myListenerClass;
    }

    public String toString() {
        return "Topic('" + this.myDisplayName + "'" + (this.myBroadcastDirection == Topic.BroadcastDirection.NONE ? "" : ", direction=" + this.myBroadcastDirection) + (this.myImmediateDelivery ? ", immediateDelivery" : "") + ", listenerClass=" + this.myListenerClass + ')';
    }

    public static <L> @NotNull Topic<L> create(@NonNls @NotNull String displayName, @NotNull Class<L> listenerClass) {
        return new Topic<L>(displayName, listenerClass);
    }

    public static <L> @NotNull Topic<L> create(@NonNls @NotNull String displayName, @NotNull Class<L> listenerClass, @NotNull BroadcastDirection direction) {
        if (direction == null) {
            $$$reportNull$$$0(16);
        }

        return new Topic<L>(displayName, listenerClass, direction);
    }

    @Internal
    public @NotNull BroadcastDirection getBroadcastDirection() {
        return this.myBroadcastDirection;
    }

    @Internal
    @Experimental
    public boolean isImmediateDelivery() {
        return this.myImmediateDelivery;
    }

    public static enum BroadcastDirection {
        TO_CHILDREN,
        TO_DIRECT_CHILDREN,
        NONE,
        TO_PARENT;
    }

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.FIELD})
    public @interface AppLevel {
    }

    @Retention(RetentionPolicy.CLASS)
    @Target({ElementType.FIELD})
    public @interface ProjectLevel {
    }
}
