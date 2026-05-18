package com.example.projekt_mobilka.viewmodel

import android.app.Application
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.projekt_mobilka.MyApplication
import com.example.projekt_mobilka.model.User
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class Screen {
    object Registration : Screen()
    object Game : Screen()
    object Settings : Screen()
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = (application as MyApplication).repository

    val user: StateFlow<User?> = repository.user
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    var currentScreen by mutableStateOf<Screen>(Screen.Registration)
        private set

    init {
        // If user already exists, skip registration
        viewModelScope.launch {
            repository.user.collect {
                if (it != null && currentScreen == Screen.Registration) {
                    currentScreen = Screen.Game
                }
            }
        }
    }

    fun registerUser(username: String) {
        viewModelScope.launch {
            repository.updateUsername(username)
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

    fun navigateToSettings() {
        currentScreen = Screen.Settings
    }
}
