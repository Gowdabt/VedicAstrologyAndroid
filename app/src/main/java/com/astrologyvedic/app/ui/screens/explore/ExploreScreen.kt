package com.astrologyvedic.app.ui.screens.explore

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

data class ExploreCategory(
    val title: String,
    val items: List<ExploreItem>
)

data class ExploreItem(
    val title: String,
    val route: String
)

private val exploreCategories = listOf(
    ExploreCategory(
        title = "Astrology",
        items = listOf(
            ExploreItem("Kundli", Routes.Kundli.route),
            ExploreItem("Daily", Routes.Daily.route),
            ExploreItem("Match", Routes.Match.route),
            ExploreItem("Panchang", Routes.Panchang.route),
            ExploreItem("Numerology", Routes.Numerology.route),
            ExploreItem("KP Astrology", Routes.KpAstrology.route),
            ExploreItem("Lal Kitab", Routes.LalKitab.route),
            ExploreItem("Western", Routes.Western.route),
            ExploreItem("Chinese", Routes.ChineseZodiac.route),
            ExploreItem("Nadi", Routes.Nadi.route)
        )
    ),
    ExploreCategory(
        title = "Charts & Analysis",
        items = listOf(
            ExploreItem("Navamsa", Routes.Navamsa.route),
            ExploreItem("Dasamsa", Routes.Dasamsa.route),
            ExploreItem("Varga Charts", Routes.VargaCharts.route),
            ExploreItem("Transit", Routes.Transit.route),
            ExploreItem("Yoga Detection", Routes.YogaDetection.route),
            ExploreItem("Chart Compare", Routes.ChartComparison.route)
        )
    ),
    ExploreCategory(
        title = "Compatibility",
        items = listOf(
            ExploreItem("Match Making", Routes.Match.route),
            ExploreItem("Love", Routes.LoveCompatibility.route),
            ExploreItem("Guna Milan", Routes.GunaMilan.route),
            ExploreItem("Name Match", Routes.NameMatch.route),
            ExploreItem("Porutham", Routes.Porutham.route)
        )
    ),
    ExploreCategory(
        title = "Spiritual",
        items = listOf(
            ExploreItem("Meditation", Routes.Meditation.route),
            ExploreItem("Prayers", Routes.Prayers.route),
            ExploreItem("Mantra Counter", Routes.MantraCounter.route),
            ExploreItem("Puja Guide", Routes.PujaGuide.route),
            ExploreItem("Homa Guide", Routes.HomaGuide.route),
            ExploreItem("Vrat Calendar", Routes.VratCalendar.route),
            ExploreItem("Temple Finder", Routes.TempleFinder.route)
        )
    ),
    ExploreCategory(
        title = "Predictions",
        items = listOf(
            ExploreItem("Tarot", Routes.Tarot.route),
            ExploreItem("Palm Reading", Routes.PalmReading.route),
            ExploreItem("Face Reading", Routes.FaceReading.route),
            ExploreItem("Past Life", Routes.PastLife.route),
            ExploreItem("Pathfinder", Routes.Pathfinder.route),
            ExploreItem("Life Report", Routes.LifeReport.route)
        )
    )
)

@Composable
fun ExploreScreen(navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        exploreCategories.forEach { category ->
            item(span = { GridItemSpan(3) }) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                )
            }
            items(category.items) { item ->
                Card(
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.labelLarge,
                            color = TextPrimary
                        )
                    }
                }
            }
        }
    }
}
