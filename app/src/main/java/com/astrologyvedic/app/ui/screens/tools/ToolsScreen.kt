package com.astrologyvedic.app.ui.screens.tools

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.navigation.Routes

data class ToolItem(
    val title: String,
    val description: String,
    val route: String
)

private val toolItems = listOf(
    ToolItem("Rahu Kaal", "Check inauspicious time", Routes.RahuKaal.route),
    ToolItem("Choghadiya", "Auspicious time slots", Routes.Choghadiya.route),
    ToolItem("Hora", "Planetary hours", Routes.Hora.route),
    ToolItem("Muhurat", "Find best timing", Routes.Muhurat.route),
    ToolItem("Sunrise & Sunset", "Solar timings", Routes.SunriseSunset.route),
    ToolItem("Ayanamsa", "Precession calculator", Routes.Ayanamsa.route),
    ToolItem("Mantra Counter", "Track your japa", Routes.MantraCounter.route),
    ToolItem("Lucky Numbers", "Find your lucky numbers", Routes.Lucky.route),
    ToolItem("Gemstone", "Gem recommendations", Routes.Gemstone.route),
    ToolItem("Vastu", "Direction analysis", Routes.Vastu.route),
    ToolItem("Sade Sati", "Saturn transit check", Routes.SadeSati.route),
    ToolItem("Kaal Sarp", "Dosha analysis", Routes.Kaalsarp.route)
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToolsScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Astrology Tools") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item(span = { GridItemSpan(2) }) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Build,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            "Practical tools for daily astrological calculations and spiritual practice.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
            items(toolItems) { tool ->
                Card(
                    onClick = { navController.navigate(tool.route) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = tool.title,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = tool.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
