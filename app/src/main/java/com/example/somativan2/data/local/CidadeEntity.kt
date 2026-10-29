package com.example.somativan2.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cidades")
data class CidadeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nome: String
)
