package com.astrologyvedic.app.ui.screens.navamsa

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavamsaScreen(navController: NavController, viewModel: NavamsaViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Navamsa (D9) Chart", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                PersonForm(title = "Birth Details", state = uiState.personForm, onStateChange = { viewModel.updatePersonForm(it) })
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.personForm.isValid(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Generate D9 Chart", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Generating Navamsa chart...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                ResultCard(title = "Key Indicators") { Column { if (uiState.venusPosition.isNotBlank()) Text("Venus in D9: ${uiState.venusPosition}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary); if (uiState.seventhLord.isNotBlank()) Text("7th Lord: ${uiState.seventhLord}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary) } }
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.marriageIndicators.isNotEmpty()) { ResultCard(title = "Marriage Indicators") { Column { uiState.marriageIndicators.forEach { Text("- $it", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.padding(vertical = 2.dp)) } } } }
            }
        }
    }
}
