package com.giftmkwara.memories

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.giftmkwara.memories.shared.MainViewModel
import com.giftmkwara.memories.shared.Screen
import com.giftmkwara.memories.create.CreateScreen
import com.giftmkwara.memories.login.LoginScreen
import com.giftmkwara.memories.ui.theme.MemoriesTheme
import com.giftmkwara.memories.view.ViewScreen

val LocalNavController = staticCompositionLocalOf<NavController?> { null }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MemoriesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val mainViewModel: MainViewModel = viewModel()

                    CompositionLocalProvider(LocalNavController provides navController) {
                        NavHost(navController = navController, startDestination = Screen.Login.route) {
                            composable(route = Screen.Login.route) {
                                LoginScreen(mainViewModel = mainViewModel)
                            }
                            composable(route = Screen.Create.route) {
                                CreateScreen(mainViewModel = mainViewModel)
                            }
                            composable(route = Screen.View.route) {
                                ViewScreen(mainViewModel = mainViewModel)
                            }
                        }
                    }
                }
            }
        }
    }
}
