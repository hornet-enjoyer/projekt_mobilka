package com.example.projekt_mobilka.view

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.projekt_mobilka.view.theme.Projekt_mobilkaTheme

@Composable
fun SettingsScreen(
    username: String,
    profilePicturePath: String?,
    wins: Int,
    losses: Int,
    onUsernameChange: (String) -> Unit,
    onProfilePictureChange: (Uri) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isEditingUsername by remember { mutableStateOf(false) }
    var tempUsername by remember { mutableStateOf(username) }

    val totalGames = wins + losses
    val winRate = if (totalGames > 0) (wins.toDouble() / totalGames * 100).toInt() else 0

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onProfilePictureChange(it) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(32.dp),
                    tint = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Profile Picture with Edit Icon
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // Profile background/image
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color(0xFFEADDFF))
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (profilePicturePath != null) {
                    AsyncImage(
                        model = profilePicturePath,
                        contentDescription = "Profile Picture",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(0.7f),
                        tint = Color(0xFF4F378B)
                    )
                }
            }

            // Edit Icon overlay
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = (-8).dp, y = 8.dp)
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .padding(2.dp)
                    .clickable { launcher.launch("image/*") }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background(Color.Black),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit Profile Picture",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Username Display/Edit
        Row(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (isEditingUsername) {
                TextField(
                    value = tempUsername,
                    onValueChange = { tempUsername = it },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent
                    )
                )
                IconButton(onClick = {
                    onUsernameChange(tempUsername)
                    isEditingUsername = false
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Save Username")
                }
            } else {
                Text(
                    text = username,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {
                    tempUsername = username
                    isEditingUsername = true
                }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit Username")
                }
            }
        }

        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f), thickness = 1.dp, color = Color.LightGray)

        Spacer(modifier = Modifier.height(16.dp))

        // Stats List
        StatItem("Wygrane gry", wins.toString())
        StatItem("Przegrane gry", losses.toString())
        
        HorizontalDivider(modifier = Modifier.fillMaxWidth(0.9f).padding(vertical = 8.dp), thickness = 1.dp, color = Color.LightGray)
        
        StatItem("Procent wygranych gier", "$winRate%")
    }
}

@Composable
fun StatItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, fontSize = 18.sp)
        Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF7D67AE))
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    Projekt_mobilkaTheme {
        SettingsScreen(
            username = "Username",
            profilePicturePath = null,
            wins = 10,
            losses = 5,
            onUsernameChange = {},
            onProfilePictureChange = {},
            onBackClick = {}
        )
    }
}
