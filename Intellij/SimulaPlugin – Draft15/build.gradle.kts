plugins {
    id("java")
//    id("org.jetbrains.kotlin.jvm") version "2.1.0"
//    id("org.jetbrains.intellij.platform") version "2.7.1"

    id("org.jetbrains.kotlin.jvm") version "2.3.0"
    id("org.jetbrains.intellij.platform") version "2.10.5"
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/gen")
        }
    }
}

// Configure IntelliJ Platform Gradle Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    intellijPlatform {
        create("IC", "2025.1.4.1")
        testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)

        // Add necessary plugin dependencies for compilation here, example:
        // bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.java")
    }
}

intellijPlatform {
    pluginConfiguration {
        ideaVersion {
            sinceBuild = "251"
        }

        changeNotes = """
            Initial version
        """.trimIndent()
    }
}

// MYH tasks {
//    // Set the JVM compatibility versions
//    withType<JavaCompile> {
//        sourceCompatibility = "21"
//        targetCompatibility = "21"
//    }
//}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}
kotlin {
    compilerOptions {
// MYH        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21)

//  This error occurs because your compileJava task is targeting JVM 25,
        //  while your compileKotlin task is targeting JVM 24.
        //  Kotlin currently does not support JVM 25 as a target,
        //  leading the compiler to fall back to JVM 24 and creating an inconsistency.
//  To fix this, you must align both tasks to the same supported JVM version
        //  (e.g., JVM 21 or 24) using one of the following methods:
//      jvmToolchain(25)
//        jvmToolchain(24)
        jvmToolchain(21)
    }
}

tasks.withType<Jar> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

// If the error occurs during resource processing
tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}
