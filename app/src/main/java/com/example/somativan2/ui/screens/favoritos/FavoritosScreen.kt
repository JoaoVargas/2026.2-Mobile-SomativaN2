package com.example.somativan2.ui.screens.favoritos

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.somativan2.ui.components.CidadeItemCard
import com.example.somativan2.ui.components.TopBarSimples

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoritosScreen(
    aoAdicionarCidade: () -> Unit,
    viewModel: FavoritosViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopBarSimples(titulo = "Favoritos") },
        floatingActionButton = {
            FloatingActionButton(onClick = aoAdicionarCidade) {
                Icon(Icons.Filled.Add, contentDescription = "Adicionar cidade")
            }
        }
    ) { innerPadding ->
        if (uiState.cidades.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma cidade favoritada. Toque em + para adicionar.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(uiState.cidades) { favorita ->
                    CidadeItemCard(
                        nome = favorita.cidade.nome,
                        subtitulo = favorita.resumo,
                        aoExcluir = { viewModel.confirmarExclusao(favorita.cidade) }
                    )
                }
            }
        }

        uiState.cidadeParaExcluir?.let { cidade ->
            AlertDialog(
                onDismissRequest = viewModel::cancelarExclusao,
                title = { Text("Excluir ${cidade.nome}?") },
                text = { Text("Essa cidade será removida dos favoritos.") },
                confirmButton = {
                    TextButton(onClick = viewModel::excluir) { Text("Excluir") }
                },
                dismissButton = {
                    TextButton(onClick = viewModel::cancelarExclusao) { Text("Cancelar") }
                }
            )
        }
    }
}
