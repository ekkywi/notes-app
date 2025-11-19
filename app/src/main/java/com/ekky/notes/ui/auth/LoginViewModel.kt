package com.ekky.notes.ui.auth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ekky.notes.data.local.TokenManager
import com.ekky.notes.data.remote.dto.AuthRequest
import com.ekky.notes.data.remote.ApiService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val apiService: ApiService
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
                val request = AuthRequest(username.value, password.value)
                val response = apiService.login(request)

                if (response.isSuccessful) {
                    val token = response.body()?.token
                    TokenManager.token = token

                    loginResult.value = "Login Berhasil !"
                    _loginEvent.emit(Unit)
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Terjadi kesalahan"
                    loginResult.value = "Login Gagal: $errorBody"
                }
            } catch (e: Exception) {
                loginResult.value = "Error: ${e.localizedMessage ?: e.toString()}"
            }
        }
    }
}