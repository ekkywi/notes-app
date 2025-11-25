package com.ekky.notes.ui.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekky.notes.data.local.TokenManager
import com.ekky.notes.data.remote.ApiService
import com.ekky.notes.data.remote.dto.AuthRequestDto
import com.ekky.notes.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val repository: NoteRepository,
    private val tokenManager: TokenManager
) : ViewModel() {

    var username = mutableStateOf("")
    var password = mutableStateOf("")
    var email = mutableStateOf("")
    var isLoginMode = mutableStateOf(true)
    var loginResult = mutableStateOf("")

    private val _loginEvent = MutableSharedFlow<Unit>()
    val loginEvent = _loginEvent.asSharedFlow()

    fun submit() {
        if (isLoginMode.value) {
            login()
        } else {
            register()
        }
    }

    private fun register() {
        viewModelScope.launch {
            loginResult.value = "Mendaftarkan..."
            val result = repository.register(username.value, email.value, password.value)

            result.onSuccess {
                loginResult.value = "Register Berhasil! Silakan Login."
                isLoginMode.value = true
                password.value = ""
            }.onFailure { error ->
                loginResult.value = "Register Gagal: ${error.message}"
            }
        }
    }

    private fun login() {
        viewModelScope.launch {
            loginResult.value = "Loading..."
            try {
                val request = AuthRequestDto(username = username.value, password = password.value)
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    if (token != null) tokenManager.saveToken(token)
                    loginResult.value = "Login Berhasil!"
                    _loginEvent.emit(Unit)
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    loginResult.value = "Gagal: $errorBody"
                }
            } catch (e: Exception) {
                loginResult.value = "Error: ${e.message}"
            }
        }
    }
}