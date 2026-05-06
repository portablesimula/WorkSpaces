package simula.psi.intellij;

//Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

//package com.intellij.util.diff;
//
//import com.intellij.openapi.util.Ref;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

public interface FlyweightCapableTreeStructure<T> {
T getRoot();

T getParent( T node);

int getChildren( T parent,  Ref<T[]> into);

void disposeChildren(T[] nodes, int count);


CharSequence toString( T node);

int getStartOffset( T node);
int getEndOffset( T node);
}