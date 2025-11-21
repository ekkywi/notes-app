package com.ekky.notes.ui.auth

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekky.notes.data.local.TokenManager
import com.ekky.notes.data.remote.ApiService
import com.ekky.notes.data.remote.dto.AuthRequestDto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : ViewModel() {

    var username = mutableStateOf("")
    var password = mutableStateOf("")
    var loginResult = mutableStateOf("")

    private val _loginEvent = MutableSharedFlow<Unit>()
    val loginEvent = _loginEvent.asSharedFlow()

    fun login() {
        viewModelScope.launch {
            loginResult.value = "Loading..."
            try {
                val request = AuthRequestDto(username.value, password.value)
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    val token = response.body()?.token

                    if (token != null) {
                        tokenManager.saveToken(token)
                        Log.d("AUTH", "Token berhasil disimpan: $token")
                    }

                    loginResult.value = "Login Berhasil!"
                    _loginEvent.emit(Unit)
                } else {
                    val errorBody = response.errorBody()?.string() ?: ""
                    loginResult.value = "Gagal: $errorBody"
                    Log.e("AUTH", "Login Gagal: $errorBody")
                }
            } catch (e: Exception) {
                loginResult.value = "Error: ${e.message}"
                Log.e("AUTH", "Exception: ${e.message}")
            }
        }
    }
}