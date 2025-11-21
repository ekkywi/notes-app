package com.ekky.notes.ui.add_edit

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekky.notes.data.local.TokenManager
import com.ekky.notes.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    private var currentNoteId: String? = null

    private val _saveEvent = MutableSharedFlow<Unit>()
    val saveEvent = _saveEvent.asSharedFlow()

    fun loadNote(noteId: String) {
        if (noteId == "new") {
            currentNoteId = null
            title.value = ""
            description.value = ""
            return
        }

        currentNoteId = noteId

        viewModelScope.launch {
            isLoading.value = true

            val token = tokenManager.getBearerToken()

            val result = repository.getNoteById(token, noteId)

            result.onSuccess { note ->
                if (note != null) {
                    title.value = note.title
                    description.value = note.description
                }
            }.onFailure {
                Log.e("AddEditVM", "Gagal load data: ${it.message}")
            }
            isLoading.value = false
        }
    }

    fun saveNote() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val token = tokenManager.getBearerToken()

                val result = if (currentNoteId == null) {
                    repository.createNote(token, title.value, description.value)
                } else {
                    repository.updateNote(token, currentNoteId!!, title.value, description.value)
                }

                result.onSuccess {
                    _saveEvent.emit(Unit)
                }.onFailure {
                    Log.e("AddEditVM", "Gagal: ${it.message}")
                }

            } catch (e: Exception) {
                Log.e("AddEditVM", "Error: ${e.message}")
            } finally {
                isLoading.value = false
            }
        }
    }
}