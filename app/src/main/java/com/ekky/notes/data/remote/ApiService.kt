package com.ekky.notes.data.remote

import com.ekky.notes.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("/notes-api/auth/login")
    suspend fun  login(@Body request: AuthRequest): Response<AuthResponse>

    @GET("/notes-api/notes")
    suspend fun getAllNotes(@Header("Authorization") token: String): Response<NotesResponseDto>

    @POST("/notes-api/notes")
    suspend fun createNote(
        @Header("Authorization") token: String,
        @Body request: CreateNoteRequest
    ): Response<SingleNoteResponseDto>

    @PUT("/notes-api/notes/{id}")
    suspend fun updateNote(
        @Header("Authorization") token: String,
        @Path("id") id: String,
        @Body request: CreateNoteRequest
    ): Response<SingleNoteResponseDto>

    @DELETE("/notes-api/notes/{id}")
    suspend fun deleteNote(
        @Header("Authorization") token: String,
        @Path("id") id: String
    ): Response<Unit>
}