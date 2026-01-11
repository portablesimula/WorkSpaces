package com.intellij.openapi.util;

import com.intellij.util.keyFMap.KeyFMap;
import com.intellij.util.xmlb.annotations.Transient;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.UnknownNullability;
import org.jetbrains.annotations.ApiStatus.Experimental;

@Transient
public class UserDataHolderBase extends AtomicReference<KeyFMap> implements UserDataHolderEx {
    private static final Key<KeyFMap> COPYABLE_USER_MAP_KEY = Key.create("COPYABLE_USER_MAP_KEY");

    public UserDataHolderBase() {
        this.set(KeyFMap.EMPTY_MAP);
    }

    protected Object clone() {
        try {
            UserDataHolderBase clone = (UserDataHolderBase)super.clone();
            clone.setUserMap(KeyFMap.EMPTY_MAP);
            this.copyCopyableDataTo(clone);
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    @TestOnly
    public String getUserDataString() {
        KeyFMap userMap = this.getUserMap();
        KeyFMap copyableMap = (KeyFMap)this.getUserData(COPYABLE_USER_MAP_KEY);
        return userMap + (copyableMap == null ? "" : copyableMap.toString());
    }

    public void copyUserDataTo(@NotNull UserDataHolderBase other) {
        other.setUserMap(this.getUserMap());
    }

    public <T> T getUserData(@NotNull Key<T> key) {
        T t = (T)this.getUserMap().get(key);
    	System.out.println("UserDataHolderBase.getUserData: t="+t);
        if (t == null && key instanceof KeyWithDefaultValue) {
        	System.out.println("UserDataHolderBase.getUserData: CODE REMOVED");
//            t = (T)this.putUserDataIfAbsent(key, ((KeyWithDefaultValue)key).getDefaultValue());
        }

        return t;
    }

    protected @NotNull KeyFMap getUserMap() {
        return (KeyFMap)this.get();
    }

    public <T> void putUserData(@NotNull Key<T> key, @Nullable T value) {
        KeyFMap map;
        KeyFMap newMap;
        do {
            map = this.getUserMap();
            newMap = value == null ? map.minus(key) : map.plus(key, value);
        } while(newMap != map && !this.changeUserMap(map, newMap));

    }

    protected boolean changeUserMap(@NotNull KeyFMap oldMap, @NotNull KeyFMap newMap) {
        return this.compareAndSet(oldMap, newMap);
    }

    public <T> @UnknownNullability T getCopyableUserData(@NotNull Key<T> key) {
        KeyFMap map = (KeyFMap)this.getUserData(COPYABLE_USER_MAP_KEY);
        return (T)(map == null ? null : map.get(key));
    }

    public <T> void putCopyableUserData(@NotNull Key<T> key, T value) {
        KeyFMap map;
        KeyFMap newMap;
        do {
            map = this.getUserMap();
            KeyFMap copyableMap = (KeyFMap)map.get(COPYABLE_USER_MAP_KEY);
            if (copyableMap == null) {
                copyableMap = KeyFMap.EMPTY_MAP;
            }

            KeyFMap newCopyableMap = value == null ? copyableMap.minus(key) : copyableMap.plus(key, value);
            newMap = newCopyableMap.isEmpty() ? map.minus(COPYABLE_USER_MAP_KEY) : map.plus(COPYABLE_USER_MAP_KEY, newCopyableMap);
        } while(newMap != map && !this.changeUserMap(map, newMap));

    }

    public <T> boolean replace(@NotNull Key<T> key, @Nullable T oldValue, @Nullable T newValue) {
        KeyFMap map;
        KeyFMap newMap;
        do {
            map = this.getUserMap();
            if (map.get(key) != oldValue) {
                return false;
            }

            newMap = newValue == null ? map.minus(key) : map.plus(key, newValue);
        } while(newMap != map && !this.changeUserMap(map, newMap));

        return true;
    }

    public <T> @NotNull T putUserDataIfAbsent(@NotNull Key<T> key, @NotNull T value) {
        KeyFMap map;
        KeyFMap newMap;
        do {
            map = this.getUserMap();
            T oldValue = (T)map.get(key);
            if (oldValue != null) {
                return oldValue;
            }

            newMap = map.plus(key, value);
        } while(newMap != map && !this.changeUserMap(map, newMap));

        return value;
    }

    public void copyCopyableDataTo(@NotNull UserDataHolderBase clone) {
        clone.putUserData(COPYABLE_USER_MAP_KEY, (KeyFMap)this.getUserData(COPYABLE_USER_MAP_KEY));
    }

    @Experimental
    protected boolean isCopyableDataEqual(@NotNull UserDataHolderBase other) {
        return Objects.equals(this.getUserData(COPYABLE_USER_MAP_KEY), other.getUserData(COPYABLE_USER_MAP_KEY));
    }

    protected void clearUserData() {
        this.setUserMap(KeyFMap.EMPTY_MAP);
    }

    protected void setUserMap(@NotNull KeyFMap map) {
        this.set(map);
    }

    public boolean isUserDataEmpty() {
        return this.getUserMap().isEmpty();
    }
}
