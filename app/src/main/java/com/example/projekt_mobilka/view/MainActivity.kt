package com.example.projekt_mobilka.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.projekt_mobilka.viewmodel.MainViewModel
import com.example.projekt_mobilka.viewmodel.Screen
import com.example.projekt_mobilka.view.theme.Projekt_mobilkaTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Projekt_mobilkaTheme {
                val user by viewModel.user.collectAsState()
                
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    when (viewModel.currentScreen) {
                        is Screen.Registration -> {
                            RegistrationScreen(
                                onRegisterSuccess = { username -> 
                                    viewModel.registerUser(username) 
                                },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        is Screen.Game -> {
                            GameScreen(
                                onSettingsClick = { viewModel.navigateToSettings() },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                        is Screen.Settings -> {
                            SettingsScreen(
                                username = user?.username ?: "",
                                profilePicturePath = user?.profilePicturePath,
                                onUsernameChange = { viewModel.updateUsername(it) },
                                onProfilePictureChange = { viewModel.updateProfilePicture(it) },
                                onBackClick = { viewModel.navigateToGame() },
                                modifier = Modifier.padding(innerPadding)
                            )
                        }
                    }
                }
            }
        }
    }
}
