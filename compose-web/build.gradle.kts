plugins {
    kotlin("multiplatform")
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.jetbrainsCompose)
}

group = "com.example"
version = "1.0-SNAPSHOT"

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    wasmJs {
        moduleName = "chip8"
        browser {
            commonWebpackConfig {
                outputFileName = "chip8.js"
//                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(
//                    static = (devServer?.static ?: mutableListOf()).apply {
//                        // Serve sources to debug inside browser
//                        add(project.rootDir.path)
//                        add(project.rootDir.path + "/compose-web/")
//                    },)

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
}

