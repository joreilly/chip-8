pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
        maven("https://redirector.kotlinlang.org/maven/dev")
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
        maven("https://redirector.kotlinlang.org/maven/dev")
    }
}

rootProject.name = "chip-8"
include(":androidApp")
include(":shared")
include(":compose-desktop")
include(":wearApp")
include(":compose-web")
