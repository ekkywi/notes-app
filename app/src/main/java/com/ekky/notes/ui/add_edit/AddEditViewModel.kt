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
    private val repository: NoteRepository
) : ViewModel() {

    var title = mutableStateOf("")
    var description = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    private var currentNoteId: String? = null

    private val _saveEvent = MutableSharedFlow<Unit>()
    val saveEvent = _saveEvent.asSharedFlow()

    fun loadNote(noteId: String) {
        if (noteId == "new") return
        currentNoteId = noteId
    }

    fun saveNote() {
        viewModelScope.launch {
            isLoading.value = true
            try {
                val token = TokenManager.getBearerToken()

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