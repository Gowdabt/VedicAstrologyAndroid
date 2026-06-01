package com.astrologyvedic.app.ui.screens.services

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.navigation.Routes
import com.astrologyvedic.app.ui.theme.*

data class ServiceItem(
    val title: String,
    val subtitle: String,
    val route: String,
    val iconChar: String,
    val iconColor: Color
)

data class ServiceCategory(
    val name: String,
    val items: List<ServiceItem>
)

private val serviceCategories = listOf(
    ServiceCategory(
        name = "Core Astrology",
        items = listOf(
            ServiceItem("Kundli", "Complete birth chart with Dashas & Yogas", Routes.Kundli.route, "☉", Color(0xFFf97316)),
            ServiceItem("Daily Horoscope", "Personalized daily predictions", Routes.Daily.route, "☽", Color(0xFF3b82f6)),
            ServiceItem("Kundli Match", "Ashta-Koota Guna Milan", Routes.Match.route, "♥", Color(0xFFec4899)),
            ServiceItem("Timeline", "Dasha & transit predictions", Routes.Timeline.route, "⟳", Color(0xFFa855f7)),
            ServiceItem("Panchang", "Daily Tithi, Nakshatra, Yoga & more", Routes.Panchang.route, "☸", Color(0xFFeab308)),
            ServiceItem("Transit Report", "Current planetary transits effect", Routes.Transit.route, "⊕", Color(0xFF06b6d4))
        )
    ),
    ServiceCategory(
        name = "AI-Powered",
        items = listOf(
            ServiceItem("AI Astrologer", "Chat with AI for personalized answers", Routes.AiChat.route, "✧", Color(0xFF8b5cf6)),
            ServiceItem("Palm Reading", "AI-powered palm line analysis", Routes.PalmReading.route, "✋", Color(0xFF7c3aed)),
            ServiceItem("Face Reading", "Personality from facial features", Routes.FaceReading.route, "◉", Color(0xFFf43f5e)),
            ServiceItem("Tarot", "Draw cards for mystical insights", Routes.Tarot.route, "✦", Color(0xFF6d28d9)),
            ServiceItem("Life Report", "Complete 8-section life analysis", Routes.LifeReport.route, "❋", Color(0xFFf59e0b)),
            ServiceItem("Pathfinder", "When will I marry? Job or business?", Routes.Pathfinder.route, "⊛", Color(0xFF10b981)),
            ServiceItem("Yoga Detection", "1000+ planetary yoga analysis", Routes.YogaDetection.route, "△", Color(0xFF14b8a6))
        )
    ),
    ServiceCategory(
        name = "Compatibility",
        items = listOf(
            ServiceItem("Love Compatibility", "Romantic compatibility check", Routes.LoveCompatibility.route, "♡", Color(0xFFec4899)),
            ServiceItem("Guna Milan", "36-point Nakshatra matching", Routes.GunaMilan.route, "⚭", Color(0xFFf97316)),
            ServiceItem("Name Match", "Compatibility from names", Routes.NameMatch.route, "A⟷B", Color(0xFF3b82f6)),
            ServiceItem("Porutham", "South Indian 10-point matching", Routes.Porutham.route, "⚜", Color(0xFF10b981))
        )
    ),
    ServiceCategory(
        name = "Astrology Systems",
        items = listOf(
            ServiceItem("KP Astrology", "Krishnamurti Paddhati predictions", Routes.KpAstrology.route, "KP", Color(0xFF06b6d4)),
            ServiceItem("Lal Kitab", "Red Book remedies & predictions", Routes.LalKitab.route, "◈", Color(0xFFef4444)),
            ServiceItem("Western", "Tropical zodiac with aspects", Routes.Western.route, "♈", Color(0xFF6366f1)),
            ServiceItem("Chinese Zodiac", "Animal sign & compatibility", Routes.ChineseZodiac.route, "龍", Color(0xFFdc2626)),
            ServiceItem("Nadi", "Nadi-based predictions", Routes.Nadi.route, "☯", Color(0xFF8b5cf6))
        )
    ),
    ServiceCategory(
        name = "Charts & Divisions",
        items = listOf(
            ServiceItem("Navamsa D9", "Marriage & spiritual chart", Routes.Navamsa.route, "D9", Color(0xFF7c3aed)),
            ServiceItem("Dasamsa D10", "Career divisional chart", Routes.Dasamsa.route, "D10", Color(0xFF14b8a6)),
            ServiceItem("Varga Charts", "All 16 divisional charts", Routes.VargaCharts.route, "D∞", Color(0xFFa855f7)),
            ServiceItem("Chart Comparison", "Side-by-side chart analysis", Routes.ChartComparison.route, "⇔", Color(0xFF3b82f6)),
            ServiceItem("Celebrity Charts", "Famous personalities' charts", Routes.Celebrity.route, "★", Color(0xFFf59e0b))
        )
    ),
    ServiceCategory(
        name = "Spiritual & Devotional",
        items = listOf(
            ServiceItem("Puja Guide", "Step-by-step worship instructions", Routes.PujaGuide.route, "🙏", Color(0xFFf97316)),
            ServiceItem("Jaap Counter", "Digital mala for chanting", Routes.MantraCounter.route, "⊙", Color(0xFFeab308)),
            ServiceItem("Festivals", "Hindu festivals & vrat calendar", Routes.Festivals.route, "◐", Color(0xFF10b981)),
            ServiceItem("Daily Mantra", "Personalized mantra for today", Routes.DailyMantra.route, "ॐ", Color(0xFFa855f7)),
            ServiceItem("Meditation", "Guided meditation with hora awareness", Routes.Meditation.route, "◎", Color(0xFF06b6d4)),
            ServiceItem("Prayers", "Stotra & prayer collection", Routes.Prayers.route, "☙", Color(0xFFf43f5e)),
            ServiceItem("Homa Guide", "DIY fire ritual instructions", Routes.HomaGuide.route, "♨", Color(0xFFef4444)),
            ServiceItem("Vrat Calendar", "Personalized fasting days", Routes.VratCalendar.route, "☾", Color(0xFF3b82f6))
        )
    ),
    ServiceCategory(
        name = "More",
        items = listOf(
            ServiceItem("Past Life", "Previous birth karma analysis", Routes.PastLife.route, "↺", Color(0xFF8b5cf6)),
            ServiceItem("Baby Names", "Nakshatra-based name suggestions", Routes.BabyNames.route, "Aa", Color(0xFFec4899)),
            ServiceItem("Lucky Today", "Daily lucky number & color", Routes.Lucky.route, "☘", Color(0xFFeab308)),
            ServiceItem("Stock Astrology", "Planetary market influence", Routes.StockAstrology.route, "↗", Color(0xFF10b981)),
            ServiceItem("Birth Rectification", "Calculate birth time from events", Routes.BirthRectification.route, "⧖", Color(0xFF3b82f6)),
            ServiceItem("Gemstone", "Personalized gemstone guide", Routes.Gemstone.route, "◆", Color(0xFF06b6d4)),
            ServiceItem("Vastu", "Directional & spatial analysis", Routes.Vastu.route, "⌂", Color(0xFFf97316))
        )
    )
)

@Composable
fun ServicesScreen(navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        serviceCategories.forEach { category ->
            item {
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = Saffron400,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 12.dp, bottom = 6.dp)
                )
            }
            items(category.items) { service ->
                ServiceCard(service = service, onClick = { navController.navigate(service.route) })
            }
        }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}

@Composable
private fun ServiceCard(service: ServiceItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(service.iconColor.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = service.iconChar,
                    color = service.iconColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(14.dp))
            // Text
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = service.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextTertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
            // Arrow
            Text(
                text = "›",
                color = TextTertiary,
                fontSize = 20.sp
            )
        }
    }
}
