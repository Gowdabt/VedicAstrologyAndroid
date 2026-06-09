package com.astrologyvedic.app.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Construction
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Numbers
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.navigation.Routes

// ───── Data Models ─────

private data class QuickAction(
    val title: String,
    val icon: ImageVector,
    val route: String
)

private data class SpecialService(
    val title: String,
    val description: String,
    val route: String,
    val gradientColors: List<Color>
)

private val quickActions = listOf(
    QuickAction("Kundli", Icons.Outlined.WbSunny, Routes.Kundli.route),
    QuickAction("Match", Icons.Outlined.Favorite, Routes.Match.route),
    QuickAction("Daily", Icons.Outlined.Star, Routes.Daily.route),
    QuickAction("Panchang", Icons.Outlined.CalendarMonth, Routes.Panchang.route),
    QuickAction("Numerology", Icons.Outlined.Numbers, Routes.Numerology.route),
    QuickAction("Tarot", Icons.Outlined.Style, Routes.Tarot.route),
    QuickAction("Chat", Icons.Outlined.ChatBubbleOutline, Routes.Chat.route),
    QuickAction("Tools", Icons.Outlined.Construction, Routes.Tools.route)
)

private val specialServices = listOf(
    SpecialService(
        "AI Astrologer",
        "Get personalized predictions powered by AI",
        Routes.AiChat.route,
        listOf(Color(0xFF7c3aed), Color(0xFF4c1d95))
    ),
    SpecialService(
        "Life Report",
        "Complete 8-section life analysis report",
        Routes.LifeReport.route,
        listOf(Color(0xFFf59e0b), Color(0xFF92400e))
    ),
    SpecialService(
        "Palm Reading",
        "Upload your palm for AI analysis",
        Routes.PalmReading.route,
        listOf(Color(0xFF059669), Color(0xFF064e3b))
    )
)

private val zodiacSigns = listOf(
    "Aries" to "♈", "Taurus" to "♉", "Gemini" to "♊", "Cancer" to "♋",
    "Leo" to "♌", "Virgo" to "♍", "Libra" to "♎", "Scorpio" to "♏",
    "Sagittarius" to "♐", "Capricorn" to "♑", "Aquarius" to "♒", "Pisces" to "♓"
)

// ───── Screen ─────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomeViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Vedic Astrology",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(Icons.Outlined.Search, contentDescription = "Search")
                    }
                    IconButton(onClick = { navController.navigate(Routes.Settings.route) }) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. Today's Panchang Hero Card
            item {
                PanchangHeroCard(uiState = uiState, navController = navController)
            }

            // 2. Quick Access Grid
            item {
                QuickAccessGrid(navController = navController)
            }

            // 3. Special Services Section
            item {
                SpecialServicesSection(navController = navController)
            }

            // 4. Zodiac Signs Section
            item {
                ZodiacSignsSection(navController = navController)
            }
        }
    }
}

// ───── 1. Panchang Hero Card ─────

@Composable
private fun PanchangHeroCard(uiState: HomeUiState, navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        shape = MaterialTheme.shapes.large,
        onClick = { navController.navigate(Routes.Panchang.route) }
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // Decorative star element in top-right corner
            Icon(
                imageVector = Icons.Outlined.AutoAwesome,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp)
                    .size(24.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.3f)
            )

            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Label
                Text(
                    text = "TODAY'S PANCHANG",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                    fontWeight = FontWeight.SemiBold,
                    letterSpacing = 1.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Tithi name - large
                Text(
                    text = uiState.tithi.ifEmpty { "Loading..." },
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Nakshatra and Yoga chips
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PanchangChip(
                        label = uiState.nakshatra.ifEmpty { "---" }
                    )
                    PanchangChip(
                        label = uiState.yoga.ifEmpty { "---" }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Date
                Text(
                    text = uiState.todayDate,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun PanchangChip(label: String) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.1f),
        shape = RoundedCornerShape(20.dp)
    ) {
        Text(
            text = label,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Medium
        )
    }
}

// ───── 2. Quick Access Grid ─────

@Composable
private fun QuickAccessGrid(navController: NavController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        val rows = quickActions.chunked(4)
        rows.forEachIndexed { index, rowItems ->
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
            if (index < rows.lastIndex) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    action: QuickAction,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(56.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = action.icon,
                    contentDescription = action.title,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = action.title,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

// ───── 3. Special Services Section ─────

@Composable
private fun SpecialServicesSection(navController: NavController) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Special Services",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "View all",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable {
                    navController.navigate(Routes.Services.route)
                }
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Horizontal scrollable row of promo cards
        Row(
            modifier = Modifier
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            specialServices.forEach { service ->
                SpecialServiceCard(
                    service = service,
                    onClick = { navController.navigate(service.route) }
                )
            }
        }
    }
}

@Composable
private fun SpecialServiceCard(service: SpecialService, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(220.dp)
            .height(120.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        onClick = onClick
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.horizontalGradient(service.gradientColors),
                    shape = MaterialTheme.shapes.large
                )
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.White.copy(alpha = 0.8f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ───── 4. Zodiac Signs Section ─────

@Composable
private fun ZodiacSignsSection(navController: NavController) {
    Column(
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)
    ) {
        // Header
        Text(
            text = "Zodiac Signs",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // 3 rows x 4 columns grid
        val rows = zodiacSigns.chunked(4)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
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
private fun ZodiacItem(
    name: String,
    symbol: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.secondaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = symbol,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = name,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            fontSize = 10.sp
        )
    }
}
