package com.ekky.notes.data.mapper

import com.ekky.notes.data.remote.dto.NoteDto
import com.ekky.notes.domain.model.Note

fun NoteDto.toDomain(): Note {
    return Note(
        id = id,
        title = title,
        description = description,
        userId = userId,
    )
}