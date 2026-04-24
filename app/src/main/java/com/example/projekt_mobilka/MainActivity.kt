package com.example.projekt_mobilka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.projekt_mobilka.ui.MainViewModel
import com.example.projekt_mobilka.ui.Screen
import com.example.projekt_mobilka.ui.views.RegistrationScreen
import com.example.projekt_mobilka.ui.views.GameScreen
import com.example.projekt_mobilka.ui.views.SettingsScreen
import com.example.projekt_mobilka.ui.theme.Projekt_mobilkaTheme

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Projekt_mobilkaTheme {
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
                                username = viewModel.user.username,
                                onUsernameChange = { viewModel.updateUsername(it) },
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
