package com.ekky.notes.ui.add_edit

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AddEditScreen(
    navController: NavController,
    viewModel: AddEditViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.saveEvent.collect {
            navController.popBackStack()
        }
    }

    AddEditContent(
        title = viewModel.title.value,
        description = viewModel.description.value,
        isLoading = viewModel.isLoading.value,
        onTitleChange = { newText: String -> viewModel.title.value = newText },
        onDescriptionChange = { newText: String -> viewModel.description.value = newText },
        onSaveClick = { viewModel.saveNote() },
        onBackClick = { navController.popBackStack() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditContent(
    title: String,
    description: String,
    isLoading: Boolean,
    onTitleChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val screenTitle = if (title.isEmpty()) "Tambah Catatan" else "Edit Catatan"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(screenTitle) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onSaveClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Check, "Simpan")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                label = { Text("Judul") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = description,
                onValueChange = onDescriptionChange,
                label = { Text("Isi Catatan") },
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )

            if (isLoading) {
                Spacer(modifier = Modifier.height(16.dp))
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddEditScreenPreview() {
    AddEditContent(
        title = "Judul Contoh",
        description = "Ini adalah contoh deskripsi catatan untuk preview.",
        isLoading = false,
        onTitleChange = {},
        onDescriptionChange = {},
        onSaveClick = {},
        onBackClick = {}
    )
}