package com.astrologyvedic.app.ui.screens.stock_astrology

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockAstrologyScreen(navController: NavController, viewModel: StockAstrologyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Stock Astrology", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (uiState.isLoading) { LoadingState(message = "Analyzing planetary positions...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.loadPredictions() }) }
            if (uiState.hasResult) {
                if (uiState.planetaryIndicators.isNotBlank()) { ResultCard(title = "Planetary Indicators") { Text(uiState.planetaryIndicators, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) }; Spacer(modifier = Modifier.height(12.dp)) }
                if (uiState.todayOutlook.isNotBlank()) { ResultCard(title = "Today's Outlook") { Text(uiState.todayOutlook, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) }; Spacer(modifier = Modifier.height(12.dp)) }
                if (uiState.sectors.isNotEmpty()) { ResultCard(title = "Sector Predictions") { Column { uiState.sectors.forEach { s -> Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), horizontalArrangement = Arrangement.SpaceBetween) { Text(s.sector, style = MaterialTheme.typography.bodyMedium, color = TextPrimary); Text(s.trend, style = MaterialTheme.typography.bodyMedium, color = when(s.trend.lowercase()) { "bullish" -> Success; "bearish" -> Error; else -> Warning }, fontWeight = FontWeight.Medium) }; HorizontalDivider(color = BorderDark.copy(alpha = 0.5f)) } } }; Spacer(modifier = Modifier.height(12.dp)) }
                if (uiState.weeklyOutlook.isNotBlank()) { ResultCard(title = "Weekly Outlook") { Text(uiState.weeklyOutlook, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) } }
            }
        }
    }
}
