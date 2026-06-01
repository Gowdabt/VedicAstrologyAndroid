package com.astrologyvedic.app.ui.screens.timeline

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimelineScreen(navController: NavController, viewModel: TimelineViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Dasha Timeline", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                PersonForm(title = "Birth Details", state = uiState.personForm, onStateChange = { viewModel.updatePersonForm(it) })
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.personForm.isValid(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Generate Timeline", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Generating Dasha timeline...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                if (uiState.currentDasha.isNotBlank()) { ResultCard(title = "Current Dasha") { Text(uiState.currentDasha, style = MaterialTheme.typography.headlineSmall, color = Saffron500, fontWeight = FontWeight.Bold) }; Spacer(modifier = Modifier.height(12.dp)) }
                ResultCard(title = "Dasha Periods") {
                    Column {
                        uiState.periods.forEach { period ->
                            Row(modifier = Modifier.fillMaxWidth().then(if (period.isCurrent) Modifier.clip(RoundedCornerShape(8.dp)).background(Saffron500.copy(alpha = 0.1f)).border(1.dp, Saffron500.copy(alpha = 0.3f), RoundedCornerShape(8.dp)) else Modifier).padding(8.dp)) {
                                Text(period.planet, style = MaterialTheme.typography.bodyMedium, color = if (period.isCurrent) Saffron500 else TextPrimary, fontWeight = if (period.isCurrent) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.weight(1f))
                                Text("${period.startDate} - ${period.endDate}", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                            }
                            if (period != uiState.periods.last()) HorizontalDivider(color = BorderDark.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}
