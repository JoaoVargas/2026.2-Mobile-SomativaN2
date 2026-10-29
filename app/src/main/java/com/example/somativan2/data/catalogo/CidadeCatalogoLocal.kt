package com.example.somativan2.data.catalogo

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.json.JSONArray
import java.text.Normalizer
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CidadeCatalogoLocal @Inject constructor(
    @ApplicationContext context: Context
) {
    private val cidades: List<CidadeCatalogo> by lazy {
        val texto = context.assets.open("cidades_catalogo.json").bufferedReader().use { it.readText() }
        val array = JSONArray(texto)
        (0 until array.length()).map { i ->
            val objeto = array.getJSONObject(i)
            CidadeCatalogo(
                nome = objeto.getString("nome"),
                estadoOuPais = objeto.getString("estadoOuPais")
            )
        }
    }

    fun buscar(query: String): List<CidadeCatalogo> {
        if (query.isBlank()) return emptyList()
        val normalizada = normalizar(query)
        return cidades
            .filter { normalizar(it.nome).contains(normalizada) }
            .sortedBy { it.nome }
            .take(20)
    }

    fun resolverNomeOficial(nome: String): String? {
        val normalizada = normalizar(nome)
        return cidades.firstOrNull { normalizar(it.nome) == normalizada }?.nome
    }

    private fun normalizar(texto: String): String {
        val semAcentos = Normalizer.normalize(texto, Normalizer.Form.NFD)
            .replace(Regex("[\\p{InCombiningDiacriticalMarks}]"), "")
        return semAcentos.trim().lowercase()
    }
}
