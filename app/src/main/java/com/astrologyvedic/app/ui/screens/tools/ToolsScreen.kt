package com.astrologyvedic.app.ui.screens.tools

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.navigation.Routes
import com.astrologyvedic.app.ui.theme.SurfaceCard
import com.astrologyvedic.app.ui.theme.TextPrimary
import com.astrologyvedic.app.ui.theme.TextSecondary

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

@Composable
fun ToolsScreen(navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Text(
                text = "Astrology Tools",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }
        items(toolItems) { tool ->
            Card(
                onClick = { navController.navigate(tool.route) },
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(
                        text = tool.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = TextPrimary
                    )
                    Text(
                        text = tool.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSecondary,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}
