package com.example.somativan2.ui.screens.historico

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somativan2.data.local.CidadeEntity
import com.example.somativan2.data.model.ClimaHora
import com.example.somativan2.data.model.ResumoDia
import com.example.somativan2.data.model.ResumoMes
import com.example.somativan2.data.repository.ClimaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class NivelHistorico { MESES, DIAS, HORAS }

data class HistoricoUiState(
    val nivel: NivelHistorico = NivelHistorico.MESES,
    val nomeCidade: String = "",
    val meses: List<ResumoMes> = emptyList(),
    val mesSelecionado: ResumoMes? = null,
    val dias: List<ResumoDia> = emptyList(),
    val diaSelecionado: ResumoDia? = null,
    val horas: List<ClimaHora> = emptyList(),
    val unidadeTemperatura: String = "C"
)

@HiltViewModel
class HistoricoViewModel @Inject constructor(
    private val repository: ClimaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoricoUiState())
    val uiState: StateFlow<HistoricoUiState> = _uiState.asStateFlow()

    private var cidadeAtual: CidadeEntity? = null

    init {
        viewModelScope.launch {
            repository.observarCidadeAtual().collect { cidade ->
                cidadeAtual = cidade
                val unidade = repository.observarPreferencia().first()?.unidade ?: "C"
                _uiState.value = if (cidade == null) {
                    HistoricoUiState(unidadeTemperatura = unidade)
                } else {
                    HistoricoUiState(
                        nomeCidade = cidade.nome,
                        meses = repository.gerarUltimosMeses(cidade.nome),
                        unidadeTemperatura = unidade
                    )
                }
            }
        }
    }

    fun selecionarMes(mes: ResumoMes) {
        val cidade = cidadeAtual ?: return
        _uiState.value = _uiState.value.copy(
            nivel = NivelHistorico.DIAS,
            mesSelecionado = mes,
            dias = repository.gerarDiasDoMes(cidade.nome, mes.mesOffset)
        )
    }

    fun selecionarDia(dia: ResumoDia) {
        val cidade = cidadeAtual ?: return
        _uiState.value = _uiState.value.copy(
            nivel = NivelHistorico.HORAS,
            diaSelecionado = dia,
            horas = repository.gerarHorasDoDia(cidade.nome, dia.diaOffset)
        )
    }

    /** Sobe um nível de navegação interno; retorna false quando já está no nível raiz (meses). */
    fun voltarNivel(): Boolean {
        val estadoAtual = _uiState.value
        return when (estadoAtual.nivel) {
            NivelHistorico.HORAS -> {
                _uiState.value = estadoAtual.copy(nivel = NivelHistorico.DIAS, diaSelecionado = null, horas = emptyList())
                true
            }
            NivelHistorico.DIAS -> {
                _uiState.value = estadoAtual.copy(nivel = NivelHistorico.MESES, mesSelecionado = null, dias = emptyList())
                true
            }
            NivelHistorico.MESES -> false
        }
    }
}
