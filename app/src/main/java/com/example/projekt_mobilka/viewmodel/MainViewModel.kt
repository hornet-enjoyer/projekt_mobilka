package com.example.projekt_mobilka.viewmodel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt_mobilka.model.*
import com.example.projekt_mobilka.network.WeatherApi
import kotlinx.coroutines.launch
import kotlin.random.Random

sealed class Screen {
    object Initial : Screen()
    object Registration : Screen()
    object Game : Screen()
    object Settings : Screen()
    object Play : Screen()
}

data class GameState(
    val targetCapital: Capital? = null,
    val targetWeather: CurrentWeather? = null,
    val guesses: List<Guess> = emptyList(),
    val lives: Int = 0,
    val isGameOver: Boolean = false,
    val isGameWon: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MainViewModel(private val repository: UserRepository) : ViewModel() {
    var currentScreen by mutableStateOf<Screen>(Screen.Initial)
        private set

    var user by mutableStateOf(User())
        private set

    var gameHistory by mutableStateOf<List<GameResultEntity>>(emptyList())
        private set

    var gameState by mutableStateOf(GameState())
        private set

    private val weatherApi = WeatherApi.create()
    
    init {
        viewModelScope.launch {
            repository.user.collect { dbUser ->
                if (dbUser != null) {
                    user = dbUser
                    // If we were in Initial state and found a user, go to Game
                    // If no user found (username blank), go to Registration
                    if (currentScreen is Screen.Initial) {
                        if (dbUser.username.isNotBlank()) {
                            navigateToGame()
                        } else {
                            navigateToRegistration()
                        }
                    }
                } else if (currentScreen is Screen.Initial) {
                    navigateToRegistration()
                }
            }
        }
        viewModelScope.launch {
            repository.gameHistory.collect { history ->
                gameHistory = history
            }
        }
    }

    fun registerUser(username: String, profilePictureUri: Uri? = null) {
        viewModelScope.launch {
            repository.updateUsername(username)
            profilePictureUri?.let { repository.updateProfilePicture(it) }
            navigateToGame()
        }
    }

    fun updateUsername(newUsername: String) {
        viewModelScope.launch {
            repository.updateUsername(newUsername)
        }
    }
    
    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch {
            repository.updateProfilePicture(uri)
        }
    }

    fun navigateToGame() {
        currentScreen = Screen.Game
    }

    fun navigateToRegistration() {
        currentScreen = Screen.Registration
    }

    fun navigateToSettings() {
        currentScreen = Screen.Settings
    }

    fun startNewGame(difficulty: Difficulty = Difficulty.EASY) {
        // Reset state for new game
        gameState = GameState(isLoading = true, lives = difficulty.lives)
        currentScreen = Screen.Play
        
        viewModelScope.launch {
            try {
                val randomCapital = capitalCities[Random.nextInt(capitalCities.size)]
                val response = weatherApi.getWeather(randomCapital.latitude, randomCapital.longitude)
                gameState = gameState.copy(
                    targetCapital = randomCapital,
                    targetWeather = response.current,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                gameState = gameState.copy(
                    isLoading = false,
                    error = "Problem z połączeniem. Sprawdź internet."
                )
            }
        }
    }

    fun submitGuess(cityName: String) {
        val targetCapital = gameState.targetCapital ?: return
        val targetWeather = gameState.targetWeather ?: return
        
        val guessedCapital = capitalCities.find { it.name.equals(cityName, ignoreCase = true) }
        
        if (guessedCapital == null) {
            gameState = gameState.copy(error = "Nie znaleziono miasta w bazie.")
            return
        }

        viewModelScope.launch {
            try {
                val response = weatherApi.getWeather(guessedCapital.latitude, guessedCapital.longitude)
                val guessedWeather = response.current
                
                val guess = Guess(
                    cityName = guessedCapital.name,
                    tempStatus = compare(guessedWeather.temperature, targetWeather.temperature),
                    windStatus = compare(guessedWeather.windSpeed, targetWeather.windSpeed),
                    humidityStatus = compare(guessedWeather.humidity.toDouble(), targetWeather.humidity.toDouble())
                )
                
                val newGuesses = listOf(guess) + gameState.guesses
                val won = guessedCapital.name.equals(targetCapital.name, ignoreCase = true)
                
                var newLives = gameState.lives
                if (!won) {
                    newLives--
                }

                if (won) {
                    repository.incrementWins(targetCapital.name)
                } else if (newLives <= 0) {
                    repository.incrementLosses(targetCapital.name)
                }

                gameState = gameState.copy(
                    guesses = newGuesses,
                    lives = newLives,
                    isGameOver = won || newLives <= 0,
                    isGameWon = won,
                    error = null
                )
            } catch (e: Exception) {
                gameState = gameState.copy(error = "Błąd połączenia podczas sprawdzania.")
            }
        }
    }

    private fun compare(current: Double, target: Double): ComparisonStatus {
        return when {
            Math.abs(current - target) < 1.0 -> ComparisonStatus.CORRECT
            current < target -> ComparisonStatus.TOO_LOW
            else -> ComparisonStatus.TOO_HIGH
        }
    }
    
    fun resetError() {
        gameState = gameState.copy(error = null)
    }
}
