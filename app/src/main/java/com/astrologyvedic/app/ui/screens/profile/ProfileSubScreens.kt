package com.astrologyvedic.app.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.theme.*

// ═══ NOTIFICATIONS SCREEN ═══

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Notifications", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            var dailyHoroscope by remember { mutableStateOf(true) }
            var festivals by remember { mutableStateOf(true) }
            var rahuKaal by remember { mutableStateOf(false) }
            var muhurat by remember { mutableStateOf(false) }

            NotifCard("Daily Horoscope", "Get your personalized horoscope every morning", dailyHoroscope) { dailyHoroscope = it }
            NotifCard("Festival Reminders", "Upcoming Hindu festivals & vrat dates", festivals) { festivals = it }
            NotifCard("Rahu Kaal Alert", "Daily Rahu Kaal timing notification", rahuKaal) { rahuKaal = it }
            NotifCard("Muhurat Alerts", "Get notified of auspicious timings", muhurat) { muhurat = it }
        }
    }
}

@Composable
private fun NotifCard(title: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.Medium)
                Text(description, style = MaterialTheme.typography.bodySmall, color = TextTertiary)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Saffron500,
                    checkedTrackColor = Saffron500.copy(alpha = 0.3f)
                )
            )
        }
    }
}

// ═══ HELP & SUPPORT SCREEN ═══

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpSupportScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Help & Support", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FaqItem("How accurate are the predictions?", "Our calculations use Swiss Ephemeris with Lahiri Ayanamsa, the same used by professional Vedic astrologers. AI features provide interpretative insights based on classical texts.")
            FaqItem("Why does my Kundli look different from other apps?", "Different apps may use different Ayanamsa values (Lahiri, Raman, KP). You can change this in Settings. We default to Lahiri which is the most widely used in India.")
            FaqItem("Is my birth data safe?", "All data is stored locally on your device. We don't upload your birth details to any server unless you explicitly generate a report.")
            FaqItem("How does AI Astrologer work?", "The AI uses your birth chart data combined with classical Vedic astrology texts to provide personalized answers. It's not a generic horoscope — it's based on YOUR chart.")
            FaqItem("What is Rahu Kaal?", "Rahu Kaal is an inauspicious time period of approximately 1.5 hours each day, ruled by Rahu. It's calculated based on sunrise time for your location.")
            FaqItem("Contact Us", "Email: support@astrologyvedic.com\nWebsite: astrologyvedic.com")
        }
    }
}

@Composable
private fun FaqItem(question: String, answer: String) {
    var expanded by remember { mutableStateOf(false) }
    Card(
        onClick = { expanded = !expanded },
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(question, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.Medium)
            if (expanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(answer, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
            }
        }
    }
}

// ═══ LANGUAGE SCREEN ═══

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageScreen(navController: NavController) {
    var selectedLang by remember { mutableIntStateOf(0) }
    val languages = listOf(
        "English" to "Default",
        "हिन्दी (Hindi)" to "हिन्दी",
        "ಕನ್ನಡ (Kannada)" to "ಕನ್ನಡ",
        "தமிழ் (Tamil)" to "தமிழ்",
        "తెలుగు (Telugu)" to "తెలుగు"
    )

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Language", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    languages.forEachIndexed { index, (name, _) ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp, horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedLang == index,
                                onClick = { selectedLang = index },
                                colors = RadioButtonDefaults.colors(selectedColor = Saffron500)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(name, style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                        }
                        if (index < languages.size - 1) {
                            HorizontalDivider(color = Cosmic800, thickness = 0.5.dp, modifier = Modifier.padding(start = 56.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Language changes will apply to all predictions and reports.",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary
            )
        }
    }
}

// ═══ SAVED CHARTS SCREEN ═══

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SavedChartsScreen(navController: NavController) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Saved Charts", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        if (uiState.profiles.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("☉", fontSize = 48.sp, color = TextTertiary)
                Spacer(modifier = Modifier.height(12.dp))
                Text("No charts saved yet", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                Text("Generate a Kundli to save a chart", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                uiState.profiles.forEach { profile ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier.size(40.dp).background(Saffron500.copy(alpha = 0.12f), RoundedCornerShape(10.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(profile.name.firstOrNull()?.uppercase() ?: "?", color = Saffron500, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(profile.name, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.Medium)
                                Text("${profile.dob} • ${profile.place}", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                            }
                            if (profile.isDefault) {
                                Surface(color = Saffron500.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                                    Text("Default", modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 9.sp, color = Saffron400, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══ REPORT HISTORY SCREEN ═══

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportHistoryScreen(navController: NavController) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Report History", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        if (uiState.reportHistory.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("❋", fontSize = 48.sp, color = TextTertiary)
                Spacer(modifier = Modifier.height(12.dp))
                Text("No reports yet", style = MaterialTheme.typography.bodyLarge, color = TextSecondary)
                Text("Generate Kundli, Match, or any report to see history", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
                uiState.reportHistory.forEach { report ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(report.type, style = MaterialTheme.typography.bodyLarge, color = TextPrimary, fontWeight = FontWeight.Medium)
                                Text(java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault()).format(java.util.Date(report.createdAt)), style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                            }
                        }
                    }
                }
            }
        }
    }
}

// ═══ EDIT PROFILE SCREEN ═══

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var birthTime by remember { mutableStateOf("") }
    var birthPlace by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Edit Profile", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Saffron500,
                    unfocusedBorderColor = Cosmic700,
                    focusedLabelColor = Saffron500,
                    cursorColor = Saffron500,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )
            OutlinedTextField(
                value = birthDate,
                onValueChange = { birthDate = it },
                label = { Text("Birth Date (DD/MM/YYYY)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Saffron500,
                    unfocusedBorderColor = Cosmic700,
                    focusedLabelColor = Saffron500,
                    cursorColor = Saffron500,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )
            OutlinedTextField(
                value = birthTime,
                onValueChange = { birthTime = it },
                label = { Text("Birth Time (HH:MM)") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Saffron500,
                    unfocusedBorderColor = Cosmic700,
                    focusedLabelColor = Saffron500,
                    cursorColor = Saffron500,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )
            OutlinedTextField(
                value = birthPlace,
                onValueChange = { birthPlace = it },
                label = { Text("Birth Place") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Saffron500,
                    unfocusedBorderColor = Cosmic700,
                    focusedLabelColor = Saffron500,
                    cursorColor = Saffron500,
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(8.dp))

            FilledTonalButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = Saffron500,
                    contentColor = Color.White
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Text("Save Profile", modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
