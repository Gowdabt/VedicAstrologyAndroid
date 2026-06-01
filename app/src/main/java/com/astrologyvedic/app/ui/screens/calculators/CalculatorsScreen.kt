package com.astrologyvedic.app.ui.screens.calculators

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.navigation.Routes
import com.astrologyvedic.app.ui.theme.*

data class CalculatorItem(
    val title: String,
    val subtitle: String,
    val route: String,
    val iconChar: String,
    val iconColor: Color
)

data class CalcCategory(
    val name: String,
    val items: List<CalculatorItem>
)

private val calcCategories = listOf(
    CalcCategory(
        name = "Daily Timings",
        items = listOf(
            CalculatorItem("Rahu Kaal", "Inauspicious time", Routes.RahuKaal.route, "◐", Color(0xFFef4444)),
            CalculatorItem("Choghadiya", "Time slots", Routes.Choghadiya.route, "◷", Color(0xFF06b6d4)),
            CalculatorItem("Hora", "Planetary hours", Routes.Hora.route, "⏱", Color(0xFFf59e0b)),
            CalculatorItem("Muhurat", "Best timing", Routes.Muhurat.route, "✓", Color(0xFF10b981)),
            CalculatorItem("Sunrise", "Solar times", Routes.SunriseSunset.route, "☀", Color(0xFFf97316)),
            CalculatorItem("Ayanamsa", "Precession", Routes.Ayanamsa.route, "∠", Color(0xFFa855f7))
        )
    ),
    CalcCategory(
        name = "Dosha & Transit",
        items = listOf(
            CalculatorItem("Sade Sati", "Saturn transit", Routes.SadeSati.route, "♄", Color(0xFF3b82f6)),
            CalculatorItem("Kaal Sarp", "Dosha check", Routes.Kaalsarp.route, "☊", Color(0xFFf97316)),
            CalculatorItem("Numerology", "Name numbers", Routes.Numerology.route, "∑", Color(0xFFa855f7)),
            CalculatorItem("Lucky", "Today's lucky", Routes.Lucky.route, "☘", Color(0xFFeab308))
        )
    ),
    CalcCategory(
        name = "Remedies & Guides",
        items = listOf(
            CalculatorItem("Gemstone", "Gem finder", Routes.Gemstone.route, "◆", Color(0xFF06b6d4)),
            CalculatorItem("Vastu", "Direction tips", Routes.Vastu.route, "⌂", Color(0xFFf97316)),
            CalculatorItem("Temple", "Nearby temples", Routes.TempleFinder.route, "🛕", Color(0xFFec4899)),
            CalculatorItem("Mantra", "Japa counter", Routes.MantraCounter.route, "ॐ", Color(0xFFeab308))
        )
    )
)

@Composable
fun CalculatorsScreen(navController: NavController) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        calcCategories.forEach { category ->
            item(span = { GridItemSpan(3) }) {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Saffron400,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp, bottom = 4.dp)
                )
            }
            items(category.items) { calc ->
                CalcCard(item = calc, onClick = { navController.navigate(calc.route) })
            }
        }
        item(span = { GridItemSpan(3) }) {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun CalcCard(item: CalculatorItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(item.iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = item.iconChar,
                    color = item.iconColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelMedium,
                color = TextPrimary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary,
                fontSize = 9.sp,
                textAlign = TextAlign.Center,
                lineHeight = 12.sp,
                maxLines = 1
            )
        }
    }
}
