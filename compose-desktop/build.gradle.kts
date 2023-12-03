plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version libs.versions.composeMultiplatform
    application
}

dependencies {
    implementation(project(":shared"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:${libs.versions.coroutines}")
    implementation(compose.desktop.currentOs)
}

application {
    mainClass.set("MainKt")
}

tasks.getByName<JavaExec>("run").workingDir=project.rootDir

compose {
    kotlinCompilerPlugin.set("1.5.4-dev1-kt2.0.0-Beta1")
    kotlinCompilerPluginArgs.add("suppressKotlinVersionCompatibilityCheck=2.0.0-Beta1")
}