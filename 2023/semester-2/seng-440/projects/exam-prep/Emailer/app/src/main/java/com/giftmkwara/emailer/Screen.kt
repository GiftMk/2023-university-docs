package com.giftmkwara.emailer

sealed class Screen(val route: String) {
    object Home: Screen("home_screen")
    object Sent: Screen("sent_screen")
}
