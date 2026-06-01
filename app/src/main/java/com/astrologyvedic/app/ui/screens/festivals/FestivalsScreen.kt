package com.astrologyvedic.app.ui.screens.festivals

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
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FestivalsScreen(
    navController: NavController,
    viewModel: FestivalsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Festivals 2026", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        if (uiState.isLoading) {
            LoadingState(message = "Loading festivals...")
        } else if (uiState.error != null) {
            ErrorState(message = uiState.error!!, onRetry = { viewModel.loadFestivals() })
        } else {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                uiState.festivals.forEach { festival ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
                            Column(modifier = Modifier.width(80.dp)) {
                                Text(festival.date, style = MaterialTheme.typography.bodySmall, color = Saffron400, fontWeight = FontWeight.Bold)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(festival.name, style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                                if (festival.significance.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(festival.significance, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
