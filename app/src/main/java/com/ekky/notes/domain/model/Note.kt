package com.ekky.notes.domain.model

data class Note(
    val id: String,
    val title: String,
    val description: String,
    val userId: String
)
