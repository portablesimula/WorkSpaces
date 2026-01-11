
// Her er koden oversatt til Java:
	
//Copyright 2000-2026 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.util.containers;

import java.util.Arrays;

/**
* <p>A simple object pool which instantiates objects on-demand and keeps up to the given number of objects for later reuse.</p>
* <p><b>Note:</b> the class is not thread-safe; use {@link Sync synchronized version} for concurrent access.</p>
*
* @author max, Boris.Krylov
*/
public class LimitedPool<T> {
 
 @FunctionalInterface
 public interface ObjectFactory<T> {
     T create();
     default void cleanup(T t) {}
 }

 private final int myMaxCapacity;
 private final ObjectFactory<T> myFactory;
 private Object[] myStorage = new Object[0];
 private int myIndex = 0;

 public LimitedPool(int maxCapacity, ObjectFactory<T> factory) {
     this.myMaxCapacity = maxCapacity;
     this.myFactory = factory;
 }

 public T alloc() {
     if (myIndex == 0) {
         return myFactory.create();
     }

     int i = --myIndex;
     @SuppressWarnings("unchecked")
     T result = (T) myStorage[i];
     myStorage[i] = null;
     return result;
 }

 public void recycle(T t) {
     myFactory.cleanup(t);
     if (myIndex >= myMaxCapacity) {
         return;
     }

     ensureCapacity();
     myStorage[myIndex++] = t;
 }

 private void ensureCapacity() {
     if (myStorage.length <= myIndex) {
         int newCapacity = (myStorage.length * 3) / 2;
         
         if (newCapacity < 10) {
             myStorage = Arrays.copyOf(myStorage, 10);
         } else if (newCapacity <= myMaxCapacity) {
             myStorage = Arrays.copyOf(myStorage, newCapacity);
         } else {
             myStorage = Arrays.copyOf(myStorage, myMaxCapacity);
         }
     }
 }
}
