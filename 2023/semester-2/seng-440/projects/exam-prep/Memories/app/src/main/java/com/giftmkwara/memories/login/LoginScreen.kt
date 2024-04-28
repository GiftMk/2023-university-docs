package com.giftmkwara.memories.login

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.giftmkwara.memories.LocalNavController
import com.giftmkwara.memories.shared.MainScaffold
import com.giftmkwara.memories.shared.MainViewModel
import com.giftmkwara.memories.R
import com.giftmkwara.memories.shared.Screen

@Composable
fun LoginScreen(mainViewModel: MainViewModel) {
    val context = LocalContext.current
    val navController = LocalNavController.current
    var showPassword by remember {
        mutableStateOf(false)
    }

    MainScaffold(showBottomBar = false) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                text = "Welcome Back"
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Username", color = Color.Gray)
                    TextField(
                        value = mainViewModel.username,
                        onValueChange = { mainViewModel.username = it },
                        singleLine = true
                    )
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "Password", color = Color.Gray)
                    TextField(
                        value = mainViewModel.password,
                        onValueChange = { mainViewModel.password = it },
                        singleLine = true,
                        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val res =
                                if (showPassword) R.drawable.visibility_on else R.drawable.visibility_off

                            Icon(
                                modifier = Modifier.clickable { showPassword = !showPassword },
                                painter = painterResource(id = res),
                                contentDescription = "Visibility Icon"
                            )
                        }
                    )
                }
            }
            Button(
                onClick = {
                    if (mainViewModel.hasValidCredentials()) {
                        navController?.navigate(Screen.Create.route)
                    } else Toast.makeText(
                        context,
                        "Your username or password is invalid",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                Text(text = "Login")
            }
        }
    }
}