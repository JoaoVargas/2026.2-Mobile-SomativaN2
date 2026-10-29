package com.example.somativan2.data.repository

import com.example.somativan2.data.catalogo.CidadeCatalogo
import com.example.somativan2.data.catalogo.CidadeCatalogoLocal
import com.example.somativan2.data.local.CidadeDao
import com.example.somativan2.data.local.CidadeEntity
import com.example.somativan2.data.local.PreferenciaDao
import com.example.somativan2.data.local.PreferenciaEntity
import com.example.somativan2.data.model.ClimaHora
import com.example.somativan2.data.model.ResumoDia
import com.example.somativan2.data.model.ResumoMes
import com.example.somativan2.data.simulador.SimuladorClima
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class ClimaRepositoryImpl @Inject constructor(
    private val cidadeDao: CidadeDao,
    private val preferenciaDao: PreferenciaDao,
    private val catalogo: CidadeCatalogoLocal
) : ClimaRepository {

    override fun listarCidades(): Flow<List<CidadeEntity>> = cidadeDao.listarTodas()

    override suspend fun adicionarCidade(nome: String): Result<Int> {
        val nomeOficial = catalogo.resolverNomeOficial(nome)
            ?: return Result.failure(IllegalArgumentException("Cidade não encontrada no catálogo"))
        cidadeDao.buscarPorNome(nomeOficial)?.let {
            return Result.failure(IllegalStateException("Essa cidade já está nos favoritos"))
        }
        val id = cidadeDao.inserir(CidadeEntity(nome = nomeOficial)).toInt()
        return Result.success(id)
    }

    override suspend fun removerCidade(cidade: CidadeEntity) {
        cidadeDao.excluir(cidade)
        val preferencia = preferenciaDao.observar().first() ?: return
        if (preferencia.cidadeAtualId == cidade.id) {
            val restantes = cidadeDao.listarTodas().first()
            preferenciaDao.salvar(preferencia.copy(cidadeAtualId = restantes.firstOrNull()?.id))
        }
    }

    override fun buscarNoCatalogo(query: String): List<CidadeCatalogo> = catalogo.buscar(query)

    override fun observarPreferencia(): Flow<PreferenciaEntity?> = preferenciaDao.observar()

    override suspend fun salvarUnidadeTemperatura(unidade: String) {
        val preferencia = preferenciaDao.observar().first() ?: PreferenciaEntity()
        preferenciaDao.salvar(preferencia.copy(unidade = unidade))
    }

    override suspend fun salvarUnidadeVento(unidade: String) {
        val preferencia = preferenciaDao.observar().first() ?: PreferenciaEntity()
        preferenciaDao.salvar(preferencia.copy(unidadeVento = unidade))
    }

    override suspend fun definirCidadeAtual(id: Int) {
        val preferencia = preferenciaDao.observar().first() ?: PreferenciaEntity()
        preferenciaDao.salvar(preferencia.copy(cidadeAtualId = id))
    }

    override fun observarCidadeAtual(): Flow<CidadeEntity?> =
        preferenciaDao.observar().flatMapLatest { preferencia ->
            val id = preferencia?.cidadeAtualId
            if (id == null) flowOf(null) else cidadeDao.buscarPorId(id)
        }

    override fun gerarClimaAtual(nome: String): ClimaHora = SimuladorClima.gerarHora(nome, 0)

    override fun gerarHorasApartirDe(nome: String, offsetInicial: Long, quantidade: Int): List<ClimaHora> =
        SimuladorClima.gerarHorasApartirDe(nome, offsetInicial, quantidade)

    override fun gerarProximosDias(nome: String, dias: Int): List<ResumoDia> =
        SimuladorClima.gerarProximosDias(nome, dias)

    override fun gerarUltimosMeses(nome: String): List<ResumoMes> =
        SimuladorClima.gerarUltimosMeses(nome)

    override fun gerarDiasDoMes(nome: String, mesOffset: Int): List<ResumoDia> =
        SimuladorClima.gerarDiasDoMes(nome, mesOffset)

    override fun gerarHorasDoDia(nome: String, diaOffsetAbsoluto: Int): List<ClimaHora> =
        SimuladorClima.gerarHorasDoDia(nome, diaOffsetAbsoluto)
}
