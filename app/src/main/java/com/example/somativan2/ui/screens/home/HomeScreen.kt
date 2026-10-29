package com.example.somativan2.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.somativan2.data.simulador.ConversorUnidades
import com.example.somativan2.ui.components.ChipHora
import com.example.somativan2.ui.components.SeletorCidadeAtual
import com.example.somativan2.ui.components.TopBarSimples

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    aoAbrirPrevisao: () -> Unit,
    aoAbrirHistorico: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { TopBarSimples(titulo = "Clima") }
    ) { innerPadding ->
        if (uiState.favoritas.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nenhuma cidade favoritada. Vá em Favoritos para adicionar uma.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                SeletorCidadeAtual(
                    cidades = uiState.favoritas,
                    selecionada = uiState.cidadeAtual,
                    aoSelecionar = viewModel::selecionarCidadeAtual,
                    modifier = Modifier.fillMaxWidth()
                )

                if (uiState.cidadeAtual == null) {
                    Text(
                        text = "Escolha uma cidade para ver o clima.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                } else {
                    uiState.clima?.let { clima ->
                        ElevatedCard(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = ConversorUnidades.formatarTemperatura(clima.temperatura, uiState.unidadeTemperatura),
                                    style = MaterialTheme.typography.displayMedium
                                )
                                Text(
                                    text = clima.condicao,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Row(
                                    modifier = Modifier.padding(top = 16.dp),
                                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                                ) {
                                    InfoClimaSecundaria(rotulo = "Umidade", valor = "${clima.umidade}%")
                                    InfoClimaSecundaria(
                                        rotulo = "Vento",
                                        valor = ConversorUnidades.formatarVento(clima.vento, uiState.unidadeVento)
                                    )
                                }
                            }
                        }
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Próximas 12 horas", style = MaterialTheme.typography.titleMedium)
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            items(uiState.horas) { hora ->
                                ChipHora(hora = hora, unidadeTemperatura = uiState.unidadeTemperatura)
                            }
                        }
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(onClick = aoAbrirPrevisao, modifier = Modifier.weight(1f)) {
                            Text("Previsão")
                        }
                        OutlinedButton(onClick = aoAbrirHistorico, modifier = Modifier.weight(1f)) {
                            Text("Histórico")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoClimaSecundaria(rotulo: String, valor: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = rotulo,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(valor, style = MaterialTheme.typography.titleMedium)
    }
}
