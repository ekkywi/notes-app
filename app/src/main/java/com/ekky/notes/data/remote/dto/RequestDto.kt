package com.ekky.notes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthRequest(
    val username: String,
    val password: String
)

data class AuthResponse(
    val message: String,
    val token: String?
)

data class CreateNoteRequest(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String
)