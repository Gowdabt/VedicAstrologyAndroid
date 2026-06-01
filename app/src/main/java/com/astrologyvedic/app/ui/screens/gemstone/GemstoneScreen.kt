package com.astrologyvedic.app.ui.screens.gemstone

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
fun GemstoneScreen(
    navController: NavController,
    viewModel: GemstoneViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Gemstone Recommendation", color = TextPrimary) },
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
                    Text("Get Recommendations", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            if (uiState.isLoading) { LoadingState(message = "Finding your gemstones...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }

            if (uiState.hasResult) {
                uiState.gemstones.forEach { gem ->
                    ResultCard(title = gem.name) {
                        Column {
                            Text("Planet: ${gem.planet}", style = MaterialTheme.typography.bodyMedium, color = Saffron400, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(8.dp))
                            if (gem.properties.isNotBlank()) Text("Properties: ${gem.properties}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = BorderDark)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text("Wearing Instructions", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                            Spacer(modifier = Modifier.height(4.dp))
                            if (gem.weight.isNotBlank()) Text("Weight: ${gem.weight}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            if (gem.metal.isNotBlank()) Text("Metal: ${gem.metal}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            if (gem.finger.isNotBlank()) Text("Finger: ${gem.finger}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            if (gem.day.isNotBlank()) Text("Day to wear: ${gem.day}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
