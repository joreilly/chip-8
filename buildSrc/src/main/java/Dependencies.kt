
object Versions {
    const val kotlin = "1.5.10"
    const val kotlinxCoroutines = "1.5.0-native-mt"

    const val compose = "1.0.0-rc01"
    const val nav_compose = "2.4.0-alpha04"

    const val junit = "4.13"
}


object AndroidSdk {
    const val min = 21
    const val compile = 30
    const val target = compile
}


object Compose {
    const val ui = "androidx.compose.ui:ui:${Versions.compose}"
    const val uiGraphics = "androidx.compose.ui:ui-graphics:${Versions.compose}"
    const val uiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val foundationLayout = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
    const val material = "androidx.compose.material:material:${Versions.compose}"
    const val materialIconsExtended = "androidx.compose.material:material-icons-extended:${Versions.compose}"
    const val navigation = "androidx.navigation:navigation-compose:${Versions.nav_compose}"
}



