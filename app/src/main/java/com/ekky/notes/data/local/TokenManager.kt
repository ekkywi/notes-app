package com.ekky.notes.data.local

object TokenManager {
    var token: String? = null

    fun getBearerToken(): String {
        return "Bearer $token"
    }
}