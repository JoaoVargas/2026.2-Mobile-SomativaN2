package com.example.somativan2.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somativan2.data.local.CidadeEntity
import com.example.somativan2.data.model.ClimaHora
import com.example.somativan2.data.repository.ClimaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val HORAS_EXIBIDAS = 12

data class HomeUiState(
    val carregando: Boolean = true,
    val favoritas: List<CidadeEntity> = emptyList(),
    val cidadeAtual: CidadeEntity? = null,
    val clima: ClimaHora? = null,
    val horas: List<ClimaHora> = emptyList(),
    val unidadeTemperatura: String = "C",
    val unidadeVento: String = "KMH"
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: ClimaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            combine(
                repository.listarCidades(),
                repository.observarCidadeAtual(),
                repository.observarPreferencia()
            ) { favoritas, cidadeAtual, preferencia ->
                val clima = cidadeAtual?.let { repository.gerarClimaAtual(it.nome) }
                val horas = cidadeAtual?.let {
                    repository.gerarHorasApartirDe(it.nome, 0, HORAS_EXIBIDAS)
                } ?: emptyList()
                HomeUiState(
                    carregando = false,
                    favoritas = favoritas,
                    cidadeAtual = cidadeAtual,
                    clima = clima,
                    horas = horas,
                    unidadeTemperatura = preferencia?.unidade ?: "C",
                    unidadeVento = preferencia?.unidadeVento ?: "KMH"
                )
            }.collect { novoEstado -> _uiState.value = novoEstado }
        }
    }

    fun selecionarCidadeAtual(id: Int) {
        viewModelScope.launch { repository.definirCidadeAtual(id) }
    }
}
