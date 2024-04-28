package com.giftmkwara.memories.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.giftmkwara.memories.model.Memory

class MainViewModel: ViewModel() {

    var username by mutableStateOf("")
    var password by mutableStateOf("")
    var memories: List<Memory> by mutableStateOf(emptyList())
        private set

    fun addMemory(memory: Memory) {
        val updatedMemories = memories.toMutableList()
        updatedMemories.add(memory)
        memories = updatedMemories
    }

    fun hasValidCredentials(): Boolean {
        return username.isNotBlank() && password.isNotBlank()
    }
}