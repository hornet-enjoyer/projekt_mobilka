package com.example.projekt_mobilka.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

sealed class Screen {
    object Registration : Screen()
    object Game : Screen()
}

class MainViewModel : ViewModel() {
    var currentScreen by mutableStateOf<Screen>(Screen.Registration)
        private set

    fun navigateToGame() {
        currentScreen = Screen.Game
    }

    fun navigateToRegistration() {
        currentScreen = Screen.Registration
    }
}
