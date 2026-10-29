package com.example.somativan2.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.somativan2.data.model.ClimaHora
import com.example.somativan2.data.simulador.ConversorUnidades
import com.example.somativan2.data.simulador.FormatadorTempo

@Composable
fun ChipHora(
    hora: ClimaHora,
    unidadeTemperatura: String,
    modifier: Modifier = Modifier
) {
    val destaque = hora.timestampOffsetHoras == 0L

    Card(
        modifier = modifier.width(78.dp),
        colors = if (destaque) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        } else {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 14.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = FormatadorTempo.rotuloHoraRelativa(hora.timestampOffsetHoras),
                style = MaterialTheme.typography.labelMedium
            )
            Text(
                text = ConversorUnidades.formatarTemperatura(hora.temperatura, unidadeTemperatura),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = hora.condicao,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
