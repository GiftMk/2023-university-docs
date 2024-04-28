package com.giftmkwara.memories.shared

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.giftmkwara.memories.LocalNavController

data class ActionButton(val icon: ImageVector, val onClick: () -> Unit, val description: String)

data class NavigationItem(
    val route: String,
    val icon: ImageVector,
    val filledIcon: ImageVector,
    val label: String,
)

val navigationItems = listOf(
    NavigationItem(
        route = Screen.Create.route,
        icon = Icons.Default.Create,
        filledIcon = Icons.Default.Create,
        label = "Create"
    ),
    NavigationItem(
        route = Screen.View.route,
        icon = Icons.Default.List,
        filledIcon = Icons.Default.List,
        label = "View"
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    showBottomBar: Boolean = true,
    actionButton: ActionButton? = null,
    content: @Composable () -> Unit
) {
    val navController = LocalNavController.current
    fun isSelected(route: String) = currentRoute(navController).contains(route)

    Scaffold(
        topBar = {
            if (isPortrait()) {
                TopAppBar(
                    title = { Text(text = "Memories 💭", fontWeight = FontWeight.Medium) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    navigationItems.map { item ->
                        NavigationBarItem(
                            selected = isSelected(item.route),
                            onClick = { navController?.navigate(item.route) },
                            icon = {
                                val icon = if (isSelected(item.route)) {
                                    item.filledIcon
                                } else item.icon

                                Icon(
                                    imageVector = icon,
                                    contentDescription = item.label
                                )
                            },
                            label = { Text(text = item.label) }
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            actionButton?.let {
                FloatingActionButton(onClick = it.onClick) {
                    Icon(imageVector = it.icon, contentDescription = it.description)
                }
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues)
                .padding(12.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            content()
        }
    }
}