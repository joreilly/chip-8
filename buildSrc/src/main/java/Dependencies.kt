
object Versions {
    const val kotlin = "2.0.0-Beta1"
    const val kotlinxCoroutines = "1.8.0-RC"

    const val compose = "1.5.3"
    const val composeCompiler = "1.5.5-dev-k2.0.0-Beta1-06b8ae672a4"
    const val navCompose = "2.5.2"
    const val composeMultiplatform = "1.5.10-dev-wasm03"
    const val wearCompose = "1.1.0"

    const val junit = "4.13"
}


object AndroidSdk {
    const val min = 21
    const val minWear = 28
    const val compile = 34
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

    const val wearFoundation = "androidx.wear.compose:compose-foundation:${Versions.wearCompose}"
    const val wearMaterial = "androidx.wear.compose:compose-material:${Versions.wearCompose}"
    const val wearNavigation = "androidx.wear.compose:compose-navigation:${Versions.wearCompose}"

    const val activityCompose = "androidx.activity:activity-compose:1.4.0"
}



