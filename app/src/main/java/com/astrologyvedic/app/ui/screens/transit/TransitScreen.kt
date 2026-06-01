package com.astrologyvedic.app.ui.screens.transit

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
fun TransitScreen(navController: NavController, viewModel: TransitViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Current Transits", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                PersonForm(title = "Birth Details", state = uiState.personForm, onStateChange = { viewModel.updatePersonForm(it) })
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.personForm.isValid(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Show Transits", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Calculating transits...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                if (uiState.summary.isNotBlank()) { ResultCard(title = "Transit Summary") { Text(uiState.summary, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) }; Spacer(modifier = Modifier.height(12.dp)) }
                ResultCard(title = "Planet Transits") {
                    Column {
                        Row(modifier = Modifier.fillMaxWidth().background(Cosmic800).padding(8.dp)) { Text("Planet", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold); Text("Sign", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold); Text("House", modifier = Modifier.weight(0.6f), style = MaterialTheme.typography.labelSmall, color = Saffron400, fontWeight = FontWeight.Bold) }
                        uiState.transits.forEach { t ->
                            Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 6.dp)) {
                                Row(modifier = Modifier.fillMaxWidth()) { Text(t.planet, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextPrimary); Text(t.sign, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = TextSecondary); Text(t.house, modifier = Modifier.weight(0.6f), style = MaterialTheme.typography.bodySmall, color = TextSecondary) }
                                if (t.effect.isNotBlank()) Text(t.effect, style = MaterialTheme.typography.bodySmall, color = TextTertiary, modifier = Modifier.padding(top = 2.dp))
                            }
                            HorizontalDivider(color = BorderDark.copy(alpha = 0.5f))
                        }
                    }
                }
            }
        }
    }
}
