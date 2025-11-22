package com.ekky.notes

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState // Import ini PENTING
import androidx.compose.runtime.getValue
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

        installSplashScreen().setKeepOnScreenCondition {
            // Tahan splash screen selama isLoading masih true
            mainViewModel.isLoading.value
        }

        setContent {
            NotesTheme {
                // Baca StateFlow sebagai State Compose
                val isLoading by mainViewModel.isLoading.collectAsState()
                val startDestination by mainViewModel.startDestination.collectAsState()

                Log.d("DEBUG_APP", "UI Render: Loading=$isLoading, Dest=$startDestination")

                if (isLoading || startDestination == null) {
                    // Tampilkan layar kosong/loading sampai keputusan final
                    LoadingScreen()
                } else {
                    // PENTING: Pakai startDestination!! (tanda seru ganda karena sudah pasti tidak null)
                    AppNavigation(startDestination = startDestination!!)
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