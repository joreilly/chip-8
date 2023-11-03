plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version Versions.composeMultiplatform
    application
}

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:${Versions.kotlinxCoroutines}")
    implementation(compose.desktop.currentOs)
}

compose {
    kotlinCompilerPlugin.set("1.5.2.1-rc01")
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=1.9.20-RC")
}

application {
    mainClass.set("MainKt")
}

tasks.getByName<JavaExec>("run").workingDir=project.rootDir
