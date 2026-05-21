package com.example.projekt_mobilka.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.projekt_mobilka.MyApplication
import com.example.projekt_mobilka.model.Difficulty
import com.example.projekt_mobilka.viewmodel.MainViewModel
import com.example.projekt_mobilka.viewmodel.Screen
import com.example.projekt_mobilka.view.theme.Projekt_mobilkaTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = (application as MyApplication).repository
                return MainViewModel(repository) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Projekt_mobilkaTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (viewModel.currentScreen) {
                        is Screen.Initial -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                        is Screen.Registration -> {
                            RegistrationScreen(
                                onRegisterSuccess = { username, uri -> 
                                    viewModel.registerUser(username, uri)
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        is Screen.Game -> {
                            GameScreen(
                                onSettingsClick = { viewModel.navigateToSettings() },
                                onStartGameClick = { viewModel.startNewGame(it) },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        is Screen.Settings -> {
                            SettingsScreen(
                                username = viewModel.user.username,
                                profilePicturePath = viewModel.user.profilePicturePath,
                                wins = viewModel.user.wins,
                                losses = viewModel.user.losses,
                                onUsernameChange = { viewModel.updateUsername(it) },
                                onProfilePictureChange = { viewModel.updateProfilePicture(it) },
                                onBackClick = { viewModel.navigateToGame() },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        is Screen.Play -> {
                            PlayScreen(
                                gameState = viewModel.gameState,
                                onGuessSubmit = { viewModel.submitGuess(it) },
                                onGameEnd = { viewModel.navigateToGame() },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
