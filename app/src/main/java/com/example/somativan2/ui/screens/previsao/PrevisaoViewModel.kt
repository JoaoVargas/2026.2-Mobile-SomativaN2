package com.example.somativan2.ui.screens.previsao

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somativan2.data.model.ResumoDia
import com.example.somativan2.data.repository.ClimaRepository
import com.example.somativan2.data.simulador.FormatadorTempo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DiaPrevisao(val rotulo: String, val resumo: ResumoDia)

data class PrevisaoUiState(
    val nomeCidade: String = "",
    val dias: List<DiaPrevisao> = emptyList(),
    val unidadeTemperatura: String = "C",
    val carregando: Boolean = true
)

@HiltViewModel
class PrevisaoViewModel @Inject constructor(
    private val repository: ClimaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PrevisaoUiState())
    val uiState: StateFlow<PrevisaoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.observarCidadeAtual(),
                repository.observarPreferencia()
            ) { cidade, preferencia ->
                if (cidade == null) {
                    PrevisaoUiState(carregando = false)
                } else {
                    val dias = repository.gerarProximosDias(cidade.nome, 7).map { resumo ->
                        DiaPrevisao(rotulo = FormatadorTempo.rotuloDia(resumo.diaOffset), resumo = resumo)
                    }
                    PrevisaoUiState(
                        nomeCidade = cidade.nome,
                        dias = dias,
                        unidadeTemperatura = preferencia?.unidade ?: "C",
                        carregando = false
                    )
                }
            }.collect { novoEstado -> _uiState.value = novoEstado }
        }
    }
}
