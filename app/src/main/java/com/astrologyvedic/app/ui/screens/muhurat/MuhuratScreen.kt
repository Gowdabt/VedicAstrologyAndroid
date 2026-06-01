package com.astrologyvedic.app.ui.screens.muhurat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MuhuratScreen(
    navController: NavController,
    viewModel: MuhuratViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Muhurat", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Text("Find auspicious timings", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(16.dp))

                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        // Activity dropdown
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                            OutlinedTextField(
                                value = uiState.selectedActivity,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Activity Type") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark,
                                    focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                                    focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard
                                ),
                                shape = MaterialTheme.shapes.small
                            )
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                viewModel.activities.forEach { activity ->
                                    DropdownMenuItem(
                                        text = { Text(activity) },
                                        onClick = { viewModel.updateActivity(activity); expanded = false }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = uiState.startDate,
                            onValueChange = { viewModel.updateStartDate(it) },
                            label = { Text("Start Date (DD/MM/YYYY)") },
                            leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextTertiary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                                focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard
                            ),
                            shape = MaterialTheme.shapes.small, singleLine = true
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = uiState.endDate,
                            onValueChange = { viewModel.updateEndDate(it) },
                            label = { Text("End Date (DD/MM/YYYY)") },
                            leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextTertiary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary,
                                focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard
                            ),
                            shape = MaterialTheme.shapes.small, singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FilledTonalButton(
                    onClick = { viewModel.findMuhurat() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.selectedActivity.isNotBlank(),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Find Muhurat", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            if (uiState.isLoading) { LoadingState(message = "Finding auspicious timings...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.findMuhurat() }) }

            if (uiState.hasResult) {
                Text("Auspicious Windows for ${uiState.selectedActivity}", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                if (uiState.windows.isEmpty()) {
                    Text("No auspicious windows found in the selected date range.", style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                } else {
                    uiState.windows.forEach { window ->
                        ResultCard(title = window.date) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("${window.startTime} - ${window.endTime}", style = MaterialTheme.typography.bodyLarge, color = TextPrimary)
                                Text(window.quality, style = MaterialTheme.typography.bodyMedium,
                                    color = if (window.quality == "Excellent") Saffron500 else Success, fontWeight = FontWeight.Medium)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
