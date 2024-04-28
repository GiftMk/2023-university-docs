package com.giftmkwara.emailer.presentation.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.giftmkwara.emailer.LocalSnackbarHostState
import com.giftmkwara.emailer.dismissAndShowSnackbar
import com.giftmkwara.emailer.presentation.shared.recipients
import com.giftmkwara.emailer.presentation.shared.MainScaffold
import com.giftmkwara.emailer.presentation.shared.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class,
    ExperimentalAnimationApi::class
)
@Composable
fun HomeScreen(
    mainViewModel: MainViewModel,
    homeViewModel: HomeViewModel = viewModel()
) {
    val snackbarHostState = LocalSnackbarHostState.current

    var ccIsExpanded by remember {
        mutableStateOf(false)
    }
    var confirmationDialogIsOpen by remember {
        mutableStateOf(false)
    }

    var progress by remember {
        mutableStateOf(0f)
    }

    val coroutineScope = rememberCoroutineScope()

    suspend fun loadProgress() {
        for (i in 1..100) {
            progress = i.toFloat() / 100
            delay(homeViewModel.sendTime / 100)
        }
    }

    var emailWasSent by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = emailWasSent) {
        if (emailWasSent) {
            snackbarHostState.dismissAndShowSnackbar(
                message = "Email was sent",
                actionLabel = "Undo"
            ).run {
                if (this == SnackbarResult.ActionPerformed) {
                    mainViewModel.removeLastEmail()
                }
            }
        }
    }

    MainScaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    if (homeViewModel.emailIsValid()) {
                        confirmationDialogIsOpen = true
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.dismissAndShowSnackbar("Please make sure all fields are filled in correctly")
                        }
                    }
                },
                text = { Text(text = "Send") },
                icon = { Icon(imageVector = Icons.Default.Email, "Send email icon") })

        }
    ) {
        AnimatedContent(targetState = homeViewModel.isSending, label = "") { isSending ->
            if (isSending) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = "Sending...")
                    Spacer(modifier = Modifier.size(4.dp))
                    LinearProgressIndicator(progress = progress)
                }
            } else {
                Column(modifier = Modifier.fillMaxSize()) {
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            val text = if (homeViewModel.ccRecipients.isEmpty()) {
                                "Cc"
                            } else "Cc (${homeViewModel.ccRecipients.size})"

                            FilterChip(
                                modifier = Modifier.padding(horizontal = 8.dp),
                                selected = ccIsExpanded,
                                shape = CircleShape,
                                onClick = { ccIsExpanded = !ccIsExpanded },
                                label = { Text(modifier = Modifier.height(IntrinsicSize.Max), text = text) }
                            )
                        },
                        label = { Text(text = "To") },
                        value = homeViewModel.to,
                        onValueChange = { homeViewModel.to = it },
                        singleLine = true
                    )
                    AnimatedVisibility(
                        visible = ccIsExpanded,
                        enter = fadeIn(animationSpec = spring()),
                        exit = fadeOut(animationSpec = spring())
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text(text = "Cc") },
                                value = homeViewModel.cc,
                                onValueChange = { homeViewModel.cc = it },
                                singleLine = true,
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        homeViewModel.addCcRecipient(homeViewModel.cc)
                                        homeViewModel.clearCc()
                                    }
                                )
                            )
                            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                homeViewModel.ccRecipients.map {
                                    InputChip(
                                        colors = InputChipDefaults.inputChipColors(
                                            containerColor = MaterialTheme.colorScheme.secondaryContainer
                                        ),
                                        selected = false,
                                        onClick = { homeViewModel.removeCcRecipient(it) },
                                        label = { Text(text = it) },
                                        trailingIcon = {
                                            Icon(
                                                imageVector = Icons.Default.Clear,
                                                contentDescription = "Delete recipient icon"
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text(text = "Subject") },
                        value = homeViewModel.subject,
                        onValueChange = { homeViewModel.subject = it },
                        singleLine = true
                    )
                    OutlinedTextField(
                        modifier = Modifier
                            .fillMaxSize()
                            .weight(1f),
                        label = { Text(text = "Body") },
                        value = homeViewModel.body,
                        onValueChange = { homeViewModel.body = it },
                    )
                }
            }
        }
    }

    if (confirmationDialogIsOpen) {
        val recipients = recipients(homeViewModel.getEmail())
        
        Dialog(onDismissRequest = { /*TODO*/ }) {
            
        }

        AlertDialog(
            onDismissRequest = { confirmationDialogIsOpen = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        confirmationDialogIsOpen = false

                        coroutineScope.launch {
                            loadProgress()
                        }
                        coroutineScope.launch {
                            homeViewModel.send()
                            mainViewModel.addEmail(homeViewModel.getEmail())
                            homeViewModel.clearAll()
                            emailWasSent = true
                        }
                    }
                ) {
                    Text(text = "OKAY")
                }
            },
            dismissButton = {
                TextButton(onClick = { confirmationDialogIsOpen = false }) {
                    Text(text = "CANCEL")
                }
            },
            text = { Text(text = "Are you sure you want to send this email to $recipients?") }
        )
    }
}