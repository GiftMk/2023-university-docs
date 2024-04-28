package com.giftmkwara.emailer.presentation.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.giftmkwara.emailer.model.Email
import kotlinx.coroutines.delay

class HomeViewModel : ViewModel() {
    val sendTime = 1000L

    var to by mutableStateOf("")
    var cc by mutableStateOf("")
    var ccRecipients: List<String> by mutableStateOf(emptyList())
    var subject by mutableStateOf("")
    var body by mutableStateOf("")
    var isSending by mutableStateOf(false)

    fun clearAll() {
        to = ""
        cc = ""
        ccRecipients = emptyList()
        subject = ""
        body = ""
    }

    fun clearCc() {
        cc = ""
    }

    suspend fun send() {
        isSending = true
        delay(sendTime)
        isSending = false
    }

    fun addCcRecipient(recipient: String) {
        if (recipient.isNotBlank()) {
            val updated = ccRecipients.toMutableList()
            updated.add(recipient)
            ccRecipients = updated.toList()
        }
    }

    fun removeCcRecipient(recipient: String) {
        val updated = ccRecipients.toMutableList()
        updated.remove(recipient)
        ccRecipients = updated.toList()
    }

    fun emailIsValid(): Boolean {
        return to.isNotBlank() && subject.isNotBlank() && body.isNotBlank()
    }

    fun getEmail(): Email {
        return Email(
            to = to,
            cc = ccRecipients,
            subject = subject,
            body = body,
            timestamp = System.currentTimeMillis()
        )
    }
}