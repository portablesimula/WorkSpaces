import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.changelog")
    id("org.jetbrains.intellij.platform")
}

// Read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin.html
dependencies {
    testImplementation(libs.junit)

    // IntelliJ Platform Gradle Plugin Dependencies Extension - read more: https://plugins.jetbrains.com/docs/intellij/tools-intellij-platform-gradle-plugin-dependencies-extension.html
    intellijPlatform {
//        intellijIdea("2025.3.5")
        intellijIdea("2026.2")
        testFramework(TestFrameworkType.Platform)

        // Add plugin dependencies for compilation here:
        bundledPlugin("com.intellij.java")
        bundledPlugin("com.intellij.modules.json")
    }

//    intellijPlatform {
//        // Du må oppdatere til en 2026.2+ versjon (f.eks. "262.XXXX" eller tilsvarende stabil versjon)
//        type.set(IntelliJPlatformType.IntelliJIdeaUltimate)
//        version.set("2026.2")
//    }

}
