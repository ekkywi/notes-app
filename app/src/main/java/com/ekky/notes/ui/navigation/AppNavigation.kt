package com.ekky.notes.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ekky.notes.ui.auth.LoginScreen
import com.ekky.notes.ui.auth.LoginViewModel
import com.ekky.notes.ui.home.HomeScreen
import com.ekky.notes.ui.add_edit.AddEditScreen
import com.ekky.notes.ui.add_edit.AddEditViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {

        composable("login") {
            val viewModel: LoginViewModel = hiltViewModel()
            LoginScreen(
                navController = navController,
                viewModel = viewModel
            )
        }

        composable("home") {
            HomeScreen(navController = navController)
        }

        composable(
            route = "add_edit_note/{noteId}",
            arguments = listOf(
                navArgument("noteId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getString("noteId") ?: "new"

            val viewModel: AddEditViewModel = hiltViewModel()

            LaunchedEffect(noteId) {
                viewModel.loadNote(noteId)
            }

            AddEditScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}