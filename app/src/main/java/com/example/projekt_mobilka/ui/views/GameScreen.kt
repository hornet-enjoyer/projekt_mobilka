package com.example.projekt_mobilka.ui.views

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekt_mobilka.ui.theme.Projekt_mobilkaTheme

@Composable
fun GameScreen(
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf("Łatwy") }
    val difficulties = listOf("Łatwy", "Średni", "Trudny")

    val gameHistory = listOf(
        GameResult("1.01", true),
        GameResult("2.01", false),
        GameResult("3.01", true),
        GameResult("4.01", false),
        GameResult("5.01", false)
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Settings Icon
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Recent Games List
        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(280.dp)
                .clip(RoundedCornerShape(24.dp))
                .background(Color.LightGray.copy(alpha = 0.2f))
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(gameHistory) { result ->
                    GameHistoryItem(result)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Start Game Button
        Button(
            onClick = { /* TODO: Start Game */ },
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7D67AE)
            )
        ) {
            Text(
                text = "Rozpocznij grę",
                fontSize = 18.sp,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Difficulty Selector
        Row(
            modifier = Modifier.fillMaxWidth(0.8f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Poziom trudności",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Box {
                Surface(
                    modifier = Modifier
                        .width(120.dp)
                        .clickable { expanded = true }
                        .clip(RoundedCornerShape(8.dp)),
                    color = Color(0xFFEADDFF)
                ) {
                    Text(
                        text = selectedDifficulty,
                        modifier = Modifier.padding(8.dp),
                        color = Color.Black,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(120.dp) // Match the Surface width
                        .background(Color(0xFFF3EDF7))
                ) {
                    difficulties.forEach { difficulty ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = difficulty,
                                    modifier = Modifier.fillMaxWidth(),
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center
                                )
                            },
                            onClick = {
                                selectedDifficulty = difficulty
                                expanded = false
                            }
                        )
                    }
                }
            }
        }
    }
}

data class GameResult(val date: String, val won: Boolean)

@Composable
fun GameHistoryItem(result: GameResult) {
    val backgroundColor = if (result.won) Color(0xFFC8E6C9) else Color(0xFFFFCDD2)
    val text = if (result.won) "${result.date} - Wygrana" else "${result.date} - Przegrana"

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp)
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color.DarkGray
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    Projekt_mobilkaTheme {
        GameScreen(onSettingsClick = {})
    }
}
