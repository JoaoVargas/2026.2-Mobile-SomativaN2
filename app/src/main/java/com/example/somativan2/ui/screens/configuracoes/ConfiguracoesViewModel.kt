package com.example.somativan2.ui.screens.configuracoes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somativan2.data.repository.ClimaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ConfiguracoesUiState(
    val unidadeTemperatura: String = "C",
    val unidadeVento: String = "KMH"
)

@HiltViewModel
class ConfiguracoesViewModel @Inject constructor(
    private val repository: ClimaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfiguracoesUiState())
    val uiState: StateFlow<ConfiguracoesUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observarPreferencia().collect { preferencia ->
                _uiState.value = ConfiguracoesUiState(
                    unidadeTemperatura = preferencia?.unidade ?: "C",
                    unidadeVento = preferencia?.unidadeVento ?: "KMH"
                )
            }
        }
    }

    fun selecionarUnidadeTemperatura(unidade: String) {
        viewModelScope.launch { repository.salvarUnidadeTemperatura(unidade) }
    }

    fun selecionarUnidadeVento(unidade: String) {
        viewModelScope.launch { repository.salvarUnidadeVento(unidade) }
    }
}
