package com.astrologyvedic.app.ui.screens.puja_guide

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PujaGuideScreen(
    navController: NavController,
    viewModel: PujaGuideViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Puja Guide", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = {
                    if (uiState.hasDetail) viewModel.clearSelection() else navController.popBackStack()
                }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        if (uiState.isLoading) {
            LoadingState(message = "Loading puja guide...")
        } else if (uiState.hasDetail && uiState.selectedPuja != null) {
            // Detail view
            val puja = uiState.selectedPuja!!
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                Text(puja.name, style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                Text("Deity: ${puja.deity}", style = MaterialTheme.typography.bodyMedium, color = Saffron400)
                Spacer(modifier = Modifier.height(8.dp))
                Text(puja.benefits, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(16.dp))

                if (puja.materials.isNotEmpty()) {
                    ResultCard(title = "Materials Required") {
                        Column {
                            puja.materials.forEach { material ->
                                Text("- $material", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.padding(vertical = 2.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                if (puja.steps.isNotEmpty()) {
                    ResultCard(title = "Step-by-Step Guide") {
                        Column {
                            puja.steps.forEachIndexed { index, step ->
                                Row(modifier = Modifier.padding(vertical = 4.dp)) {
                                    Text("${index + 1}.", style = MaterialTheme.typography.bodyMedium, color = Saffron500, modifier = Modifier.width(24.dp))
                                    Text(step, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // Grid view
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(uiState.pujas) { puja ->
                    Card(
                        modifier = Modifier.fillMaxWidth().aspectRatio(1.2f).clickable { viewModel.selectPuja(puja) },
                        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(12.dp),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(puja.name, style = MaterialTheme.typography.titleSmall, color = TextPrimary, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(puja.deity, style = MaterialTheme.typography.bodySmall, color = Saffron400, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(puja.benefits, style = MaterialTheme.typography.bodySmall, color = TextTertiary, textAlign = TextAlign.Center)
                        }
                    }
                }
            }
        }
    }
}
