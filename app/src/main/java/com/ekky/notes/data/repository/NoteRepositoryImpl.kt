package com.ekky.notes.data.repository

import com.ekky.notes.data.mapper.toDomain
import com.ekky.notes.data.remote.ApiService
import com.ekky.notes.data.remote.dto.CreateNoteRequest
import com.ekky.notes.domain.model.Note
import com.ekky.notes.domain.repository.NoteRepository
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val api: ApiService
) : NoteRepository {

    override suspend fun getAllNotes(token: String): Result<List<Note>> {
        return try {
            val response = api.getAllNotes(token)
            if (response.isSuccessful) {
                val notes = response.body()?.notes?.map { it.toDomain() } ?: emptyList()
                Result.success(notes)
            } else {
                Result.failure(Exception("Gagal mengambil data: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun createNote(token: String, title: String, description: String): Result<Unit> {
        return try {
            val request = CreateNoteRequest(title, description)
            val response = api.createNote(token, request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Gagal membuat catatan"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateNote(token: String, id: String, title: String, description: String): Result<Unit> {
        return try {
            val request = CreateNoteRequest(title, description)
            val response = api.updateNote(token, id, request)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Gagal update catatan"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun deleteNote(token: String, id: String): Result<Unit> {
        return try {
            val response = api.deleteNote(token, id)
            if (response.isSuccessful) Result.success(Unit)
            else Result.failure(Exception("Gagal menghapus catatan"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}