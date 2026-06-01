package com.astrologyvedic.app.ui.screens.rahu_kaal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
fun RahuKaalScreen(
    navController: NavController,
    viewModel: RahuKaalViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(SurfaceDark)
    ) {
        TopAppBar(
            title = { Text("Rahu Kaal", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            if (uiState.isLoading) {
                LoadingState(message = "Loading Rahu Kaal timings...")
            }

            uiState.error?.let { error ->
                ErrorState(message = error, onRetry = { viewModel.loadTimings() })
            }

            if (uiState.hasResult) {
                // Today's highlight
                ResultCard(title = "Today's Rahu Kaal") {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${uiState.todayStart} - ${uiState.todayEnd}",
                                style = MaterialTheme.typography.headlineMedium,
                                color = Error,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        // Time bar visualization
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(24.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(SurfaceCard)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(0.15f)
                                    .fillMaxHeight()
                                    .offset(x = 80.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(Error.copy(alpha = 0.7f))
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("6:00", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                            Text("12:00", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                            Text("18:00", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Avoid starting new activities during this period",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextTertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Weekly timings
                ResultCard(title = "Weekly Timings") {
                    Column {
                        uiState.weekTimings.forEach { timing ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (timing.isToday) Saffron500.copy(alpha = 0.1f) else androidx.compose.ui.graphics.Color.Transparent)
                                    .padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = timing.day,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (timing.isToday) Saffron500 else TextPrimary,
                                    fontWeight = if (timing.isToday) FontWeight.Bold else FontWeight.Normal
                                )
                                Text(
                                    text = "${timing.startTime} - ${timing.endTime}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (timing.isToday) Error else TextSecondary
                                )
                            }
                            if (timing != uiState.weekTimings.last()) {
                                HorizontalDivider(color = BorderDark)
                            }
                        }
                    }
                }
            }
        }
    }
}
