package com.example.somativan2.data.repository

import com.example.somativan2.data.catalogo.CidadeCatalogo
import com.example.somativan2.data.local.CidadeEntity
import com.example.somativan2.data.local.PreferenciaEntity
import com.example.somativan2.data.model.ClimaHora
import com.example.somativan2.data.model.ResumoDia
import com.example.somativan2.data.model.ResumoMes
import kotlinx.coroutines.flow.Flow

interface ClimaRepository {

    fun listarCidades(): Flow<List<CidadeEntity>>
    suspend fun adicionarCidade(nome: String): Result<Int>
    suspend fun removerCidade(cidade: CidadeEntity)

    fun buscarNoCatalogo(query: String): List<CidadeCatalogo>

    fun observarPreferencia(): Flow<PreferenciaEntity?>
    suspend fun salvarUnidadeTemperatura(unidade: String)
    suspend fun salvarUnidadeVento(unidade: String)
    suspend fun definirCidadeAtual(id: Int)
    fun observarCidadeAtual(): Flow<CidadeEntity?>

    fun gerarClimaAtual(nome: String): ClimaHora
    fun gerarHorasApartirDe(nome: String, offsetInicial: Long, quantidade: Int): List<ClimaHora>
    fun gerarProximosDias(nome: String, dias: Int = 7): List<ResumoDia>
    fun gerarUltimosMeses(nome: String): List<ResumoMes>
    fun gerarDiasDoMes(nome: String, mesOffset: Int): List<ResumoDia>
    fun gerarHorasDoDia(nome: String, diaOffsetAbsoluto: Int): List<ClimaHora>
}
