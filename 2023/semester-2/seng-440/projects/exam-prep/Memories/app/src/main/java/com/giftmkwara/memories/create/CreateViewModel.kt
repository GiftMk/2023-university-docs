package com.giftmkwara.memories.create

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.util.Calendar

class CreateViewModel: ViewModel() {

    var description by mutableStateOf("")
    var date by mutableLongStateOf(Calendar.getInstance().timeInMillis)
    var time by mutableLongStateOf(Calendar.getInstance().timeInMillis)

    fun valuesAreValid(): Boolean {
        return description.isNotBlank()
    }
}