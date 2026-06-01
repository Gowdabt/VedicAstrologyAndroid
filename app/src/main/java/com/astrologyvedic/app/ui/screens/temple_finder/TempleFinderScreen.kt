package com.astrologyvedic.app.ui.screens.temple_finder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TempleFinderScreen(
    navController: NavController,
    viewModel: TempleFinderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Navagraha Temples", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        // Planet grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier.fillMaxWidth().padding(16.dp).heightIn(max = 240.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(uiState.planetTemples) { index, planetTemple ->
                Card(
                    modifier = Modifier.fillMaxWidth().clickable { viewModel.selectPlanet(index) },
                    colors = CardDefaults.cardColors(
                        containerColor = if (index == uiState.selectedPlanet) Saffron500 else SurfaceCardElevated
                    ),
                    shape = MaterialTheme.shapes.small
                ) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            planetTemple.planet.split(" ").first(),
                            style = MaterialTheme.typography.bodySmall,
                            color = if (index == uiState.selectedPlanet) Color.White else TextPrimary,
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }

        // Selected planet temples
        if (uiState.planetTemples.isNotEmpty()) {
            val selected = uiState.planetTemples[uiState.selectedPlanet]
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                Text(selected.planet, style = MaterialTheme.typography.titleLarge, color = Saffron500, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(12.dp))
                selected.temples.forEach { temple ->
                    ResultCard(title = temple.name) {
                        Column {
                            Text("Location: ${temple.location}", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(temple.significance, style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
