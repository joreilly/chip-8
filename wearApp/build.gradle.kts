plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "dev.johnoreilly.chip8"
    compileSdk = AndroidSdk.compile
    defaultConfig {
        applicationId = "dev.johnoreilly.chip8"
        minSdk = AndroidSdk.minWear
        targetSdk = AndroidSdk.target

        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = Versions.composeCompiler
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf("-Xskip-prerelease-check",
            "-opt-in=androidx.wear.material.ExperimentalWearMaterialApi",
            "-opt-in=com.google.accompanist.pager.ExperimentalPagerApi"
        )
    }
}


dependencies {
    implementation(project(":shared"))

    with (Compose) {
        implementation(ui)
        implementation(uiGraphics)
        implementation(uiTooling)
        implementation(foundationLayout)
        implementation(activityCompose)
        implementation(materialIconsExtended)
        implementation(wearFoundation)
        implementation(wearMaterial)
        implementation(wearNavigation)
    }

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.0-alpha03")
}
