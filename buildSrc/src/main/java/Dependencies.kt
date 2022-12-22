
object Versions {
    const val kotlin = "1.8.0-RC"
    const val kotlinxCoroutines = "1.6.4"

    const val compose = "1.4.0-alpha03"
    const val composeCompiler = "1.4.0-dev-k1.8.0-RC-4c1865595ed"
    const val navCompose = "2.5.2"
    const val composeDesktop = "1.3.0-beta03"

    const val junit = "4.13"
}


object AndroidSdk {
    const val min = 21
    const val compile = 33
    const val target = compile
}


object Compose {
    const val ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
    const val material = "androidx.compose.material:material:${Versions.compose}"
    const val materialIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val navigation = "androidx.navigation:navigation-compose:${Versions.navCompose}"
}



