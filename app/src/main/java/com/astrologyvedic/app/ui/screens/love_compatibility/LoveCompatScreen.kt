package com.astrologyvedic.app.ui.screens.love_compatibility

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
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
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoveCompatScreen(navController: NavController, viewModel: LoveCompatViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Love Compatibility", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                PersonForm(title = "Person 1", state = uiState.person1Form, onStateChange = { viewModel.updatePerson1Form(it) })
                Spacer(modifier = Modifier.height(12.dp))
                PersonForm(title = "Person 2", state = uiState.person2Form, onStateChange = { viewModel.updatePerson2Form(it) })
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.person1Form.isValid() && uiState.person2Form.isValid(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Check Compatibility", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Analyzing compatibility...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                ResultCard(title = "Compatibility Score") {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Favorite, contentDescription = null, tint = Error, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("${uiState.compatibilityPercent}%", style = MaterialTheme.typography.displayMedium, color = Saffron500, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                ResultCard(title = "Breakdown") {
                    Column {
                        ScoreRow("Emotional", uiState.emotionalScore)
                        ScoreRow("Physical", uiState.physicalScore)
                        ScoreRow("Intellectual", uiState.intellectualScore)
                    }
                }
                if (uiState.summary.isNotBlank()) { Spacer(modifier = Modifier.height(12.dp)); ResultCard(title = "Summary") { Text(uiState.summary, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) } }
            }
        }
    }
}

@Composable
private fun ScoreRow(label: String, score: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = TextPrimary, modifier = Modifier.width(100.dp))
        LinearProgressIndicator(progress = { score / 100f }, modifier = Modifier.weight(1f).height(8.dp), color = Saffron500, trackColor = SurfaceCard)
        Spacer(modifier = Modifier.width(8.dp))
        Text("$score%", style = MaterialTheme.typography.bodySmall, color = TextSecondary)
    }
}
