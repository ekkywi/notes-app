package com.ekky.notes

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekky.notes.data.local.TokenManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val tokenManager: TokenManager
) : ViewModel() {

    // State untuk tujuan awal
    private val _startDestination = mutableStateOf("login") // Default ke login
    val startDestination: State<String> = _startDestination

    // State untuk Loading (PENTING!)
    private val _isLoading = mutableStateOf(true)
    val isLoading: State<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            // 1. Ambil token dari DataStore
            val token = tokenManager.token.first()

            // 2. Tentukan tujuan berdasarkan token
            if (!token.isNullOrEmpty()) {
                _startDestination.value = "home"
            } else {
                _startDestination.value = "login"
            }

            // 3. Matikan loading (Sekarang UI boleh tampil)
            _isLoading.value = false
        }
    }
}