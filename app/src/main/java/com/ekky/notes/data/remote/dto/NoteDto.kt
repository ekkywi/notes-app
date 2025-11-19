package com.ekky.notes.data.remote.dto

import com.google.gson.annotations.SerializedName

data class NoteDto(
    val id: String,
    val title: String,
    val description: String,
    @SerializedName("user_id")
    val userId: String
)

data class NotesResponseDto(
    val notes: List<NoteDto>
)

data class SingleNoteResponseDto(
    val message: String,
    val note: NoteDto?
)