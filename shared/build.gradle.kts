@file:OptIn(ExperimentalWasmDsl::class)

import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    jvmToolchain(17)

    androidTarget()
    jvm()

    listOf(
        iosArm64(), iosX64(), iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    wasmJs {
        browser()
    }

    sourceSets {
        commonMain {
            dependencies {
<<<<<<< Updated upstream
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2-wasm1")
=======
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.2-wasm3")
                api("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4-wasm0")
>>>>>>> Stashed changes
            }
        }
    }
}

android {
    compileSdk = 34
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    namespace = "dev.johnoreilly.chip_8_kmm.shared"
}
