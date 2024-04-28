package com.giftmkwara.memories.shared

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun isPortrait(): Boolean {
    return LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
}

fun currentRoute(navController: NavController?): String {
    return navController?.let {
        it.currentBackStackEntry?.destination?.route
    } ?: ""
}

fun formatDate(dateInMillis: Long): String {
    val formatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    return formatter.format(Date(dateInMillis))
}

fun formatTime(timeInMillis: Long): String {
    val formatter = SimpleDateFormat("h:mm a", Locale.getDefault())
    return formatter.format(Date(timeInMillis))
}