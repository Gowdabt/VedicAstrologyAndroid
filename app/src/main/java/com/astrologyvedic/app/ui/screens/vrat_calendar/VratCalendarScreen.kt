package com.astrologyvedic.app.ui.screens.vrat_calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VratCalendarScreen(
    navController: NavController,
    viewModel: VratCalendarViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Vrat Calendar", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        // Month selector
        Row(
            modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()).padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            viewModel.months.forEach { month ->
                FilterChip(
                    selected = uiState.selectedMonth == month,
                    onClick = { viewModel.selectMonth(month) },
                    label = { Text(month.take(3)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Saffron500,
                        selectedLabelColor = androidx.compose.ui.graphics.Color.White,
                        containerColor = SurfaceCardElevated,
                        labelColor = TextSecondary
                    )
                )
            }
        }

        if (uiState.isLoading) {
            LoadingState(message = "Loading vrat dates...")
        } else if (uiState.error != null) {
            ErrorState(message = uiState.error!!, onRetry = { viewModel.loadVrats() })
        } else {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                uiState.vrats.forEach { vrat ->
                    val typeColor = when (vrat.type.lowercase()) {
                        "ekadashi" -> Saffron500
                        "pradosh" -> Cosmic400
                        "amavasya" -> Error
                        "purnima" -> Success
                        else -> TextSecondary
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Box(
                                modifier = Modifier
                                    .width(4.dp)
                                    .height(40.dp)
                                    .clip(RoundedCornerShape(2.dp))
                                    .background(typeColor)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(vrat.name, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.Medium)
                                Text(vrat.type, style = MaterialTheme.typography.bodySmall, color = typeColor)
                            }
                            Text(vrat.date, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        }
                    }
                }

                if (uiState.vrats.isEmpty() && uiState.hasResult) {
                    Text("No vrat dates found for this month", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        }
    }
}
