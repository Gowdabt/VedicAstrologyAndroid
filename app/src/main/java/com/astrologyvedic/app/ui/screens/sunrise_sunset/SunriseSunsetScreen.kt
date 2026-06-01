package com.astrologyvedic.app.ui.screens.sunrise_sunset

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.NightsStay
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun SunriseSunsetScreen(
    navController: NavController,
    viewModel: SunriseSunsetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Sunrise & Sunset", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = uiState.location,
                            onValueChange = { viewModel.updateLocation(it) },
                            label = { Text("Location") },
                            leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextTertiary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                                focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard
                            ),
                            shape = MaterialTheme.shapes.small, singleLine = true
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(
                    onClick = { viewModel.calculate() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.location.isNotBlank(),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Get Timings", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            if (uiState.isLoading) { LoadingState(message = "Calculating timings...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }

            if (uiState.hasResult) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    ResultCard(title = "Sunrise", modifier = Modifier.weight(1f)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.WbSunny, contentDescription = null, tint = Saffron500, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(uiState.sunrise, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                    ResultCard(title = "Sunset", modifier = Modifier.weight(1f)) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                            Icon(Icons.Default.NightsStay, contentDescription = null, tint = Cosmic400, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(uiState.sunset, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                ResultCard(title = "Golden Hours") {
                    Column {
                        Text("Morning: ${uiState.goldenHourMorning}", style = MaterialTheme.typography.bodyMedium, color = Saffron400)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text("Evening: ${uiState.goldenHourEvening}", style = MaterialTheme.typography.bodyMedium, color = Saffron400)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                ResultCard(title = "Day Length") {
                    Text(uiState.dayLength, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
