package com.example.somativan2.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PreferenciaDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun salvar(preferencia: PreferenciaEntity)

    @Query("SELECT * FROM preferencias WHERE id = 0")
    fun observar(): Flow<PreferenciaEntity?>
}
