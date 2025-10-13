@file:OptIn(ExperimentalWasmDsl::class, ExperimentalKotlinGradlePluginApi::class)
@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.swiftexport.ExperimentalSwiftExportDsl
import org.jetbrains.kotlin.gradle.plugin.mpp.apple.swiftexport.SWIFT_EXPORT_COROUTINES_SUPPORT_TURNED_ON

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
        flattenPackage = "dev.johnoreilly.chip8"

        configure {
            settings.put(SWIFT_EXPORT_COROUTINES_SUPPORT_TURNED_ON, "true")
        }
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
