package com.ekky.notes

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.ekky.notes.ui.navigation.AppNavigation
import com.ekky.notes.ui.theme.NotesTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // (Opsional) Install Splash Screen Native Android 12+
        installSplashScreen().setKeepOnScreenCondition {
            mainViewModel.isLoading.value
        }

        setContent {
            NotesTheme {
                // Ambil status loading
                val isLoading = mainViewModel.isLoading.value
                val startDestination = mainViewModel.startDestination.value

                if (isLoading) {
                    // Tampilkan Layar Loading jika DataStore belum siap
                    LoadingScreen()
                } else {
                    // Hanya masuk ke sini jika DataStore SUDAH SELESAI dibaca
                    AppNavigation(startDestination = startDestination)
                }
            }
        }
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}