package com.astrologyvedic.app.ui.screens.panchang

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material.icons.filled.WbTwilight
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.BorderDark
import com.astrologyvedic.app.ui.theme.Cosmic800
import com.astrologyvedic.app.ui.theme.Cosmic950
import com.astrologyvedic.app.ui.theme.Error
import com.astrologyvedic.app.ui.theme.Saffron400
import com.astrologyvedic.app.ui.theme.Saffron500
import com.astrologyvedic.app.ui.theme.SurfaceCard
import com.astrologyvedic.app.ui.theme.SurfaceCardElevated
import com.astrologyvedic.app.ui.theme.SurfaceDark
import com.astrologyvedic.app.ui.theme.TextPrimary
import com.astrologyvedic.app.ui.theme.TextSecondary
import com.astrologyvedic.app.ui.theme.TextTertiary
import com.astrologyvedic.app.ui.theme.Warning
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PanchangScreen(
    navController: NavController,
    viewModel: PanchangViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        TopAppBar(
            title = { Text("Panchang", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            actions = {
                IconButton(onClick = { /* TODO: Share */ }) {
                    Icon(Icons.Default.Share, contentDescription = "Share", tint = Saffron500)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        when {
            uiState.isLoading -> {
                LoadingState(message = "Calculating Panchang...")
            }
            uiState.error != null && uiState.panchangData == null -> {
                ErrorState(
                    message = uiState.error!!,
                    onRetry = { viewModel.loadPanchang() }
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Date selector
                    OutlinedTextField(
                        value = uiState.selectedDate,
                        onValueChange = {},
                        label = { Text("Date") },
                        leadingIcon = {
                            Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextTertiary)
                        },
                        trailingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = "Pick date", tint = Saffron500)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Saffron500,
                            unfocusedBorderColor = BorderDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = SurfaceCard,
                            unfocusedContainerColor = SurfaceCard
                        ),
                        shape = MaterialTheme.shapes.small
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Location field
                    OutlinedTextField(
                        value = uiState.location,
                        onValueChange = { viewModel.updateLocation(it) },
                        label = { Text("Location (optional)") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextTertiary)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Saffron500,
                            unfocusedBorderColor = BorderDark,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedContainerColor = SurfaceCard,
                            unfocusedContainerColor = SurfaceCard
                        ),
                        shape = MaterialTheme.shapes.small,
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    uiState.panchangData?.let { data ->
                        // Sunrise/Sunset card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.WbSunny,
                                        contentDescription = "Sunrise",
                                        tint = Saffron400,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Sunrise", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                                    Text(data.sunrise, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(
                                        Icons.Default.WbTwilight,
                                        contentDescription = "Sunset",
                                        tint = Saffron400,
                                        modifier = Modifier.size(28.dp)
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Sunset", style = MaterialTheme.typography.bodySmall, color = TextTertiary)
                                    Text(data.sunset, style = MaterialTheme.typography.titleSmall, color = TextPrimary, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Main Panchang data
                        ResultCard(title = "Panchang Details") {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                PanchangRow("Tithi", data.tithi)
                                HorizontalDivider(color = Cosmic800.copy(alpha = 0.5f))
                                PanchangRow("Nakshatra", data.nakshatra)
                                HorizontalDivider(color = Cosmic800.copy(alpha = 0.5f))
                                PanchangRow("Yoga", data.yoga)
                                HorizontalDivider(color = Cosmic800.copy(alpha = 0.5f))
                                PanchangRow("Karana", data.karana)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Inauspicious timings
                        ResultCard(title = "Inauspicious Timings") {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                TimingRow("Rahu Kaal", data.rahuKaal, Error)
                                HorizontalDivider(color = Cosmic800.copy(alpha = 0.5f))
                                TimingRow("Yamaganda", data.yamaganda, Warning)
                                HorizontalDivider(color = Cosmic800.copy(alpha = 0.5f))
                                TimingRow("Gulika", data.gulika, Warning)
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                        sdf.timeZone = TimeZone.getTimeZone("UTC")
                        val formatted = sdf.format(Date(millis))
                        viewModel.updateDate(formatted)
                    }
                    showDatePicker = false
                }) {
                    Text("OK", color = Saffron500)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel", color = TextSecondary)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

@Composable
private fun PanchangRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = TextTertiary
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun TimingRow(
    label: String,
    value: String,
    indicatorColor: androidx.compose.ui.graphics.Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(indicatorColor, shape = androidx.compose.foundation.shape.CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = TextTertiary
            )
        }
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = TextPrimary,
            fontWeight = FontWeight.Medium
        )
    }
}
