package com.example.somativan2.ui.screens.adicionar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.somativan2.ui.components.TopBarSimples

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdicionarCidadeScreen(
    aoVoltar: () -> Unit,
    aoCidadeAdicionada: () -> Unit,
    viewModel: AdicionarCidadeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var expandido by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBarSimples(titulo = "Adicionar cidade", aoVoltar = aoVoltar) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandido && uiState.sugestoes.isNotEmpty(),
                onExpandedChange = { expandido = it }
            ) {
                OutlinedTextField(
                    value = uiState.query,
                    onValueChange = {
                        viewModel.onQueryChange(it)
                        expandido = true
                    },
                    label = { Text("Nome da cidade") },
                    isError = uiState.erro != null,
                    singleLine = true,
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )
                ExposedDropdownMenu(
                    expanded = expandido && uiState.sugestoes.isNotEmpty(),
                    onDismissRequest = { expandido = false }
                ) {
                    uiState.sugestoes.forEach { sugestao ->
                        DropdownMenuItem(
                            text = { Text("${sugestao.nome} — ${sugestao.estadoOuPais}") },
                            onClick = {
                                viewModel.onSugestaoSelecionada(sugestao)
                                expandido = false
                            }
                        )
                    }
                }
            }
            uiState.erro?.let {
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Button(
                onClick = { viewModel.adicionar(onSucesso = aoCidadeAdicionada) },
                enabled = uiState.selecionada != null && !uiState.salvando,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            ) {
                Text("Adicionar")
            }
        }
    }
}
