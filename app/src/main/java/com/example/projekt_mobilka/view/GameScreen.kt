package com.example.projekt_mobilka.view

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.projekt_mobilka.model.*
import com.example.projekt_mobilka.viewmodel.GameState
import com.example.projekt_mobilka.view.theme.Projekt_mobilkaTheme

@Preview(showBackground = true)
@Composable
fun GameScreenPlayPreview() {
    Projekt_mobilkaTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            PlayScreen(
                gameState = GameState(
                    lives = 5,
                    targetCapital = Capital("Warszawa", 52.2297, 21.0122),
                    targetWeather = CurrentWeather(7.0, 67, 10.0, 3),
                    guesses = listOf(
                        Guess("Madryt", ComparisonStatus.TOO_LOW, ComparisonStatus.CORRECT, ComparisonStatus.TOO_HIGH)
                    )
                ),
                onGuessSubmit = {},
                onGameEnd = {}
            )
        }
    }
}

@Composable
fun PlayScreen(
    gameState: GameState,
    onGuessSubmit: (String) -> Unit,
    onGameEnd: () -> Unit,
    modifier: Modifier = Modifier
) {
    var guessText by remember { mutableStateOf("") }

    if (gameState.isGameOver) {
        val title = if (gameState.isGameWon) "Wygrana!" else "Przegrana"
        val message = if (gameState.isGameWon) 
            "Brawo! Stolica to ${gameState.targetCapital?.name}" 
            else "Niestety, zabrakło żyć. Stolica to ${gameState.targetCapital?.name}"

        AlertDialog(
            onDismissRequest = onGameEnd,
            confirmButton = {
                Button(onClick = onGameEnd) {
                    Text("OK")
                }
            },
            title = { Text(title) },
            text = { Text(message) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Health points display
        Text(
            text = "Pozostałe życia: ${gameState.lives}",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = if (gameState.lives <= 2) Color.Red else Color.Unspecified,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Weather Info Card
        if (gameState.isLoading) {
            Box(modifier = Modifier.height(200.dp).fillMaxWidth(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (gameState.targetWeather != null) {
            WeatherHeaderCard(gameState.targetWeather)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color(0xFFE6E1E5)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                Text("Kraj / Stolica", fontSize = 12.sp, color = Color.Gray)
                Box(modifier = Modifier.fillMaxWidth()) {
                    if (guessText.isEmpty()) {
                        // DEBUG: To be removed later - shows the target city name
                        Text(
                            text = gameState.targetCapital?.name ?: "",
                            color = Color.Gray,
                            fontSize = 18.sp
                        )
                    }
                    BasicTextField(
                        value = guessText,
                        onValueChange = { guessText = it },
                        textStyle = androidx.compose.ui.text.TextStyle(fontSize = 18.sp),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            Box(
                modifier = Modifier
                    .width(64.dp)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                    .background(Color(0xFF6750A4))
                    .clickable { 
                        if (guessText.isNotBlank()) {
                            onGuessSubmit(guessText)
                            guessText = ""
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Submit",
                    tint = Color.White
                )
            }
        }

        if (gameState.error != null) {
            Text(
                text = gameState.error,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Guesses List
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(gameState.guesses) { guess ->
                GuessItemView(guess)
            }
        }
    }
}

@Composable
fun WeatherHeaderCard(weather: CurrentWeather) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFD0BCFF))
    ) {
        Row(modifier = Modifier.fillMaxSize()) {
            // Left part: Icon and condition
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = getWeatherIcon(weather.weatherCode),
                    contentDescription = null,
                    modifier = Modifier.size(80.dp),
                    tint = Color.White
                )
                Text(
                    text = getWeatherDescription(weather.weatherCode),
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            // Right part: Parameters
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
            ) {
                WeatherParamBox(
                    label = "Temperatura",
                    value = "${weather.temperature.toInt()}°C",
                    icon = Icons.Default.Thermostat,
                    backgroundColor = Color(0xFFB69DF8)
                )
                WeatherParamBox(
                    label = "Wiatr",
                    value = "${weather.windSpeed.toInt()} KPH",
                    icon = Icons.Default.Air,
                    backgroundColor = Color(0xFF9173D1)
                )
                WeatherParamBox(
                    label = "Wilgoć",
                    value = "${weather.humidity}%",
                    icon = Icons.Default.WaterDrop,
                    backgroundColor = Color(0xFF654E9B)
                )
            }
        }
    }
}

@Composable
fun ColumnScope.WeatherParamBox(label: String, value: String, icon: ImageVector, backgroundColor: Color) {
    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxWidth()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(label, fontSize = 10.sp, color = Color.White)
                Text(value, fontSize = 18.sp, color = Color.White, fontWeight = FontWeight.Bold)
            }
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

@Composable
fun GuessItemView(guess: Guess) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF49454F))
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Top part: City Name
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.4f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = guess.cityName,
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // Bottom part: Comparisons in a Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.6f)
            ) {
                ComparisonBox("Temperatura", guess.tempStatus)
                ComparisonBox("Wiatr", guess.windStatus)
                ComparisonBox("Wilgoć", guess.humidityStatus)
            }
        }
    }
}

@Composable
fun RowScope.ComparisonBox(label: String, status: ComparisonStatus) {
    val backgroundColor = if (status == ComparisonStatus.CORRECT) Color(0xFF609466) else Color(0xFF8F4C52)
    val icon = when (status) {
        ComparisonStatus.CORRECT -> Icons.Default.Check
        ComparisonStatus.TOO_HIGH -> Icons.Default.KeyboardArrowUp
        ComparisonStatus.TOO_LOW -> Icons.Default.KeyboardArrowDown
    }
    val statusText = when (status) {
        ComparisonStatus.CORRECT -> "OK"
        ComparisonStatus.TOO_HIGH -> "Za wysoka"
        ComparisonStatus.TOO_LOW -> "Za niska"
    }

    Box(
        modifier = Modifier
            .weight(1f)
            .fillMaxHeight()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, color = Color.White)
            Text(statusText, fontSize = 14.sp, color = Color.White, fontWeight = FontWeight.Bold)
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
        }
    }
}

fun getWeatherDescription(code: Int): String {
    return when (code) {
        0 -> "Bezchmurnie"
        1, 2, 3 -> "Częściowe zachmurzenie"
        45, 48 -> "Mgła"
        51, 53, 55 -> "Mżawka"
        61, 63, 65 -> "Deszcz"
        71, 73, 75 -> "Śnieg"
        80, 81, 82 -> "Ulewa"
        95 -> "Burza"
        else -> "Pochmurnie"
    }
}

fun getWeatherIcon(code: Int): ImageVector {
    return when (code) {
        0 -> Icons.Default.WbSunny
        1, 2, 3 -> Icons.Default.CloudQueue
        45, 48 -> Icons.Default.Cloud
        51, 53, 55 -> Icons.Default.Grain
        61, 63, 65 -> Icons.Default.Umbrella
        71, 73, 75 -> Icons.Default.AcUnit
        80, 81, 82 -> Icons.Default.Grain
        95 -> Icons.Default.Thunderstorm
        else -> Icons.Default.Cloud
    }
}
