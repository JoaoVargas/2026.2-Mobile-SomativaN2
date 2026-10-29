package com.example.somativan2.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CidadeDao {

    @Insert
    suspend fun inserir(cidade: CidadeEntity): Long

    @Delete
    suspend fun excluir(cidade: CidadeEntity)

    @Query("SELECT * FROM cidades ORDER BY nome")
    fun listarTodas(): Flow<List<CidadeEntity>>

    @Query("SELECT * FROM cidades WHERE id = :id")
    fun buscarPorId(id: Int): Flow<CidadeEntity?>

    @Query("SELECT * FROM cidades WHERE nome = :nome LIMIT 1")
    suspend fun buscarPorNome(nome: String): CidadeEntity?
}
