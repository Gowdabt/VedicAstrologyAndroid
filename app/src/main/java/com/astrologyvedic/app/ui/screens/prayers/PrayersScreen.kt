package com.astrologyvedic.app.ui.screens.prayers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PrayersScreen(
    navController: NavController,
    viewModel: PrayersViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Prayers & Stotras", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            uiState.deities.forEachIndexed { index, deity ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.toggleDeity(index) }
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(deity.deity, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                            Icon(
                                if (deity.isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null, tint = Saffron500
                            )
                        }

                        AnimatedVisibility(visible = deity.isExpanded) {
                            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                                deity.prayers.forEach { prayer ->
                                    HorizontalDivider(color = BorderDark)
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(prayer.title, style = MaterialTheme.typography.titleSmall, color = Saffron400)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(prayer.text, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                                    Spacer(modifier = Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
