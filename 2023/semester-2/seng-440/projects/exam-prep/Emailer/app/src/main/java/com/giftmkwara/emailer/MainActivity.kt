package com.giftmkwara.emailer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.giftmkwara.emailer.presentation.home.HomeScreen
import com.giftmkwara.emailer.presentation.sent.SentScreen
import com.giftmkwara.emailer.presentation.shared.MainViewModel
import com.giftmkwara.emailer.ui.theme.EmailerTheme

val LocalNavController =
    staticCompositionLocalOf<NavController> { throw Error("Nav controller not provided") }
val LocalSnackbarHostState =
    compositionLocalOf<SnackbarHostState> { throw Error("Snackbar host state not provided") }

data class ThemeState(
    val darkTheme: Boolean = false,
    val setDarkTheme: (value: Boolean) -> Unit = {}
)

val LocalThemeState = staticCompositionLocalOf { ThemeState() }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var darkTheme by rememberSaveable {
                mutableStateOf(false)
            }

            EmailerTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val snackbarHostState = remember { SnackbarHostState() }
                    val mainViewModel: MainViewModel = viewModel()

                    CompositionLocalProvider(
                        LocalNavController provides navController,
                        LocalSnackbarHostState provides snackbarHostState,
                        LocalThemeState provides ThemeState(
                            darkTheme = darkTheme,
                            setDarkTheme = { darkTheme = it }
                        )
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = Screen.Home.route
                        ) {
                            composable(route = Screen.Home.route) {
                                HomeScreen(mainViewModel = mainViewModel)
                            }
                            composable(route = Screen.Sent.route) {
                                SentScreen(mainViewModel = mainViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}

suspend fun SnackbarHostState.dismissAndShowSnackbar(
    message: String,
    actionLabel: String? = null,
    duration: SnackbarDuration = SnackbarDuration.Short
): SnackbarResult {
    this.currentSnackbarData?.dismiss()
    return this.showSnackbar(message = message, actionLabel = actionLabel, duration = duration)
}