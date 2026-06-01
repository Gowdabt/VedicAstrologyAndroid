package com.astrologyvedic.app.ui.screens.choghadiya

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun ChoghadiyaScreen(
    navController: NavController,
    viewModel: ChoghadiyaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Choghadiya", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        if (uiState.isLoading) {
            LoadingState(message = "Loading Choghadiya...")
        } else if (uiState.error != null) {
            ErrorState(message = uiState.error!!, onRetry = { viewModel.loadChoghadiya() })
        } else if (uiState.hasResult) {
            Column(modifier = Modifier.fillMaxSize()) {
                TabRow(
                    selectedTabIndex = uiState.selectedTab,
                    containerColor = SurfaceCard,
                    contentColor = Saffron500
                ) {
                    Tab(selected = uiState.selectedTab == 0, onClick = { viewModel.selectTab(0) },
                        text = { Text("Day", color = if (uiState.selectedTab == 0) Saffron500 else TextTertiary) })
                    Tab(selected = uiState.selectedTab == 1, onClick = { viewModel.selectTab(1) },
                        text = { Text("Night", color = if (uiState.selectedTab == 1) Saffron500 else TextTertiary) })
                }

                Column(
                    modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
                ) {
                    val slots = if (uiState.selectedTab == 0) uiState.daySlots else uiState.nightSlots
                    slots.forEach { slot ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .clip(CircleShape)
                                        .background(
                                            when (slot.type) {
                                                ChoghadiyaType.GOOD -> Success
                                                ChoghadiyaType.BAD -> Error
                                                ChoghadiyaType.NEUTRAL -> Warning
                                            }
                                        )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(slot.name, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.Medium)
                                    if (slot.ruling.isNotBlank()) {
                                        Text(slot.ruling, style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                                    }
                                }
                                Text(
                                    text = "${slot.startTime} - ${slot.endTime}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    // Legend
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        LegendItem(color = Success, label = "Good")
                        LegendItem(color = Error, label = "Bad")
                        LegendItem(color = Warning, label = "Neutral")
                    }
                }
            }
        }
    }
}

@Composable
private fun LegendItem(color: androidx.compose.ui.graphics.Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
        Spacer(modifier = Modifier.width(4.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextTertiary)
    }
}
