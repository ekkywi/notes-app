package com.ekky.notes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthRequestDto(
    val username: String,
    val password: String
)

data class CreateNoteRequestDto(
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String
)