package com.giftmkwara.emailer.model

data class Email(
    val subject: String,
    val to: String,
    val cc: List<String>,
    val body: String,
    val timestamp: Long
)
