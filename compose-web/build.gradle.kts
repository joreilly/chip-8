import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version Versions.composeMultiplatform
}

group = "com.example"
version = "1.0-SNAPSHOT"

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    wasmJs {
        moduleName = "Chip8"
        browser {
            commonWebpackConfig {
                outputFileName = "Chip8.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(
                    static = (devServer?.static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.rootDir.path)
                        add(project.rootDir.path + "/compose-web/")
                    },)

            }
        }

        binaries.executable()
    }
    sourceSets {
        commonMain {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.components.resources)
                implementation(project(":shared"))
            }
        }
    }
}

compose.experimental {
    web.application {}
<<<<<<< Updated upstream
}

compose {
    kotlinCompilerPlugin.set("1.5.2.1-rc01")
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.9.20-RC")
=======
>>>>>>> Stashed changes
}