package com.astrologyvedic.app.ui.screens.horoscope_share

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HoroscopeShareScreen(navController: NavController, viewModel: HoroscopeShareViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Share Horoscope", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            if (uiState.isLoading) { LoadingState(message = "Generating shareable card...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.loadHoroscope() }) }
            if (uiState.hasResult) {
                Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Cosmic800), shape = MaterialTheme.shapes.large) {
                    Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Today's Horoscope", style = MaterialTheme.typography.titleSmall, color = TextTertiary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(uiState.rashi, style = MaterialTheme.typography.headlineMedium, color = Saffron500, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(uiState.prediction, style = MaterialTheme.typography.bodyMedium, color = TextSecondary, textAlign = TextAlign.Center)
                        Spacer(modifier = Modifier.height(12.dp))
                        if (uiState.luckyNumber.isNotBlank()) { Text("Lucky Number: ${uiState.luckyNumber}", style = MaterialTheme.typography.bodySmall, color = Saffron400) }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                FilledTonalButton(onClick = { /* TODO: Share intent */ }, colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = androidx.compose.ui.graphics.Color.White), shape = MaterialTheme.shapes.medium) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Share")
                }
            }
        }
    }
}
