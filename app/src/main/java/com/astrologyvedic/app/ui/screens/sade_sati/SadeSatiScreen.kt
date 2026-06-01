package com.astrologyvedic.app.ui.screens.sade_sati

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SadeSatiScreen(
    navController: NavController,
    viewModel: SadeSatiViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Sade Sati", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                PersonForm(title = "Birth Details", state = uiState.personForm, onStateChange = { viewModel.updatePersonForm(it) })
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(
                    onClick = { viewModel.calculate() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.personForm.isValid(),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Check Sade Sati", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            if (uiState.isLoading) { LoadingState(message = "Checking Sade Sati status...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }

            if (uiState.hasResult) {
                ResultCard(title = "Status") {
                    Column {
                        Text(
                            text = if (uiState.isActive) "ACTIVE" else "NOT ACTIVE",
                            style = MaterialTheme.typography.headlineSmall,
                            color = if (uiState.isActive) Error else Success,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Phase: ${uiState.currentPhase}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Text("Start: ${uiState.startDate}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        Text("End: ${uiState.endDate}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        if (uiState.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(uiState.description, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                        }
                    }
                }

                if (uiState.remedies.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    ResultCard(title = "Remedies") {
                        Column {
                            uiState.remedies.forEachIndexed { index, remedy ->
                                Text(
                                    text = "${index + 1}. $remedy",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSecondary,
                                    modifier = Modifier.padding(vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
