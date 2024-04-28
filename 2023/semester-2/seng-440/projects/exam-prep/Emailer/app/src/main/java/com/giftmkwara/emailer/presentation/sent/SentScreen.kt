package com.giftmkwara.emailer.presentation.sent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.giftmkwara.emailer.presentation.shared.MainScaffold
import com.giftmkwara.emailer.presentation.shared.MainViewModel
import com.giftmkwara.emailer.presentation.shared.isPortrait
import com.giftmkwara.emailer.presentation.shared.recipients
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentScreen(mainViewModel: MainViewModel) {
    MainScaffold {
        Column(modifier = Modifier.fillMaxSize()) {
            if (mainViewModel.sentEmails.isEmpty()) {
                Text(text = "You haven't sent any emails")
            } else {
                var selectedEmail by remember {
                    mutableStateOf(mainViewModel.sentEmails[0])
                }
                val count = if (isPortrait()) 1 else 2
                var showEmailDialog by remember {
                    mutableStateOf(false)
                }

                if (showEmailDialog) {
                    AlertDialog(
                        onDismissRequest = { showEmailDialog = false },
                        confirmButton = {
                            TextButton(
                                onClick = { showEmailDialog = false }) {
                                Text(text = "CLOSE")
                            }
                        },
                        text = {
                            Column(
                                modifier = Modifier.verticalScroll(rememberScrollState()),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val recipientsList = mutableListOf(selectedEmail.to)
                                recipientsList.addAll(selectedEmail.cc)

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.Bottom,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = selectedEmail.subject,
                                        style = MaterialTheme.typography.headlineSmall.copy(
                                            fontWeight = FontWeight.Medium
                                        )
                                    )
                                    Text(
                                        text = formatDate(selectedEmail.timestamp),
                                        color = Color.Gray
                                    )
                                }
                                Text(
                                    text = "Sent to ${recipientsList.joinToString(separator = ", ")}",
                                    color = Color.Gray
                                )
                                Text(text = selectedEmail.body)
                            }
                        }
                    )
                }

                LazyVerticalGrid(columns = GridCells.Fixed(count = count)) {
                    items(mainViewModel.sentEmails) {
                        Card(
                            modifier = Modifier.padding(4.dp),
                            onClick = {
                                selectedEmail = it
                                showEmailDialog = true
                            }
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.Bottom,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = it.subject,
                                            style = MaterialTheme.typography.headlineSmall.copy(
                                                fontWeight = FontWeight.Medium
                                            )
                                        )
                                        Text(
                                            text = formatDate(it.timestamp),
                                            color = Color.Gray
                                        )
                                    }
                                    Text(
                                        text = "Sent to ${recipients(it)}",
                                        color = Color.Gray
                                    )
                                }
                                Text(text = it.body, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            }
                        }
                    }
                }
            }
        }
    }
}

fun formatDate(timestamp: Long): String {
    val formatter = SimpleDateFormat("d MMM yyyy", Locale.getDefault())
    return formatter.format(Date(timestamp))
}