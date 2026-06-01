package com.astrologyvedic.app.ui.screens.about

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("About", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // App icon placeholder
            Card(
                modifier = Modifier.size(80.dp),
                colors = CardDefaults.cardColors(containerColor = Saffron500.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("☉", fontSize = 36.sp, color = Saffron500)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            Text("Vedic Astrology", style = MaterialTheme.typography.headlineSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
            Text("Version 1.0.0", style = MaterialTheme.typography.bodySmall, color = TextTertiary)

            Spacer(modifier = Modifier.height(24.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Authentic Vedic astrology calculations powered by Swiss Ephemeris with Lahiri Ayanamsa. Using traditional Parashari system with Vimshottari Dasha.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoRow("Engine", "Swiss Ephemeris")
                    InfoRow("Ayanamsa", "Lahiri (Chitrapaksha)")
                    InfoRow("System", "Parashari")
                    InfoRow("Dasha", "Vimshottari")
                    InfoRow("House System", "Whole Sign (Rashi)")
                    InfoRow("Website", "astrologyvedic.com")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "© 2024 AstrologyVedic. All rights reserved.",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary,
                fontSize = 11.sp
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodySmall, color = TextTertiary)
        Text(value, style = MaterialTheme.typography.bodySmall, color = TextPrimary, fontWeight = FontWeight.Medium)
    }
}
