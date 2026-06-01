package com.astrologyvedic.app.ui.screens.hora

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun HoraScreen(
    navController: NavController,
    viewModel: HoraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Hora", color = TextPrimary) },
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
                LoadingState(message = "Loading planetary hours...")
            }

            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.loadHora() }) }

            if (uiState.hasResult) {
                // Current hora highlight
                ResultCard(title = "Current Planetary Hour") {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = uiState.currentPlanet,
                            style = MaterialTheme.typography.headlineLarge,
                            color = Saffron500,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = uiState.currentNature,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (uiState.currentNature == "Benefic") Success else Warning
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Full day list
                ResultCard(title = "Today's Hora Schedule") {
                    Column {
                        uiState.slots.forEach { slot ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .then(
                                        if (slot.isCurrent) Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Saffron500.copy(alpha = 0.1f))
                                            .border(1.dp, Saffron500.copy(alpha = 0.3f), RoundedCornerShape(8.dp))
                                        else Modifier
                                    )
                                    .padding(vertical = 8.dp, horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = slot.planet,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (slot.isCurrent) Saffron500 else TextPrimary,
                                    fontWeight = if (slot.isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = slot.nature,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = if (slot.nature == "Benefic") Success else Warning,
                                    modifier = Modifier.weight(0.7f)
                                )
                                Text(
                                    text = "${slot.startTime} - ${slot.endTime}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = TextSecondary
                                )
                            }
                            if (slot != uiState.slots.last()) {
                                HorizontalDivider(color = BorderDark.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}
