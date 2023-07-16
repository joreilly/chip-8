import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.4.0-dev-wasm09"
}

group = "com.example"
version = "1.0-SNAPSHOT"

@OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
kotlin {
    wasm {
        moduleName = "Chip8"
        browser {
            commonWebpackConfig(Action {
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).copy(
                    static = (devServer?.static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.rootDir.path)
                        add(project.rootDir.path + "/compose-web/")
                    },)

            })
        }

        applyBinaryen {
            binaryenArgs = mutableListOf(
                "--enable-nontrapping-float-to-int",
                "--enable-gc",
                "--enable-reference-types",
                "--enable-exception-handling",
                "--enable-bulk-memory",
                "--hybrid",
                "--inline-functions-with-loops",
                "--traps-never-happen",
                "--fast-math",
                "-O1",
                "-c", // Run passes while binary size decreases
            )
        }

        binaries.executable()


    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.components.resources)
                implementation(project(":shared"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}

compose.experimental {
    web.application {}
}

compose {
    kotlinCompilerPlugin.set("1.4.0-dev-wasm09")
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.9.0")
}