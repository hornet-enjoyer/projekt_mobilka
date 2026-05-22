package com.example.projekt_mobilka.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameResultDao {
    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<GameResultEntity>>

    @Insert
    suspend fun insertResult(result: GameResultEntity)
}
