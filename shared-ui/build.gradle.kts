@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    androidTarget()
    jvm()
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material3)
                api(compose.materialIconsExtended)
                api(compose.components.resources)
                api(project(":shared"))
                implementation(libs.kotlinx.coroutines)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "dev.johnoreilly.chip8.ui.resources"
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    namespace = "dev.johnoreilly.chip8.ui"
}
