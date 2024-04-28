package com.giftmkwara.memories.create

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalMinimumInteractiveComponentEnforcement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.giftmkwara.memories.LocalNavController
import com.giftmkwara.memories.model.Memory
import com.giftmkwara.memories.shared.ActionButton
import com.giftmkwara.memories.shared.MainScaffold
import com.giftmkwara.memories.shared.MainViewModel
import com.giftmkwara.memories.shared.Screen
import com.giftmkwara.memories.shared.formatDate
import com.giftmkwara.memories.shared.formatTime
import com.giftmkwara.memories.shared.isPortrait
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    mainViewModel: MainViewModel,
    createViewModel: CreateViewModel = viewModel()
) {
    val context = LocalContext.current
    val navController = LocalNavController.current

    val inputWeight = if (isPortrait()) 1f else .6f

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    var showDatePickerDialog by remember {
        mutableStateOf(false)
    }
    var showTimePickerDialog by remember {
        mutableStateOf(false)
    }

    val actionButton by remember {
        mutableStateOf(
            ActionButton(
                icon = Icons.Default.Add,
                onClick = {
                    if (createViewModel.valuesAreValid()) {
                        mainViewModel.addMemory(
                            Memory(
                                description = createViewModel.description,
                                date = createViewModel.date,
                                time = createViewModel.time
                            )
                        )
                        navController?.navigate(Screen.View.route)
                    } else Toast.makeText(
                        context,
                        "Please make sure all fields are entered",
                        Toast.LENGTH_SHORT
                    ).show()
                },
                description = "Add memory"
            )
        )
    }

    fun updateSelectedTime() {
        createViewModel.time = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, timePickerState.hour)
            set(Calendar.MINUTE, timePickerState.minute)
        }.timeInMillis
    }

    MainScaffold(actionButton = actionButton) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(32.dp)
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.SemiBold),
                text = "Create a Memory"
            )
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(
                    modifier = Modifier.fillMaxWidth(inputWeight),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Date", color = Color.Gray)
                    Spacer(modifier = Modifier.size(8.dp))
                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                        Button(onClick = { showDatePickerDialog = true }) {
                            Text(text = formatDate(createViewModel.date))
                        }
                    }
                }
                Spacer(modifier = Modifier.size(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(inputWeight),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Time", color = Color.Gray)
                    Spacer(modifier = Modifier.size(8.dp))
                    CompositionLocalProvider(LocalMinimumInteractiveComponentEnforcement provides false) {
                        Button(onClick = { showTimePickerDialog = true }) {
                            Text(text = formatTime(createViewModel.time))
                        }
                    }
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(text = "Description", color = Color.Gray)
                TextField(
                    modifier = Modifier.fillMaxSize(),
                    value = createViewModel.description,
                    onValueChange = { createViewModel.description = it },
                    minLines = 5
                )
            }
        }

        if (showDatePickerDialog) {
            DatePickerDialog(
                onDismissRequest = { showDatePickerDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            datePickerState.selectedDateMillis?.let { createViewModel.date = it }
                            showDatePickerDialog = false
                        }
                    ) {
                        Text(text = "OKAY")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDatePickerDialog = false }) {
                        Text(text = "CANCEL")
                    }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }

        if (showTimePickerDialog) {
            AlertDialog(
                onDismissRequest = { showTimePickerDialog = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            updateSelectedTime()
                            showTimePickerDialog = false
                        }
                    ) {
                        Text(text = "OKAY")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showTimePickerDialog = false }) {
                        Text(text = "CANCEL")
                    }
                },
                text = { TimePicker(state = timePickerState) }
            )
        }
    }

}