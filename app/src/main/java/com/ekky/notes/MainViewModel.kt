package com.ekky.notes

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekky.notes.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    // Kita gunakan StateFlow agar perubahan state thread-safe
    // Awalnya null (Loading)
    private val _startDestination = MutableStateFlow<String?>(null)
    val startDestination = _startDestination.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            // Tambahkan delay kecil untuk memastikan DataStore siap
            delay(200)

            val token = tokenManager.token.first()
            Log.d("DEBUG_APP", "Token di MainViewModel: $token")

            if (!token.isNullOrEmpty()) {
                _startDestination.value = "home"
                Log.d("DEBUG_APP", "Set destination -> HOME")
            } else {
                _startDestination.value = "login"
                Log.d("DEBUG_APP", "Set destination -> LOGIN")
            }

            _isLoading.value = false
        }
    }
}