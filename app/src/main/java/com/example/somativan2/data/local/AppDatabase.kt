package com.example.somativan2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CidadeEntity::class, PreferenciaEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cidadeDao(): CidadeDao
    abstract fun preferenciaDao(): PreferenciaDao
}
