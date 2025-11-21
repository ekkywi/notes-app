package com.ekky.notes.ui.home

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekky.notes.data.local.TokenManager
import com.ekky.notes.domain.model.Note
import com.ekky.notes.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlinx.coroutines.flow.first

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    var notes = mutableStateOf<List<Note>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    fun fetchNotes() {
        viewModelScope.launch {
            isLoading.value = true
            errorMessage.value = ""

            val token = tokenManager.getBearerToken()

            val result = repository.getAllNotes(token)

            result.onSuccess { data ->
                notes.value = data
            }.onFailure { error ->
                errorMessage.value = error.message ?: "Gagal memuat data"
                Log.e("HomeVM", "Error: ${error.message}")
            }

            isLoading.value = false
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            val rawToken = tokenManager.token.first()
            val token = "Bearer $rawToken"

            val result = repository.deleteNote(token, id)

            result.onSuccess {
                Log.d("HomeVM", "Deleted: $id")
                fetchNotes()
            }.onFailure {
                Log.e("HomeVM", "Delete failed: ${it.message}")
            }
        }
    }
}