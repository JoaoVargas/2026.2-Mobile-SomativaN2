package com.example.somativan2.ui.screens.adicionar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.somativan2.data.catalogo.CidadeCatalogo
import com.example.somativan2.data.repository.ClimaRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AdicionarUiState(
    val query: String = "",
    val sugestoes: List<CidadeCatalogo> = emptyList(),
    val selecionada: CidadeCatalogo? = null,
    val erro: String? = null,
    val salvando: Boolean = false
)

@HiltViewModel
class AdicionarCidadeViewModel @Inject constructor(
    private val repository: ClimaRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AdicionarUiState())
    val uiState: StateFlow<AdicionarUiState> = _uiState.asStateFlow()

    fun onQueryChange(texto: String) {
        val sugestoes = if (texto.isBlank()) emptyList() else repository.buscarNoCatalogo(texto)
        _uiState.value = _uiState.value.copy(
            query = texto,
            sugestoes = sugestoes,
            selecionada = null,
            erro = null
        )
    }

    fun onSugestaoSelecionada(cidade: CidadeCatalogo) {
        _uiState.value = _uiState.value.copy(
            query = cidade.nome,
            selecionada = cidade,
            sugestoes = emptyList()
        )
    }

    fun adicionar(onSucesso: () -> Unit) {
        val selecionada = _uiState.value.selecionada ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(salvando = true)
            val resultado = repository.adicionarCidade(selecionada.nome)
            _uiState.value = _uiState.value.copy(salvando = false)
            resultado.fold(
                onSuccess = { onSucesso() },
                onFailure = { erro -> _uiState.value = _uiState.value.copy(erro = erro.message) }
            )
        }
    }
}
