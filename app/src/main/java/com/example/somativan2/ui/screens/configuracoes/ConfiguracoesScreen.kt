package com.example.somativan2.ui.screens.configuracoes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.somativan2.ui.components.TopBarSimples

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfiguracoesScreen(
    viewModel: ConfiguracoesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopBarSimples(titulo = "Configurações") }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SecaoUnidade(
                titulo = "Unidade de temperatura",
                opcoes = listOf("C" to "Celsius", "F" to "Fahrenheit"),
                selecionada = uiState.unidadeTemperatura,
                aoSelecionar = viewModel::selecionarUnidadeTemperatura
            )
            SecaoUnidade(
                titulo = "Unidade de vento",
                opcoes = listOf("KMH" to "Km/h", "MPH" to "Mph"),
                selecionada = uiState.unidadeVento,
                aoSelecionar = viewModel::selecionarUnidadeVento
            )
        }
    }
}

@Composable
private fun SecaoUnidade(
    titulo: String,
    opcoes: List<Pair<String, String>>,
    selecionada: String,
    aoSelecionar: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(titulo, style = MaterialTheme.typography.titleMedium)
            opcoes.forEach { (valor, rotulo) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selecionada == valor,
                            onClick = { aoSelecionar(valor) }
                        )
                        .padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(selected = selecionada == valor, onClick = { aoSelecionar(valor) })
                    Text(rotulo, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
