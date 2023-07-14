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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
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

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.1")
}
