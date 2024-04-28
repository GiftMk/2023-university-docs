package com.giftmkwara.emailer.presentation.shared

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.giftmkwara.emailer.LocalNavController
import com.giftmkwara.emailer.LocalSnackbarHostState
import com.giftmkwara.emailer.LocalThemeState
import com.giftmkwara.emailer.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    floatingActionButton: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val navController = LocalNavController.current
    fun isSelected(route: String) = currentRoute(navController).contains(route)
    val scrollBehaviour = TopAppBarDefaults.enterAlwaysScrollBehavior()
    val snackbarHostState = LocalSnackbarHostState.current
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val themeState = LocalThemeState.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Dark mode enabled",
                            fontWeight = FontWeight.Medium,
                            color = Color.Gray
                        )
                        Switch(
                            checked = themeState.darkTheme,
                            onCheckedChange = themeState.setDarkTheme
                        )
                    }
                }
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehaviour.nestedScrollConnection),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text(text = "HappyMail") },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                navController.navigateUp()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back icon"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { coroutineScope.launch { drawerState.open() } }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Settings icon"
                            )
                        }
                    },
                    scrollBehavior = scrollBehaviour
                )
            },
            bottomBar = {
                NavigationBar {
                    NavigationBarItem(
                        selected = isSelected(Screen.Home.route),
                        onClick = { navController.navigate(Screen.Home.route) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Home,
                                contentDescription = "Home icon"
                            )
                        },
                        label = { Text(text = "Home") }
                    )
                    NavigationBarItem(
                        selected = isSelected(Screen.Sent.route),
                        onClick = { navController.navigate(Screen.Sent.route) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = "Sent icon"
                            )
                        },
                        label = { Text(text = "Sent") }
                    )
                }
            },
            floatingActionButton = floatingActionButton
        ) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .padding(16.dp)
            ) {
                content()
            }
        }
    }
}