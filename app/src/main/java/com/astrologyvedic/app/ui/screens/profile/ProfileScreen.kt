package com.astrologyvedic.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.astrologyvedic.app.data.local.entities.ProfileEntity
import com.astrologyvedic.app.ui.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Vedic Astrology",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Routes.Settings.route) }) {
                        Icon(
                            imageVector = Icons.Outlined.Settings,
                            contentDescription = "Settings"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            // --- Profile Header ---
            item {
                ProfileHeader(
                    profile = uiState.profile,
                    onEditClick = { navController.navigate(Routes.EditProfile.route) }
                )
            }

            // --- My Charts Section ---
            item {
                MyChartsSection(
                    profiles = uiState.profiles,
                    onViewAllClick = { navController.navigate(Routes.SavedCharts.route) }
                )
            }

            // --- Preferences Section ---
            item {
                PreferencesSection(
                    profile = uiState.profile,
                    navController = navController
                )
            }

            // --- Support Section ---
            item {
                SupportSection(navController = navController)
            }

            // --- Footer ---
            item {
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = "VERSION 2.4.0",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════════
// Profile Header
// ═══════════════════════════════════════════════════════════════════

@Composable
private fun ProfileHeader(
    profile: ProfileEntity?,
    onEditClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar with initials
        Surface(
            modifier = Modifier.size(64.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = profile?.name?.let { getInitials(it) } ?: "AK",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Name
        Text(
            text = profile?.name ?: "Arjun Kumar",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(4.dp))

        // Subtitle: DOB, time, place
        Text(
            text = profile?.let { "${it.dob}, ${it.time}, ${it.place}" }
                ?: "15 Aug 1990, 10:30 AM, Bangalore",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Edit Profile button
        OutlinedButton(
            onClick = onEditClick,
            shape = RoundedCornerShape(20.dp),
            border = ButtonDefaults.outlinedButtonBorder
        ) {
            Text(text = "Edit Profile")
        }
    }
}

// ═══════════════════════════════════════════════════════════════════
// My Charts Section
// ═══════════════════════════════════════════════════════════════════

@Composable
private fun MyChartsSection(
    profiles: List<ProfileEntity>,
    onViewAllClick: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Section header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "My Charts",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            TextButton(onClick = onViewAllClick) {
                Text(text = "View All")
            }
        }

        // Horizontal scrollable chart cards
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = profiles.ifEmpty { defaultChartItems() },
                key = { it.id }
            ) { profile ->
                ChartCard(profile = profile)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun ChartCard(profile: ProfileEntity) {
    Card(
        modifier = Modifier.width(140.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            // Image placeholder
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceDim),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.AutoGraph,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Name
            Text(
                text = profile.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            // Date
            Text(
                text = profile.dob,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// ═══════════════════════════════════════════════════════════════════
// Preferences Section
// ═══════════════════════════════════════════════════════════════════

@Composable
private fun PreferencesSection(
    profile: ProfileEntity?,
    navController: NavController
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = "Preferences")

        PreferenceItem(
            icon = Icons.Outlined.Language,
            title = "Language",
            value = "English",
            onClick = { navController.navigate(Routes.Language.route) }
        )
        PreferenceItem(
            icon = Icons.Outlined.Settings,
            title = "Ayanamsa System",
            value = profile?.ayanamsa?.replaceFirstChar { it.uppercase() } ?: "Lahiri",
            onClick = { navController.navigate(Routes.Settings.route) }
        )
        PreferenceItem(
            icon = Icons.Outlined.Palette,
            title = "Chart Style",
            value = profile?.chartStyle?.replace("_", " ")
                ?.split(" ")?.joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }
                ?: "North Indian",
            onClick = { navController.navigate(Routes.Settings.route) }
        )
        PreferenceItem(
            icon = Icons.Outlined.Notifications,
            title = "Notifications",
            value = "On",
            onClick = { navController.navigate(Routes.Notifications.route) }
        )
    }
}

@Composable
private fun PreferenceItem(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        supportingContent = null,
        leadingContent = {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = value,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.Outlined.ChevronRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        tonalElevation = 0.dp
    )
}

// ═══════════════════════════════════════════════════════════════════
// Support Section
// ═══════════════════════════════════════════════════════════════════

@Composable
private fun SupportSection(navController: NavController) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(title = "Support")

        SupportItem(
            icon = Icons.Outlined.HelpOutline,
            title = "Help",
            onClick = { navController.navigate(Routes.HelpSupport.route) }
        )
        SupportItem(
            icon = Icons.Outlined.Info,
            title = "About",
            onClick = { navController.navigate(Routes.About.route) }
        )
        SupportItem(
            icon = Icons.Outlined.Star,
            title = "Rate App",
            onClick = { navController.navigate(Routes.About.route) }
        )
        SupportItem(
            icon = Icons.Outlined.Share,
            title = "Share",
            onClick = { navController.navigate(Routes.About.route) }
        )
    }
}

@Composable
private fun SupportItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge
            )
        },
        leadingContent = {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        },
        trailingContent = {
            Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        },
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        tonalElevation = 0.dp
    )
}

// ═══════════════════════════════════════════════════════════════════
// Shared Components
// ═══════════════════════════════════════════════════════════════════

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp, bottom = 8.dp)
    )
}

// ═══════════════════════════════════════════════════════════════════
// Utilities
// ═══════════════════════════════════════════════════════════════════

private fun getInitials(name: String): String {
    val parts = name.trim().split(" ").filter { it.isNotBlank() }
    return when {
        parts.size >= 2 -> "${parts.first().first().uppercase()}${parts.last().first().uppercase()}"
        parts.size == 1 -> parts.first().take(2).uppercase()
        else -> "AK"
    }
}

private fun defaultChartItems(): List<ProfileEntity> {
    return listOf(
        ProfileEntity(
            id = -1,
            name = "Arjun Kumar",
            dob = "15 Aug 1990",
            time = "10:30 AM",
            place = "Bangalore",
            latitude = 12.97,
            longitude = 77.59,
            timezone = "Asia/Kolkata"
        ),
        ProfileEntity(
            id = -2,
            name = "Priya Sharma",
            dob = "22 Mar 1993",
            time = "6:15 AM",
            place = "Mumbai",
            latitude = 19.07,
            longitude = 72.87,
            timezone = "Asia/Kolkata"
        ),
        ProfileEntity(
            id = -3,
            name = "Ravi Patel",
            dob = "8 Nov 1985",
            time = "2:45 PM",
            place = "Delhi",
            latitude = 28.61,
            longitude = 77.23,
            timezone = "Asia/Kolkata"
        )
    )
}
