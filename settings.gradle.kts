pluginManagement {
    repositories {
        google()
        jcenter()
        gradlePluginPortal()
        mavenCentral()
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
    
}

rootProject.name = "chip-8"

include(":androidApp", ":shared", ":compose-desktop")

include(":wearApp")
include(":compose-web")
