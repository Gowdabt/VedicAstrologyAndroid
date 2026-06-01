package com.astrologyvedic.app.ui.screens.meditation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeditationScreen(
    navController: NavController,
    viewModel: MeditationViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showSoundSelector by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Meditation", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Duration selector circles
            if (!uiState.isRunning && !uiState.isComplete) {
                Text("Select Duration", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    viewModel.durations.forEach { minutes ->
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(if (uiState.selectedDuration == minutes) Saffron500 else SurfaceCardElevated)
                                .clickable { viewModel.selectDuration(minutes) },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                "${minutes}m",
                                color = if (uiState.selectedDuration == minutes) Color.White else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                // Sound selector
                TextButton(onClick = { showSoundSelector = true }) {
                    Text("Sound: ${uiState.selectedSound}", color = Saffron400)
                }
            } else {
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Timer display
            val minutes = uiState.remainingSeconds / 60
            val seconds = uiState.remainingSeconds % 60
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (uiState.isComplete) {
                    Text("Namaste", style = MaterialTheme.typography.displaySmall, color = Success, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Session Complete", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                } else {
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        style = MaterialTheme.typography.displayLarge.copy(fontSize = 72.sp),
                        color = TextPrimary,
                        fontWeight = FontWeight.Light
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Controls
            Row(horizontalArrangement = Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
                if (uiState.isRunning || uiState.remainingSeconds < uiState.selectedDuration * 60) {
                    IconButton(
                        onClick = { viewModel.reset() },
                        modifier = Modifier.size(56.dp).clip(CircleShape).background(SurfaceCardElevated)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset", tint = TextPrimary)
                    }
                }

                IconButton(
                    onClick = { viewModel.toggleTimer() },
                    modifier = Modifier.size(80.dp).clip(CircleShape).background(Saffron500)
                ) {
                    Icon(
                        if (uiState.isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (uiState.isRunning) "Pause" else "Start",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    if (showSoundSelector) {
        AlertDialog(
            onDismissRequest = { showSoundSelector = false },
            title = { Text("Ambient Sound", color = TextPrimary) },
            text = {
                Column {
                    viewModel.sounds.forEach { sound ->
                        TextButton(
                            onClick = { viewModel.selectSound(sound); showSoundSelector = false },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(sound, color = if (sound == uiState.selectedSound) Saffron500 else TextSecondary, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showSoundSelector = false }) { Text("Cancel", color = TextSecondary) } },
            containerColor = SurfaceCardElevated
        )
    }
}
