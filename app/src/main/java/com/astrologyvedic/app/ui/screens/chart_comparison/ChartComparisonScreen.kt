package com.astrologyvedic.app.ui.screens.chart_comparison

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
fun ChartComparisonScreen(navController: NavController, viewModel: ChartComparisonViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Chart Comparison", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                PersonForm(title = "Person 1", state = uiState.person1Form, onStateChange = { viewModel.updatePerson1Form(it) })
                Spacer(modifier = Modifier.height(12.dp))
                PersonForm(title = "Person 2", state = uiState.person2Form, onStateChange = { viewModel.updatePerson2Form(it) })
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.person1Form.isValid() && uiState.person2Form.isValid(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Compare Charts", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Comparing charts...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                ResultCard(title = "Side-by-Side Positions") {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth().background(Cosmic800).padding(8.dp)) { Text("Planet", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold); Text("Person 1", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold); Text("Person 2", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold) }
                        uiState.positions.forEach { pos -> Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp)) { Text(pos.planet, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextPrimary); Text(pos.sign1, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextSecondary); Text(pos.sign2, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextSecondary) }; HorizontalDivider(color = BorderDark.copy(alpha = 0.5f)) }
                    }
                }
            }
        }
    }
}
