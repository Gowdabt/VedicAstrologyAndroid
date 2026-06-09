package com.astrologyvedic.app.ui.screens.services

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.SelfImprovement
import androidx.compose.material.icons.outlined.Stars
import androidx.compose.material.icons.outlined.Style
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.navigation.Routes

// --- Data models ---

private data class ServiceItem(
    val title: String,
    val description: String,
    val route: String,
    val icon: ImageVector,
    val iconTint: @Composable () -> Color
)

private data class ServiceCategory(
    val name: String,
    val items: List<ServiceItem>
)

// --- Service data ---

@Composable
private fun serviceCategories(): List<ServiceCategory> {
    val cs = MaterialTheme.colorScheme
    return listOf(
        ServiceCategory(
            name = "BIRTH CHART",
            items = listOf(
                ServiceItem(
                    title = "Kundli",
                    description = "Your detailed birth map and planetary positions.",
                    route = Routes.Kundli.route,
                    icon = Icons.Outlined.Stars,
                    iconTint = { cs.primary }
                ),
                ServiceItem(
                    title = "Navamsa",
                    description = "The D9 chart for marriage and hidden strengths.",
                    route = Routes.Navamsa.route,
                    icon = Icons.Outlined.Stars,
                    iconTint = { cs.primary }
                )
            )
        ),
        ServiceCategory(
            name = "COMPATIBILITY",
            items = listOf(
                ServiceItem(
                    title = "Match Making",
                    description = "In-depth synastry report for couples.",
                    route = Routes.Match.route,
                    icon = Icons.Outlined.FavoriteBorder,
                    iconTint = { cs.error }
                ),
                ServiceItem(
                    title = "Guna Milan",
                    description = "Traditional 36-point ashtakoot matching.",
                    route = Routes.GunaMilan.route,
                    icon = Icons.Outlined.FavoriteBorder,
                    iconTint = { cs.error }
                )
            )
        ),
        ServiceCategory(
            name = "DAILY PREDICTIONS",
            items = listOf(
                ServiceItem(
                    title = "Daily Horoscope",
                    description = "Your personalized forecast for today.",
                    route = Routes.Daily.route,
                    icon = Icons.Outlined.CalendarMonth,
                    iconTint = { cs.tertiary }
                ),
                ServiceItem(
                    title = "Panchang",
                    description = "Auspicious times and tithi details.",
                    route = Routes.Panchang.route,
                    icon = Icons.Outlined.CalendarMonth,
                    iconTint = { cs.tertiary }
                )
            )
        ),
        ServiceCategory(
            name = "REMEDIES",
            items = listOf(
                ServiceItem(
                    title = "Gemstone",
                    description = "Recommendation for strengthening planets.",
                    route = Routes.Gemstone.route,
                    icon = Icons.Outlined.Diamond,
                    iconTint = { cs.secondary }
                ),
                ServiceItem(
                    title = "Puja Guide",
                    description = "Vedic rituals for prosperity and peace.",
                    route = Routes.PujaGuide.route,
                    icon = Icons.Outlined.SelfImprovement,
                    iconTint = { cs.secondary }
                )
            )
        ),
        ServiceCategory(
            name = "TOOLS",
            items = listOf(
                ServiceItem(
                    title = "Numerology",
                    description = "Explore the power of numbers in your life.",
                    route = Routes.Numerology.route,
                    icon = Icons.Outlined.Calculate,
                    iconTint = { cs.primary }
                ),
                ServiceItem(
                    title = "Tarot",
                    description = "Draw cards for mystical insights.",
                    route = Routes.Tarot.route,
                    icon = Icons.Outlined.Style,
                    iconTint = { cs.tertiary }
                ),
                ServiceItem(
                    title = "Temple Finder",
                    description = "Locate nearby temples and sacred sites.",
                    route = Routes.TempleFinder.route,
                    icon = Icons.Outlined.Place,
                    iconTint = { cs.error }
                )
            )
        )
    )
}

// --- Screen composable ---

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ServicesScreen(navController: NavController) {
    val categories = serviceCategories()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Services",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.Explore.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = { navController.navigate(Routes.Explore.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.FilterList,
                            contentDescription = "Filter"
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            SmallFloatingActionButton(
                onClick = { navController.navigate(Routes.AiChat.route) },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = "Chat"
                )
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // Hero banner card
            item {
                HeroBannerCard()
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Categorized service list
            categories.forEach { category ->
                item {
                    Text(
                        text = category.name,
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                }
                items(category.items) { service ->
                    ServiceListItem(
                        service = service,
                        onClick = { navController.navigate(service.route) }
                    )
                }
            }

            // Bottom spacer for FAB clearance
            item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

// --- Hero banner ---

@Composable
private fun HeroBannerCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Explore the Cosmos",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Expert Vedic guidance tailored to your celestial alignment.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
            )
        }
    }
}

// --- Service list item ---

@Composable
private fun ServiceListItem(service: ServiceItem, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        color = Color.Transparent,
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        ListItem(
            headlineContent = {
                Text(
                    text = service.title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Medium
                )
            },
            supportingContent = {
                Text(
                    text = service.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            leadingContent = {
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = service.iconTint().copy(alpha = 0.12f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = service.icon,
                            contentDescription = service.title,
                            tint = service.iconTint(),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            },
            trailingContent = {
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = "Navigate",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ListItemDefaults.colors(
                containerColor = Color.Transparent
            ),
            tonalElevation = 0.dp
        )
    }
}
