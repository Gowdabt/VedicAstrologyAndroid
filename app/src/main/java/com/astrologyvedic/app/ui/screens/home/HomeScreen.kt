package com.astrologyvedic.app.ui.screens.home

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.navigation.Routes
import com.astrologyvedic.app.ui.theme.*

// ───── Data Models ─────

private data class QuickAction(
    val title: String,
    val icon: String,
    val route: String,
    val color: Color
)

private data class PromoCard(
    val title: String,
    val subtitle: String,
    val icon: String,
    val route: String,
    val gradientColors: List<Color>
)

private val quickGrid = listOf(
    QuickAction("Kundli", "☉", Routes.Kundli.route, Color(0xFFf97316)),
    QuickAction("Daily", "☽", Routes.Daily.route, Color(0xFF3b82f6)),
    QuickAction("Match", "♥", Routes.Match.route, Color(0xFFec4899)),
    QuickAction("Panchang", "☸", Routes.Panchang.route, Color(0xFFeab308)),
    QuickAction("AI Chat", "✧", Routes.AiChat.route, Color(0xFF8b5cf6)),
    QuickAction("Tarot", "✦", Routes.Tarot.route, Color(0xFF6d28d9)),
    QuickAction("Palm", "✋", Routes.PalmReading.route, Color(0xFF7c3aed)),
    QuickAction("Muhurat", "✓", Routes.Muhurat.route, Color(0xFF10b981))
)

private val promoCards = listOf(
    PromoCard(
        "AI Astrologer",
        "Get personalized answers from AI",
        "✧",
        Routes.AiChat.route,
        listOf(Color(0xFF7c3aed), Color(0xFF4c1d95))
    ),
    PromoCard(
        "Palm Reading",
        "Upload your palm for AI analysis",
        "✋",
        Routes.PalmReading.route,
        listOf(Color(0xFF6d28d9), Color(0xFF3b0764))
    ),
    PromoCard(
        "Life Report",
        "Complete 8-section life analysis",
        "❋",
        Routes.LifeReport.route,
        listOf(Color(0xFFf59e0b), Color(0xFF92400e))
    )
)

private val rashiSigns = listOf(
    "Mesh" to "♈", "Vrishabh" to "♉", "Mithun" to "♊",
    "Kark" to "♋", "Simha" to "♌", "Kanya" to "♍",
    "Tula" to "♎", "Vrishchik" to "♏", "Dhanu" to "♐",
    "Makar" to "♑", "Kumbh" to "♒", "Meen" to "♓"
)

// ───── Screen ─────

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // ═══ 1. COSMIC GREETING / TODAY'S ENERGY ═══
        item {
            CosmicHeroCard(uiState = uiState, navController = navController)
        }

        // ═══ 2. QUICK ACCESS GRID (4x2) ═══
        item {
            QuickAccessGrid(navController = navController)
        }

        // ═══ 3. PROMO CARDS (large visual cards) ═══
        item {
            PromoSection(navController = navController)
        }

        // ═══ 4. DAILY HOROSCOPE (zodiac grid) ═══
        item {
            HoroscopeSection(navController = navController)
        }

        // ═══ 5. SYSTEM FOOTER ═══
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SystemChip("Parashari")
                SystemChip("•")
                SystemChip("Lahiri Ayanamsa")
                SystemChip("•")
                SystemChip("Swiss Ephemeris")
            }
        }
    }
}

// ───── 1. HERO CARD ─────

@Composable
private fun CosmicHeroCard(uiState: HomeUiState, navController: NavController) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1a0a2e),
                        Color(0xFF0f0720),
                        Cosmic950
                    )
                )
            )
            .padding(horizontal = 16.dp, vertical = 20.dp)
    ) {
        Column {
            // Greeting
            Text(
                text = "Namaste 🙏",
                style = MaterialTheme.typography.headlineSmall,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = uiState.todayDate,
                style = MaterialTheme.typography.bodyMedium,
                color = TextTertiary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Today's Panchang Summary — matching web layout
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1a1035)),
                shape = RoundedCornerShape(16.dp),
                onClick = { navController.navigate(Routes.Panchang.route) }
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Header
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Today's Panchang",
                            style = MaterialTheme.typography.titleMedium,
                            color = Saffron400,
                            fontWeight = FontWeight.Bold
                        )
                        Surface(
                            color = Saffron500.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp),
                            modifier = Modifier.clickable {
                                val shareText = buildString {
                                    append("📅 Today's Panchang - ${uiState.todayDate}\n\n")
                                    append("🌙 Tithi: ${uiState.tithi.ifEmpty { "—" }}\n")
                                    append("⭐ Nakshatra: ${uiState.nakshatra.ifEmpty { "—" }}\n")
                                    append("🔴 Rahu Kala: ${uiState.rahuKaal.ifEmpty { "—" }}\n")
                                    append("🟡 Yamaganda: ${uiState.yamaganda.ifEmpty { "—" }}\n")
                                    append("🟣 Gulika: ${uiState.gulika.ifEmpty { "—" }}\n\n")
                                    append("Shared via Vedic Astrology App")
                                }
                                val sendIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                    type = "text/plain"
                                }
                                val shareIntent = Intent.createChooser(sendIntent, "Share Panchang")
                                context.startActivity(shareIntent)
                            }
                        ) {
                            Text(
                                text = "Share",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Saffron400,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Date
                    Text(
                        text = uiState.todayDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Panchang rows — like web screenshot
                    PanchangInfoRow(label = "Tithi:", value = uiState.tithi, labelColor = Saffron400)
                    PanchangInfoRow(label = "Nakshatra:", value = uiState.nakshatra, labelColor = Saffron400)
                    PanchangInfoRow(label = "Rahu Kala:", value = uiState.rahuKaal, labelColor = Color(0xFFef4444))
                    PanchangInfoRow(label = "Yamaganda:", value = uiState.yamaganda, labelColor = Color(0xFFf59e0b))
                    PanchangInfoRow(label = "Gulika:", value = uiState.gulika, labelColor = Color(0xFFa855f7))

                    Spacer(modifier = Modifier.height(12.dp))

                    // CTA
                    Text(
                        text = "Open Detailed Panchang →",
                        style = MaterialTheme.typography.labelMedium,
                        color = Saffron500,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun PanchangInfoRow(label: String, value: String, labelColor: Color = Saffron400) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = labelColor,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.width(110.dp)
        )
        Text(
            text = value.ifEmpty { "—" },
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Normal
        )
    }
}

// ───── 2. QUICK ACCESS GRID ─────

@Composable
private fun QuickAccessGrid(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        // 2 rows x 4 cols
        val rows = quickGrid.chunked(4)
        rows.forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                rowItems.forEach { action ->
                    QuickActionItem(
                        action = action,
                        onClick = { navController.navigate(action.route) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun QuickActionItem(action: QuickAction, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(action.color.copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = action.icon,
                fontSize = 22.sp,
                color = action.color
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = action.title,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp
        )
    }
}

// ───── 3. PROMO SECTION ─────

@Composable
private fun PromoSection(navController: NavController) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text(
                text = "AI-Powered Features",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(8.dp))
            Surface(
                color = Color(0xFF8b5cf6).copy(alpha = 0.2f),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "NEW",
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                    fontSize = 9.sp,
                    color = Color(0xFFc4b5fd),
                    fontWeight = FontWeight.Bold
                )
            }
        }

        promoCards.forEach { card ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                onClick = { navController.navigate(card.route) }
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.horizontalGradient(card.gradientColors),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.White.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = card.icon, fontSize = 24.sp)
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = card.title,
                                style = MaterialTheme.typography.titleSmall,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = card.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                        }
                        Text(
                            text = "→",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 20.sp
                        )
                    }
                }
            }
        }
    }
}

// ───── 4. HOROSCOPE SECTION ─────

@Composable
private fun HoroscopeSection(navController: NavController) {
    Column(modifier = Modifier.padding(top = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Today's Horoscope",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View All →",
                style = MaterialTheme.typography.labelMedium,
                color = Saffron500,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { navController.navigate(Routes.Daily.route) }
            )
        }

        // 3x4 grid of zodiac signs
        val rows = rashiSigns.chunked(4)
        Column(
            modifier = Modifier.padding(horizontal = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            rows.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    rowItems.forEach { (name, symbol) ->
                        ZodiacItem(
                            name = name,
                            symbol = symbol,
                            onClick = { navController.navigate(Routes.Daily.route) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ZodiacItem(name: String, symbol: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Saffron500.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = symbol,
                fontSize = 20.sp,
                color = Saffron400
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary,
            textAlign = TextAlign.Center,
            fontSize = 10.sp
        )
    }
}


// ───── SYSTEM FOOTER ─────

@Composable
private fun SystemChip(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = TextTertiary,
        fontSize = 9.sp
    )
}
