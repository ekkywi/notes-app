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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    var notes = mutableStateOf<List<Note>>(emptyList())
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    var searchQuery = mutableStateOf("")
    var endReached = mutableStateOf(false)
    private var currentPage = 1
    // ----------------------------

    fun fetchNotes(isLoadMore: Boolean = false) {
        viewModelScope.launch {
            if (isLoadMore && endReached.value) return@launch
            if (!isLoadMore) isLoading.value = true

            try {
                val token = tokenManager.getBearerToken()

                if (!isLoadMore) {
                    currentPage = 1
                    endReached.value = false
                }

                val result = repository.getAllNotes(token, searchQuery.value, currentPage)

                result.onSuccess { newNotes ->
                    if (newNotes.size < 10) {
                        endReached.value = true
                    }

                    if (isLoadMore) {
                        notes.value = notes.value + newNotes
                    } else {
                        notes.value = newNotes
                    }
                    currentPage++
                }.onFailure { error ->
                    errorMessage.value = error.message ?: "Gagal memuat data"
                    Log.e("HomeVM", "Error: ${error.message}")
                }

            } catch (e: Exception) {
                errorMessage.value = "Error: ${e.message}"
            } finally {
                isLoading.value = false
            }
        }
    }

    fun onSearchQueryChange(newQuery: String) {
        searchQuery.value = newQuery
        fetchNotes(isLoadMore = false)
    }

    fun loadNextPage() {
        if (!isLoading.value && !endReached.value) {
            fetchNotes(isLoadMore = true)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            val token = tokenManager.getBearerToken()
            val result = repository.deleteNote(token, id)

            result.onSuccess {
                Log.d("HomeVM", "Deleted: $id")
                fetchNotes(isLoadMore = false)
            }.onFailure {
                Log.e("HomeVM", "Delete failed: ${it.message}")
            }
        }
    }

    fun logout(onLogoutSuccess: () -> Unit) {
        viewModelScope.launch {
            tokenManager.clearToken()
            onLogoutSuccess()
        }
    }
}