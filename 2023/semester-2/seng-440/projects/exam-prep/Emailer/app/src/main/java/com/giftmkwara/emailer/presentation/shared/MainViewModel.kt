package com.giftmkwara.emailer.presentation.shared

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.giftmkwara.emailer.model.Email

class MainViewModel: ViewModel() {

    var sentEmails: List<Email> by mutableStateOf(emptyList())
        private set

    fun addEmail(email: Email) {
        val updated = sentEmails.toMutableList()
        updated.add(email)
        sentEmails = updated.toMutableList()
    }

    fun removeLastEmail() {
        val updated = sentEmails.toMutableList()
        updated.removeLast()
        sentEmails = updated.toMutableList()
    }
}