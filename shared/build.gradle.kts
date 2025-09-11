@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl

plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    androidTarget()
    iosArm64()
    iosX64()
    iosSimulatorArm64()
    jvm()
    wasmJs {
        browser()
    }

    @OptIn(ExperimentalSwiftExportDsl::class)
    swiftExport {
        moduleName = "Shared"

        // see https://youtrack.jetbrains.com/issue/KT-81270#focus=Comments-27-12735527.0-0
        //flattenPackage = "dev.johnoreilly.chip8"
    }

    dependencies {
        implementation(libs.kotlinx.coroutines)
        api(libs.kotlinx.collections.immutable)
    }
}

android {
    compileSdk = 36
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
    }
    namespace = "dev.johnoreilly.chip_8_kmm.shared"
}
