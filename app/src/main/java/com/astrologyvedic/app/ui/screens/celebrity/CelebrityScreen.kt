package com.astrologyvedic.app.ui.screens.celebrity

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CelebrityScreen(navController: NavController, viewModel: CelebrityViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Celebrity Charts", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        if (uiState.isLoading) { LoadingState(message = "Loading celebrities...") }
        else if (uiState.error != null) { ErrorState(message = uiState.error!!, onRetry = { viewModel.loadCelebrities() }) }
        else { Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            uiState.celebrities.forEach { celeb ->
                Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(celeb.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary, fontWeight = FontWeight.Bold)
                        Text("Born: ${celeb.dob}", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column { Text("Sun", style = MaterialTheme.typography.labelSmall, color = TextTertiary); Text(celeb.sunSign, style = MaterialTheme.typography.bodySmall, color = Saffron400) }
                            Column { Text("Moon", style = MaterialTheme.typography.labelSmall, color = TextTertiary); Text(celeb.moonSign, style = MaterialTheme.typography.bodySmall, color = TextSecondary) }
                            Column { Text("Ascendant", style = MaterialTheme.typography.labelSmall, color = TextTertiary); Text(celeb.ascendant, style = MaterialTheme.typography.bodySmall, color = TextSecondary) }
                        }
                    }
                }
            }
        } }
    }
}
