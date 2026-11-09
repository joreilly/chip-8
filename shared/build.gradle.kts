@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)
@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
}

kotlin {
    android {
        namespace = "dev.johnoreilly.chip_8_kmm.shared"
        compileSdk = 36
        minSdk = 21
    }
    iosArm64()
    iosSimulatorArm64()
    jvm()
    wasmJs {
        browser()
    }

    @OptIn(org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl::class)
    export {
        swift {
            moduleName = "Shared"
            rootPackage = "dev.johnoreilly.chip8"
            xcodeIntegration()
        }
    }

    dependencies {
        implementation(libs.kotlinx.coroutines)
    }
}
