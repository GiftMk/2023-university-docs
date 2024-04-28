package com.giftmkwara.memories.shared

sealed class Screen(val route: String) {
    object Login: Screen("login_screen")
    object Create: Screen("create_screen")
    object View: Screen("view_screen")
}
