package com.example.somativan2.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.somativan2.data.local.CidadeEntity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeletorCidadeAtual(
    cidades: List<CidadeEntity>,
    selecionada: CidadeEntity?,
    aoSelecionar: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expandido by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expandido,
        onExpandedChange = { expandido = it },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selecionada?.nome ?: "Selecione uma cidade",
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandido) },
            modifier = Modifier.menuAnchor().fillMaxWidth()
        )
        ExposedDropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            cidades.forEach { cidade ->
                DropdownMenuItem(
                    text = { Text(cidade.nome) },
                    onClick = {
                        aoSelecionar(cidade.id)
                        expandido = false
                    }
                )
            }
        }
    }
}
