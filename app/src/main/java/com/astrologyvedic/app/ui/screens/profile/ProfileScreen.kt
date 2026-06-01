package com.astrologyvedic.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.navigation.Routes
import com.astrologyvedic.app.ui.theme.*

data class ProfileMenuItem(
    val icon: ImageVector,
    val title: String,
    val subtitle: String = "",
    val route: String,
    val iconTint: Color = TextSecondary
)

@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // ═══ PROFILE HEADER ═══
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(16.dp),
                onClick = { navController.navigate(Routes.EditProfile.route) }
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(Saffron500.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Person,
                            contentDescription = "Profile",
                            modifier = Modifier.size(32.dp),
                            tint = Saffron500
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = uiState.profile?.name ?: "Guest User",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = if (uiState.profile != null) "Tap to edit profile" else "Tap to add birth details",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextTertiary
                        )
                    }
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }

        // ═══ SAVED CHARTS ═══
        item {
            SectionTitle("My Charts")
        }
        item {
            MenuCard(
                items = listOf(
                    ProfileMenuItem(
                        icon = Icons.Outlined.AutoGraph,
                        title = "Saved Charts",
                        subtitle = "${uiState.profiles.size} profiles saved",
                        route = Routes.SavedCharts.route,
                        iconTint = Color(0xFF3b82f6)
                    ),
                    ProfileMenuItem(
                        icon = Icons.Outlined.History,
                        title = "Report History",
                        subtitle = "${uiState.reportHistory.size} reports generated",
                        route = Routes.ReportHistory.route,
                        iconTint = Color(0xFFa855f7)
                    )
                ),
                navController = navController
            )
        }

        // ═══ PREFERENCES ═══
        item {
            SectionTitle("Preferences")
        }
        item {
            MenuCard(
                items = listOf(
                    ProfileMenuItem(
                        icon = Icons.Outlined.Language,
                        title = "Language",
                        subtitle = "English",
                        route = Routes.Language.route,
                        iconTint = Color(0xFF10b981)
                    ),
                    ProfileMenuItem(
                        icon = Icons.Outlined.Notifications,
                        title = "Notifications",
                        subtitle = "Daily horoscope, festivals",
                        route = Routes.Notifications.route,
                        iconTint = Color(0xFFf59e0b)
                    ),
                    ProfileMenuItem(
                        icon = Icons.Outlined.Settings,
                        title = "Settings",
                        subtitle = "Theme, calculations, ayanamsa",
                        route = Routes.Settings.route,
                        iconTint = Color(0xFF6b7280)
                    )
                ),
                navController = navController
            )
        }

        // ═══ SUPPORT ═══
        item {
            SectionTitle("Support")
        }
        item {
            MenuCard(
                items = listOf(
                    ProfileMenuItem(
                        icon = Icons.AutoMirrored.Filled.HelpCenter,
                        title = "Help & Support",
                        subtitle = "FAQs, contact us",
                        route = Routes.HelpSupport.route,
                        iconTint = Color(0xFF06b6d4)
                    ),
                    ProfileMenuItem(
                        icon = Icons.Outlined.Star,
                        title = "Rate Us",
                        subtitle = "Love the app? Rate on Play Store",
                        route = Routes.About.route,
                        iconTint = Color(0xFFeab308)
                    ),
                    ProfileMenuItem(
                        icon = Icons.Outlined.Share,
                        title = "Share App",
                        subtitle = "Tell friends about Vedic Astrology",
                        route = Routes.About.route,
                        iconTint = Color(0xFFec4899)
                    ),
                    ProfileMenuItem(
                        icon = Icons.Outlined.Info,
                        title = "About",
                        subtitle = "Version 1.0.0",
                        route = Routes.About.route,
                        iconTint = Color(0xFF8b5cf6)
                    )
                ),
                navController = navController
            )
        }

        // Footer
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Vedic Astrology v1.0.0",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary,
                fontSize = 10.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = TextTertiary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
private fun MenuCard(items: List<ProfileMenuItem>, navController: NavController) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column {
            items.forEachIndexed { index, item ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { navController.navigate(item.route) })
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(item.iconTint.copy(alpha = 0.12f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            tint = item.iconTint,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = item.title,
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextPrimary,
                            fontWeight = FontWeight.Medium
                        )
                        if (item.subtitle.isNotEmpty()) {
                            Text(
                                text = item.subtitle,
                                style = MaterialTheme.typography.bodySmall,
                                color = TextTertiary,
                                fontSize = 11.sp
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = null,
                        tint = TextTertiary,
                        modifier = Modifier.size(18.dp)
                    )
                }
                if (index < items.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(start = 66.dp),
                        color = Cosmic800,
                        thickness = 0.5.dp
                    )
                }
            }
        }
    }
}

