package com.giftmkwara.emailer.presentation.shared

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import com.giftmkwara.emailer.model.Email

fun currentRoute(navController: NavController) = navController.currentBackStackEntry?.destination?.route ?: ""

@Composable
fun isPortrait() = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

fun recipients(email: Email): String {
    val numCcRecipients = email.cc.size
    val otherOrOthers = if (numCcRecipients == 1) "other" else "others"

    return if (numCcRecipients == 0) {
        email.to
    } else "${email.to} and $numCcRecipients $otherOrOthers"
}