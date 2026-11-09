@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

kotlin {
    android {
        namespace = "dev.johnoreilly.chip8.ui"
        compileSdk = libs.versions.compileSdk.get().toInt()
        minSdk = libs.versions.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_21
        }
        // Required for Compose Multiplatform resources (the ROMs in composeResources/files)
        androidResources {
            enable = true
        }
    }
    jvm()
    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
                api(libs.compose.runtime)
                api(libs.compose.foundation)
                api(libs.compose.material3)
                api(libs.compose.material.icons.extended)
                api(libs.compose.components.resources)
                api(project(":shared"))
                implementation(libs.kotlinx.coroutines)
            }
        }
    }
}

compose.resources {
    packageOfResClass = "dev.johnoreilly.chip8.ui.resources"
}
