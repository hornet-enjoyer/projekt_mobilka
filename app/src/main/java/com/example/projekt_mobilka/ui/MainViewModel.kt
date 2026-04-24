package com.example.projekt_mobilka.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.projekt_mobilka.model.User

sealed class Screen {
    object Registration : Screen()
    object Game : Screen()
    object Settings : Screen()
}

class MainViewModel : ViewModel() {
    var currentScreen by mutableStateOf<Screen>(Screen.Registration)
        private set

    var user by mutableStateOf(User())
        private set

    fun registerUser(username: String) {
        user = user.copy(username = username)
        navigateToGame()
    }

    fun updateUsername(newUsername: String) {
        user = user.copy(username = newUsername)
    }

    fun navigateToGame() {
        currentScreen = Screen.Game
    }

    fun navigateToSettings() {
        currentScreen = Screen.Settings
    }
}
