package com.example.projekt_mobilka.view

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import android.widget.VideoView
import androidx.compose.ui.viewinterop.AndroidView
import com.example.projekt_mobilka.model.Difficulty
import com.example.projekt_mobilka.model.GameResultEntity
import com.example.projekt_mobilka.view.theme.Projekt_mobilkaTheme
import kotlinx.coroutines.launch
import android.media.MediaMetadataRetriever
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.geometry.Offset
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri

@Composable
fun GameScreen(
    gameHistory: List<GameResultEntity>,
    onSettingsClick: () -> Unit,
    onStartGameClick: (Difficulty) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    var showTutorial by remember { mutableStateOf(false) }
    var selectedDifficulty by remember { mutableStateOf("Łatwy") }
    val difficulties = listOf("Łatwy", "Średni", "Trudny")

    if (showTutorial) {
        TutorialDialog(onDismiss = { showTutorial = false })
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Icons (Settings and Tutorial)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings",
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }

            IconButton(onClick = { showTutorial = true }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Help,
                    contentDescription = "Tutorial",
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
                .border(1.dp, Color.Black, RoundedCornerShape(24.dp))
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
            onClick = { onStartGameClick(Difficulty.fromLabel(selectedDifficulty)) },
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
                        .width(120.dp)
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

@Composable
fun TutorialDialog(onDismiss: () -> Unit) {
    val pagerState = rememberPagerState(pageCount = { 2 })
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Zamknij")
            }
        },
        title = {
            Text(
                text = "Samouczek",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(450.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    TutorialPage(page)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation Arrows
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage - 1)
                            }
                        },
                        enabled = pagerState.currentPage > 0
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Poprzedni")
                    }

                    Text("Krok ${pagerState.currentPage + 1} z 2")

                    IconButton(
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(pagerState.currentPage + 1)
                            }
                        },
                        enabled = pagerState.currentPage < 1
                    ) {
                        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Następny")
                    }
                }
            }
        }
    )
}

@SuppressLint("DiscouragedApi")
@Composable
fun TutorialPage(pageIndex: Int) {
    if (pageIndex == 0) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Jak grać?",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            val instructions = listOf(
                "• To jest gra polegająca na odgadywaniu stolic na podstawie aktualnej pogody.",
                "• Wpisz nazwę miasta i sprawdź parametry pogody, aby zbliżyć się do celu.",
                "• Zwracaj uwagę na temperaturę, wiatr i wilgodność. Masz ograniczoną liczbę żyć!"
            )
            
            instructions.forEach { text ->
                Text(
                    text = text,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 12.dp)
                )
            }
        }
    } else {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Wideo instruktażowe",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            val context = androidx.compose.ui.platform.LocalContext.current
            val resId = context.resources.getIdentifier("tutorial", "raw", context.packageName)
            
            if (resId != 0) {
                val videoUri = "android.resource://${context.packageName}/$resId".toUri()

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    AndroidView(
                        factory = { ctx ->
                            VideoView(ctx).apply {
                                setVideoURI(videoUri)
                                setOnPreparedListener { mp ->
                                    mp.isLooping = true
                                    start()
                                }
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.Black.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Wideo 'tutorial.webm' nie zostało znalezione w res/raw",
                        color = Color.Black.copy(alpha = 0.5f),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun GameHistoryItem(result: GameResultEntity) {
    val statusText = if (result.won) "Wygrana" else "Przegrana"
    // Darker and less visible tints
    val overlayColor = if (result.won) Color(0xFF1B5E20).copy(alpha = 0.6f) else Color(0xFFB71C1C).copy(alpha = 0.6f)
    val formattedDate = formatTimestamp(result.timestamp)

    val cityImageUrl = when(result.cityName) {
        "Warszawa" -> "https://images.unsplash.com/photo-1519197924294-4ba991a11128?q=80&w=500&auto=format&fit=crop"
        "Berlin" -> "https://images.unsplash.com/photo-1560969184-10fe8719e047?q=80&w=500&auto=format&fit=crop"
        "Paryż" -> "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?q=80&w=500&auto=format&fit=crop"
        "Londyn" -> "https://images.unsplash.com/photo-1513635269975-59663e0ac1ad?q=80&w=500&auto=format&fit=crop"
        "Rzym" -> "https://images.unsplash.com/photo-1552832230-c0197dd311b5?q=80&w=500&auto=format&fit=crop"
        "Madryt" -> "https://images.unsplash.com/photo-1539037116277-4db20889f2d4?q=80&w=500&auto=format&fit=crop"
        "Praga" -> "https://images.unsplash.com/photo-1541849546-216549ae216d?q=80&w=500&auto=format&fit=crop"
        "Wiedeń" -> "https://images.unsplash.com/photo-1516550893923-42d28e5677af?q=80&w=500&auto=format&fit=crop"
        "Ateny" -> "https://images.unsplash.com/photo-1503152394-c571994fd383?q=80&w=500&auto=format&fit=crop"
        "Sztokholm" -> "https://images.unsplash.com/photo-1509356843151-3e7d96241e11?q=80&w=500&auto=format&fit=crop"
        "Oslo" -> "https://images.unsplash.com/photo-1513519245088-0e12902e5a38?q=80&w=500&auto=format&fit=crop"
        "Helsinki" -> "https://images.unsplash.com/photo-1513101514332-613d965e5f5f?q=80&w=500&auto=format&fit=crop"
        "Kopenhaga" -> "https://images.unsplash.com/photo-1513622470522-26c3c8a854bc?q=80&w=500&auto=format&fit=crop"
        "Amsterdam" -> "https://images.unsplash.com/photo-1512470876302-972faa2aa9a4?q=80&w=500&auto=format&fit=crop"
        "Bruksela" -> "https://images.unsplash.com/photo-1563132145-12154563cc3d?q=80&w=500&auto=format&fit=crop"
        "Budapeszt" -> "https://images.unsplash.com/photo-1551867633-194f125bddfa?q=80&w=500&auto=format&fit=crop"
        "Lizbona" -> "https://images.unsplash.com/photo-1585208798174-6cedd862bc9f?q=80&w=500&auto=format&fit=crop"
        "Dublin" -> "https://images.unsplash.com/photo-1549918838-316a4b8a1ec5?q=80&w=500&auto=format&fit=crop"
        "Reykjavík" -> "https://images.unsplash.com/photo-1504109586057-7a2ae83d1338?q=80&w=500&auto=format&fit=crop"
        "Tallinn" -> "https://images.unsplash.com/photo-1548671153-6036814e76c1?q=80&w=500&auto=format&fit=crop"
        "Ryga" -> "https://images.unsplash.com/photo-1563227812-0ea4c22e6cc8?q=80&w=500&auto=format&fit=crop"
        "Wilno" -> "https://images.unsplash.com/photo-1571217691275-3004b50c0587?q=80&w=500&auto=format&fit=crop"
        "Kijów" -> "https://images.unsplash.com/photo-1565552391206-89689f76a5b6?q=80&w=500&auto=format&fit=crop"
        "Bukareszt" -> "https://images.unsplash.com/photo-1549635031-6e3e566d5b03?q=80&w=500&auto=format&fit=crop"
        "Sofia" -> "https://images.unsplash.com/photo-1555138136-120019983411?q=80&w=500&auto=format&fit=crop"
        "Zagrzeb" -> "https://images.unsplash.com/photo-1578652670774-89c030d9237f?q=80&w=500&auto=format&fit=crop"
        "Belgrad" -> "https://images.unsplash.com/photo-1588612143093-690a6184a4f8?q=80&w=500&auto=format&fit=crop"
        else -> "https://images.unsplash.com/photo-1449034446853-66c86144b0ad?q=80&w=500&auto=format&fit=crop"
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = cityImageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Semi-transparent overlay to keep text readable
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(overlayColor)
        )
        
        Text(
            text = "$formattedDate - $statusText (${result.cityName})",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center,
            style = LocalTextStyle.current.copy(
                shadow = Shadow(
                    color = Color.Black,
                    offset = Offset(2f, 2f),
                    blurRadius = 4f
                )
            )
        )
    }
}

fun formatTimestamp(timestamp: Long): String {
    val sdf = SimpleDateFormat("HH:mm dd.MM.yyyy", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GameScreenPreview() {
    Projekt_mobilkaTheme {
        GameScreen(
            gameHistory = listOf(
                GameResultEntity(1, System.currentTimeMillis(), true, "Warszawa"),
                GameResultEntity(2, System.currentTimeMillis() - 100000, false, "Wiedeń"),
                GameResultEntity(3, System.currentTimeMillis() - 200000, true, "Tallinn")
            ),
            onSettingsClick = {},
            onStartGameClick = { _ -> }
        )
    }
}
