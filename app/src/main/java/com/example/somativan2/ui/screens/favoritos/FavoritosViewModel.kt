package com.example.somativan2.ui.screens.favoritos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somativan2.data.local.CidadeEntity
import com.example.somativan2.data.repository.ClimaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CidadeFavorita(
    val cidade: CidadeEntity,
    val resumo: String
)

data class FavoritosUiState(
    val cidades: List<CidadeFavorita> = emptyList(),
    val cidadeParaExcluir: CidadeEntity? = null
)

@HiltViewModel
class FavoritosViewModel @Inject constructor(
    private val repository: ClimaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritosUiState())
    val uiState: StateFlow<FavoritosUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.listarCidades().collect { cidades ->
                val resumos = cidades.map { cidade ->
                    val clima = repository.gerarClimaAtual(cidade.nome)
                    CidadeFavorita(cidade, "${clima.condicao} - ${clima.temperatura}°C")
                }
                _uiState.value = _uiState.value.copy(cidades = resumos)
            }
        }
    }

    fun confirmarExclusao(cidade: CidadeEntity) {
        _uiState.value = _uiState.value.copy(cidadeParaExcluir = cidade)
    }

    fun cancelarExclusao() {
        _uiState.value = _uiState.value.copy(cidadeParaExcluir = null)
    }

    fun excluir() {
        val cidade = _uiState.value.cidadeParaExcluir ?: return
        viewModelScope.launch {
            repository.removerCidade(cidade)
            _uiState.value = _uiState.value.copy(cidadeParaExcluir = null)
        }
    }
}
