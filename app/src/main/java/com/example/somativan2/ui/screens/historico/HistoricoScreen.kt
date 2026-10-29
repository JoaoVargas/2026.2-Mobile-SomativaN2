package com.example.somativan2.ui.screens.historico

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.somativan2.data.simulador.ConversorUnidades
import com.example.somativan2.data.simulador.FormatadorTempo
import com.example.somativan2.ui.components.TopBarSimples

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoricoScreen(
    aoVoltar: () -> Unit,
    viewModel: HistoricoViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopBarSimples(
                titulo = if (uiState.nomeCidade.isEmpty()) "Histórico" else "Histórico - ${uiState.nomeCidade}",
                aoVoltar = { if (!viewModel.voltarNivel()) aoVoltar() }
            )
        }
    ) { innerPadding ->
        when {
            uiState.nomeCidade.isEmpty() -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Selecione uma cidade atual na Home primeiro.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )
                }
            }

            uiState.nivel == NivelHistorico.MESES -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.meses) { mes ->
                        ItemHistorico(
                            titulo = mes.nome,
                            subtitulo = null,
                            faixaTemperatura = "${ConversorUnidades.formatarTemperatura(mes.temperaturaMediaMin, uiState.unidadeTemperatura)} / " +
                                ConversorUnidades.formatarTemperatura(mes.temperaturaMediaMax, uiState.unidadeTemperatura),
                            aoClicar = { viewModel.selecionarMes(mes) }
                        )
                    }
                }
            }

            uiState.nivel == NivelHistorico.DIAS -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.dias) { dia ->
                        ItemHistorico(
                            titulo = "Dia ${dia.diaDoMes}",
                            subtitulo = dia.condicaoPredominante,
                            faixaTemperatura = "${ConversorUnidades.formatarTemperatura(dia.temperaturaMin, uiState.unidadeTemperatura)} / " +
                                ConversorUnidades.formatarTemperatura(dia.temperaturaMax, uiState.unidadeTemperatura),
                            aoClicar = { viewModel.selecionarDia(dia) }
                        )
                    }
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.padding(innerPadding),
                    contentPadding = PaddingValues(vertical = 8.dp)
                ) {
                    items(uiState.horas) { hora ->
                        ItemHistorico(
                            titulo = FormatadorTempo.rotuloHoraAbsoluta(hora.timestampOffsetHoras),
                            subtitulo = hora.condicao,
                            faixaTemperatura = ConversorUnidades.formatarTemperatura(hora.temperatura, uiState.unidadeTemperatura),
                            aoClicar = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ItemHistorico(
    titulo: String,
    subtitulo: String?,
    faixaTemperatura: String,
    aoClicar: (() -> Unit)?
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .let { if (aoClicar != null) it.clickable(onClick = aoClicar) else it }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(titulo, style = MaterialTheme.typography.titleMedium)
                subtitulo?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = faixaTemperatura,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            if (aoClicar != null) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .padding(start = 4.dp)
                        .width(20.dp)
                )
            }
        }
    }
}
