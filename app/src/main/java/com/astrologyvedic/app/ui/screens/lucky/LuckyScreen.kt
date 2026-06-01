package com.astrologyvedic.app.ui.screens.lucky

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LuckyScreen(
    navController: NavController,
    viewModel: LuckyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Lucky Today", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (uiState.isLoading) { LoadingState(message = "Loading your lucky details...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.loadLucky() }) }

            if (uiState.hasResult) {
                // Lucky Number
                ResultCard(title = "Lucky Number") {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "${uiState.luckyNumber}",
                            style = MaterialTheme.typography.displayLarge,
                            color = Saffron500,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lucky Color
                ResultCard(title = "Lucky Color") {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(
                                    try { Color(android.graphics.Color.parseColor(uiState.colorHex)) }
                                    catch (_: Exception) { Saffron500 }
                                )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(uiState.luckyColor, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Lucky Direction
                ResultCard(title = "Lucky Direction") {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        Icon(Icons.Default.Explore, contentDescription = null, tint = Saffron500, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(uiState.luckyDirection, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Medium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ResultCard(title = "Lucky Day", modifier = Modifier.weight(1f)) {
                        Text(uiState.luckyDay, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.Medium)
                    }
                    ResultCard(title = "Lucky Gem", modifier = Modifier.weight(1f)) {
                        Text(uiState.luckyGem, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.Medium)
                    }
                }
            }
        }
    }
}
