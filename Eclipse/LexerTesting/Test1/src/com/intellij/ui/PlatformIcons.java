
// Her er Kotlin-koden oversatt til Java.
// Siden Kotlin-koden bruker en konstruktør med standardverdier (testId: String? = null),
// må Java-versjonen ha to konstruktører (eller én som håndterer null) for å bevare samme funksjonalitet. 

// Copyright 2000-2022 JetBrains s.r.o. and contributors. Use of this source code is governed by the Apache 2.0 license.
package com.intellij.ui;

import org.jetbrains.annotations.ApiStatus.Internal;
import org.jetbrains.annotations.Nullable;

@Internal
public enum PlatformIcons {
    Public,
    Private,
    Protected,
    Local,

    TodoDefault,
    TodoQuestion,
    TodoImportant,

    NodePlaceholder,
    WarningDialog,
    Copy,
    TestStateRun,
    Import,
    Export,
    Stub,

    Package,
    Folder,
    IdeaModule,

    TextFileType,
    ArchiveFileType,
    UnknownFileType,
    CustomFileType,
    JavaClassFileType("fileTypes/javaClass.svg"),
    JspFileType,
    JavaModule,
    JavaFileType("fileTypes/java.svg"),
    PropertiesFileType,

    Variable,
    Field,
    Class,
    AbstractClass,
    AnonymousClass,
    ExceptionClass,
    Enum,
    Aspect,
    Annotation,
    Function,
    Interface,
    Method,
    AbstractMethod("nodes/abstractMethod.svg"),
    AbstractException,
    MethodReference,
    Parameter,
    Property,
    Tag,
    Lambda,
    Record,
    ClassInitializer,
    Plugin,
    PpWeb,

    StaticMark,
    FinalMark,
    TestMark,
    JunitTestMark,
    RunnableMark;

    @Nullable
    public final String testId;

    PlatformIcons() {
        this(null);
    }

    PlatformIcons(@Nullable String testId) {
        this.testId = testId;
    }
}
