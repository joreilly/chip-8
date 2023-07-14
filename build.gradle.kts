buildscript {
    val compose_version by extra("1.2.0")
    val wear_compose_version by extra("1.0.0")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://androidx.dev/storage/compose-compiler/repository")
    }

//    configurations.all {
//        resolutionStrategy.dependencySubstitution {
//            substitute(module("org.jetbrains.compose.compiler:compiler")).apply {
//                using(module("androidx.compose.compiler:compiler:${Versions.composeCompiler}"))
//            }
//        }
//    }
}

