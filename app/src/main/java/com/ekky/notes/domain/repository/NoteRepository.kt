package com.ekky.notes.domain.repository

import com.ekky.notes.domain.model.Note

interface NoteRepository {
    suspend fun register(username: String, email: String, password: String): Result<Unit>
    suspend fun getAllNotes(token: String, search: String = "", page: Int = 1): Result<List<Note>>
    suspend fun getNoteById(token: String, id: String): Result<Note?>
    suspend fun createNote(token: String, title: String, description: String): Result<Unit>
    suspend fun updateNote(token: String, id:  String, title: String, description: String): Result<Unit>
    suspend fun deleteNote(token: String, id: String): Result<Unit>
}