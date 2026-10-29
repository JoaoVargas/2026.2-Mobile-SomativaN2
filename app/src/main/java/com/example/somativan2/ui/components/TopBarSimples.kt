package com.example.somativan2.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarSimples(
    titulo: String,
    aoVoltar: (() -> Unit)? = null
) {
    TopAppBar(
        title = { Text(titulo) },
        navigationIcon = {
            if (aoVoltar != null) {
                IconButton(onClick = aoVoltar) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                }
            }
        }
    )
}
