package simula.psi.intellij;

//Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
//package com.intellij.openapi.util;
//
//import com.intellij.ReviseWhenPortedToJDK;
//import com.intellij.openapi.util.userData.ExternalUserDataStorage;
//import com.intellij.util.keyFMap.KeyFMap;
//import com.intellij.util.xmlb.annotations.Transient;
//import org.jetbrains.annotations.*;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

@SuppressWarnings("serial")
public class UserDataHolderBase extends AtomicReference<KeyFMap> implements UserDataHolderEx {
	private static final Key<KeyFMap> COPYABLE_USER_MAP_KEY = Key.create("COPYABLE_USER_MAP_KEY");

	private static Supplier<ExternalUserDataStorage> ourExternalUserDataStorage = null;

	public static void setExternalUserDataStorage(Supplier<ExternalUserDataStorage> supplier) {
		ourExternalUserDataStorage = supplier;
	}

	private static ExternalUserDataStorage externalStorage() {
		Supplier<ExternalUserDataStorage> supplier = ourExternalUserDataStorage;
		return supplier == null ? null : supplier.get();
	}

	public UserDataHolderBase() {
		set(KeyFMap.EMPTY_MAP);
	}

	@Override
	protected Object clone() {
		try {
			UserDataHolderBase clone = (UserDataHolderBase) super.clone();
			clone.setUserMap(KeyFMap.EMPTY_MAP);
			copyCopyableDataTo(clone);
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

	public String getUserDataString() {
		final KeyFMap userMap = getUserMap();
		final KeyFMap copyableMap = getUserData(COPYABLE_USER_MAP_KEY);
		return userMap + (copyableMap == null ? "" : copyableMap.toString());
	}

	public void copyUserDataTo(UserDataHolderBase other) {
		other.setUserMap(getUserMap());
	}

	@Override
	public <T> T getUserData(Key<T> key) {
		ExternalUserDataStorage external = externalStorage();
		if (external != null) {
			return external.getUserData(this, key);
		} else {
			T t = getUserMap().get(key);
//			if (t == null && key instanceof KeyWithDefaultValue) {
//				t = putUserDataIfAbsent(key, ((KeyWithDefaultValue<T>) key).getDefaultValue());
//			}
			return t;
		}
	}

	protected KeyFMap getUserMap() {
		ExternalUserDataStorage external = externalStorage();
		if (external != null) {
			return external.getUserMap(this);
		} else {
			return get();
		}
	}

	@Override
	public <T> void putUserData(Key<T> key, T value) {
		ExternalUserDataStorage external = externalStorage();
		if (external != null) {
			external.putUserData(this, key, value);
		} else {
			while (true) {
				KeyFMap map = getUserMap();
				KeyFMap newMap = value == null ? map.minus(key) : map.plus(key, value);
				if (newMap == map || changeUserMap(map, newMap)) {
					break;
				}
			}
		}
	}

	protected boolean changeUserMap(KeyFMap oldMap, KeyFMap newMap) {
		return compareAndSet(oldMap, newMap);
	}

	public <T> T getCopyableUserData(Key<T> key) {
		KeyFMap map = getUserData(COPYABLE_USER_MAP_KEY);
		return map == null ? null : map.get(key);
	}

	public <T> void putCopyableUserData(Key<T> key, T value) {
		ExternalUserDataStorage external = externalStorage();
		if (external != null) {
			while (true) {
				KeyFMap oldCopyableMap = getUserData(COPYABLE_USER_MAP_KEY);
				KeyFMap newCopyableMap = oldCopyableMap;
				if (oldCopyableMap == null) {
					if (value == null) {
						// nothing
					} else {
						newCopyableMap = KeyFMap.EMPTY_MAP.plus(key, value);
					}
				} else {
					if (value == null) {
						newCopyableMap = oldCopyableMap.minus(key);
					} else {
						newCopyableMap = oldCopyableMap.plus(key, value);
					}
				}
				if (oldCopyableMap == newCopyableMap || external.compareAndPutUserData(this, COPYABLE_USER_MAP_KEY,
						oldCopyableMap, newCopyableMap)) {
					break;
				}
			}
		} else {
			while (true) {
				KeyFMap map = getUserMap();
				KeyFMap copyableMap = map.get(COPYABLE_USER_MAP_KEY);
				if (copyableMap == null) {
					copyableMap = KeyFMap.EMPTY_MAP;
				}
				KeyFMap newCopyableMap = value == null ? copyableMap.minus(key) : copyableMap.plus(key, value);
				KeyFMap newMap = newCopyableMap.isEmpty() ? map.minus(COPYABLE_USER_MAP_KEY)
						: map.plus(COPYABLE_USER_MAP_KEY, newCopyableMap);
				if (newMap == map || changeUserMap(map, newMap)) {
					return;
				}
			}
		}
	}

	@Override
	public <T> boolean replace(Key<T> key, T oldValue, T newValue) {
		ExternalUserDataStorage external = externalStorage();
		if (external != null) {
			return external.compareAndPutUserData(this, key, oldValue, newValue);
		} else {
			while (true) {
				KeyFMap map = getUserMap();
				if (map.get(key) != oldValue) {
					return false;
				}
				KeyFMap newMap = newValue == null ? map.minus(key) : map.plus(key, newValue);
				if (newMap == map || changeUserMap(map, newMap)) {
					return true;
				}
			}
		}
	}

	@Override
	public <T> T putUserDataIfAbsent(final Key<T> key, final T value) {
		ExternalUserDataStorage external = externalStorage();
		if (external != null) {
			return external.putUserDataIfAbsent(this, key, value);
		} else {
			while (true) {
				KeyFMap map = getUserMap();
				T oldValue = map.get(key);
				if (oldValue != null) {
					return oldValue;
				}
				KeyFMap newMap = map.plus(key, value);
				if (newMap == map || changeUserMap(map, newMap)) {
					return value;
				}
			}
		}
	}

	public void copyCopyableDataTo(UserDataHolderBase clone) {
		clone.putUserData(COPYABLE_USER_MAP_KEY, getUserData(COPYABLE_USER_MAP_KEY));
	}

	public boolean isCopyableDataEqual(UserDataHolderBase other) {
		return Objects.equals(getUserData(COPYABLE_USER_MAP_KEY), other.getUserData(COPYABLE_USER_MAP_KEY));
	}

	protected void clearUserData() {
		setUserMap(KeyFMap.EMPTY_MAP);
	}

	protected void setUserMap(KeyFMap map) {
		ExternalUserDataStorage external = externalStorage();
		if (external != null) {
			external.setUserMap(this, map);
		} else {
			set(map);
		}
	}

	public boolean isUserDataEmpty() {
		return getUserMap().isEmpty();
	}
}