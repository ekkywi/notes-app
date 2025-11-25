package com.ekky.notes.ui.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.loginEvent.collect {
            navController.navigate("home") {
                popUpTo("login") { inclusive = true }
            }
        }
    }

    val isLogin = viewModel.isLoginMode.value

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = if (isLogin) "Login Note App" else "Daftar Akun Baru",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
            value = viewModel.username.value,
            onValueChange = { viewModel.username.value = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        if (!isLogin) {
            OutlinedTextField(
                value = viewModel.email.value,
                onValueChange = { viewModel.email.value = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        OutlinedTextField(
            value = viewModel.password.value,
            onValueChange = { viewModel.password.value = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.submit() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (isLogin) "LOGIN" else "DAFTAR")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { viewModel.isLoginMode.value = !isLogin }) {
            Text(if (isLogin) "Belum punya akun? Daftar" else "Sudah punya akun? Login")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = viewModel.loginResult.value)
    }
}