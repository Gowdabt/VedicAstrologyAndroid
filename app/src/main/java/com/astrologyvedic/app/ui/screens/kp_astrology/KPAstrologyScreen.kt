package com.astrologyvedic.app.ui.screens.kp_astrology

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
fun KPAstrologyScreen(navController: NavController, viewModel: KPAstrologyViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("KP Astrology", color = TextPrimary) }, navigationIcon = {
            IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) }
        }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                PersonForm(title = "Birth Details", state = uiState.personForm, onStateChange = { viewModel.updatePersonForm(it) })
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.personForm.isValid(),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) {
                    Text("Generate KP Chart", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp))
                }
            }
            if (uiState.isLoading) { LoadingState(message = "Generating KP analysis...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                if (uiState.subLords.isNotEmpty()) {
                    ResultCard(title = "Sub-Lords Table") {
                        Column {
                            Row(modifier = Modifier.fillMaxWidth().background(Cosmic800).padding(8.dp)) {
                                Text("House", modifier = Modifier.weight(0.7f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold)
                                Text("Sign Lord", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold)
                                Text("Star Lord", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold)
                                Text("Sub Lord", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold)
                            }
                            uiState.subLords.forEach { entry ->
                                Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp)) {
                                    Text(entry.house, modifier = Modifier.weight(0.7f), style = MaterialTheme.typography.bodySmall, color = TextPrimary)
                                    Text(entry.signLord, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                    Text(entry.starLord, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                    Text(entry.subLord, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                                HorizontalDivider(color = BorderDark.copy(alpha = 0.5f))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                if (uiState.significators.isNotBlank()) {
                    ResultCard(title = "Significators") { Text(uiState.significators, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) }
                    Spacer(modifier = Modifier.height(12.dp))
                }
                if (uiState.prediction.isNotBlank()) {
                    ResultCard(title = "Prediction") { Text(uiState.prediction, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) }
                }
            }
        }
    }
}
