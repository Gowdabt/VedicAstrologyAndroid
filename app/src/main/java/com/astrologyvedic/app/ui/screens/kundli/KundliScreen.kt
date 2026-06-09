package com.astrologyvedic.app.ui.screens.kundli

import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PictureAsPdf
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
// TabRowDefaults handles indicator internally in M3
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.PersonForm
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.components.isValid
import com.astrologyvedic.app.util.ShareUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KundliScreen(
    navController: NavController,
    viewModel: KundliViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TopAppBar(
            title = { Text("Kundli", color = MaterialTheme.colorScheme.onSurface) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (uiState.result == null && !uiState.isLoading) {
                // Form state
                PersonForm(
                    title = "Birth Details",
                    state = uiState.personForm,
                    onStateChange = { viewModel.updatePersonForm(it) },
                    autoDetectLocation = true
                )

                Spacer(modifier = Modifier.height(16.dp))

                FilledTonalButton(
                    onClick = { viewModel.generateKundli() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.personForm.isValid(),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Generate Kundli",
                        style = MaterialTheme.typography.titleSmall,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }

            if (uiState.isLoading) {
                LoadingState(message = "Generating your Kundli...")
            }

            uiState.error?.let { error ->
                ErrorState(
                    message = error,
                    onRetry = { viewModel.generateKundli() }
                )
            }

            if (uiState.result != null) {
                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        val name = uiState.personForm.name
                        val shareText = buildString {
                            appendLine("Kundli Report - $name")
                            appendLine("Generated by Vedic Astrology App")
                            appendLine()
                            if (uiState.planets.isNotEmpty()) {
                                appendLine("Planetary Positions:")
                                uiState.planets.forEach { p ->
                                    appendLine("${p.planet}: ${p.sign} (House ${p.house})")
                                }
                            }
                        }
                        ShareUtils.shareText(context, "Kundli - $name", shareText)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(onClick = {
                        Toast.makeText(context, "PDF export coming soon", Toast.LENGTH_SHORT).show()
                    }) {
                        Icon(Icons.Default.PictureAsPdf, contentDescription = "PDF", tint = MaterialTheme.colorScheme.primary)
                    }
                }

                // Tabs
                val tabs = listOf("Chart", "Planets", "Dasha", "Yoga", "Dosha")
                TabRow(
                    selectedTabIndex = uiState.selectedTab,
                    containerColor = MaterialTheme.colorScheme.surfaceContainer,
                    contentColor = MaterialTheme.colorScheme.primary
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = uiState.selectedTab == index,
                            onClick = { viewModel.selectTab(index) },
                            text = {
                                Text(
                                    text = title,
                                    color = if (uiState.selectedTab == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                                    style = MaterialTheme.typography.bodySmall
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (uiState.selectedTab) {
                    0 -> ChartTab()
                    1 -> PlanetsTab(planets = uiState.planets)
                    2 -> DashaTab(result = uiState.result)
                    3 -> YogaTab(result = uiState.result)
                    4 -> DoshaTab(result = uiState.result)
                }
            }
        }
    }
}

@Composable
private fun ChartTab() {
    ResultCard(title = "North Indian Chart") {
        val chartColor = MaterialTheme.colorScheme.primary
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val strokeWidth = 2f
                val color = chartColor

                // Outer square
                drawRect(
                    color = color,
                    topLeft = Offset.Zero,
                    size = size,
                    style = Stroke(width = strokeWidth)
                )

                // Diagonal lines forming diamond
                drawLine(color, Offset(w / 2, 0f), Offset(w, h / 2), strokeWidth = strokeWidth)
                drawLine(color, Offset(w, h / 2), Offset(w / 2, h), strokeWidth = strokeWidth)
                drawLine(color, Offset(w / 2, h), Offset(0f, h / 2), strokeWidth = strokeWidth)
                drawLine(color, Offset(0f, h / 2), Offset(w / 2, 0f), strokeWidth = strokeWidth)

                // Cross lines
                drawLine(color, Offset(w / 2, 0f), Offset(w / 2, h), strokeWidth = strokeWidth)
                drawLine(color, Offset(0f, h / 2), Offset(w, h / 2), strokeWidth = strokeWidth)
            }

            Text(
                text = "Asc",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun PlanetsTab(planets: List<PlanetInfo>) {
    ResultCard(title = "Planetary Positions") {
        Column {
            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                    .padding(8.dp)
            ) {
                Text("Planet", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("Sign", modifier = Modifier.weight(1f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("House", modifier = Modifier.weight(0.7f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("Nakshatra", modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                Text("Degree", modifier = Modifier.weight(0.8f), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
            planets.forEach { planet ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Text(planet.planet, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface)
                    Text(planet.sign, modifier = Modifier.weight(1f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(planet.house, modifier = Modifier.weight(0.7f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(planet.nakshatra, modifier = Modifier.weight(1.2f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(planet.degree, modifier = Modifier.weight(0.8f), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun DashaTab(result: com.google.gson.JsonObject?) {
    ResultCard(title = "Dasha Periods") {
        val dashaText = try {
            result?.getAsJsonObject("analysis")
                ?.get("dasha")?.asString ?: "Dasha data will appear here based on your birth chart analysis."
        } catch (_: Exception) {
            "Dasha data will appear here based on your birth chart analysis."
        }
        Text(
            text = dashaText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun YogaTab(result: com.google.gson.JsonObject?) {
    ResultCard(title = "Yogas Found") {
        val yogaText = try {
            result?.getAsJsonObject("analysis")
                ?.get("yogas")?.asString ?: "Yoga analysis will appear here based on planetary positions."
        } catch (_: Exception) {
            "Yoga analysis will appear here based on planetary positions."
        }
        Text(
            text = yogaText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun DoshaTab(result: com.google.gson.JsonObject?) {
    ResultCard(title = "Doshas") {
        val doshaText = try {
            result?.getAsJsonObject("analysis")
                ?.get("doshas")?.asString ?: "Dosha analysis (Manglik, Kaalsarp, etc.) will appear here."
        } catch (_: Exception) {
            "Dosha analysis (Manglik, Kaalsarp, etc.) will appear here."
        }
        Text(
            text = doshaText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
