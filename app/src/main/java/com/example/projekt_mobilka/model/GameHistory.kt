package com.example.projekt_mobilka.model

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "game_history")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val timestamp: Long,
    val won: Boolean,
    val cityName: String = ""
)

@Dao
interface GameResultDao {
    @Query("SELECT * FROM game_history ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<GameResultEntity>>

    @Insert
    suspend fun insertResult(result: GameResultEntity)
}
