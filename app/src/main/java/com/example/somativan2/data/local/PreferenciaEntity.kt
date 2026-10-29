package com.example.somativan2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

// Tabela de uma única linha (id sempre 0) guardando as preferências do app.
@Entity(tableName = "preferencias")
data class PreferenciaEntity(
    @PrimaryKey val id: Int = 0,
    val unidade: String = "C",
    val unidadeVento: String = "KMH",
    val cidadeAtualId: Int? = null
)
