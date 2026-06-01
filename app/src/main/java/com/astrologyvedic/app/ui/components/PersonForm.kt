package com.astrologyvedic.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.astrologyvedic.app.data.api.models.PersonRequest
import com.astrologyvedic.app.data.repository.LocationRepository
import com.astrologyvedic.app.ui.theme.BorderDark
import com.astrologyvedic.app.util.LocationHelper
import com.astrologyvedic.app.util.GpsLocationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.astrologyvedic.app.ui.theme.Saffron500
import com.astrologyvedic.app.ui.theme.SurfaceCard
import com.astrologyvedic.app.ui.theme.SurfaceCardElevated
import com.astrologyvedic.app.ui.theme.TextPrimary
import com.astrologyvedic.app.ui.theme.TextSecondary
import com.astrologyvedic.app.ui.theme.TextTertiary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

data class PersonFormState(
    val name: String = "",
    val dob: String = "",
    val time: String = "",
    val place: String = "",
    val latitude: String = "",
    val longitude: String = ""
)

fun PersonFormState.toPersonRequest(): PersonRequest? {
    if (name.isBlank() || dob.isBlank() || time.isBlank() || place.isBlank()) return null

    // If lat/long are empty, use default coordinates (New Delhi as a reasonable default for Vedic astrology)
    // Note: In a production app, you should implement geocoding to fetch coordinates from place name
    val lat = latitude.toDoubleOrNull() ?: 28.6139  // New Delhi latitude as default
    val lng = longitude.toDoubleOrNull() ?: 77.2090  // New Delhi longitude as default

    return PersonRequest(
        name = name,
        dob = dob,
        time = time,
        place = place,
        latitude = lat,
        longitude = lng,
        timezone = TimeZone.getDefault().id
    )
}

fun PersonFormState.isValid(): Boolean {
    return name.isNotBlank() && dob.isNotBlank() && time.isNotBlank() && place.isNotBlank()
}

/**
 * Internal ViewModel to provide LocationRepository and LocationHelper for PersonForm
 */
@HiltViewModel
class PersonFormLocationViewModel @Inject constructor(
    val locationRepository: LocationRepository,
    val locationHelper: LocationHelper
) : ViewModel()

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonForm(
    title: String = "Birth Details",
    state: PersonFormState,
    onStateChange: (PersonFormState) -> Unit,
    modifier: Modifier = Modifier,
    locationViewModel: PersonFormLocationViewModel = hiltViewModel(),
    autoDetectLocation: Boolean = false
) {
    val context = LocalContext.current
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var isLoadingLocation by remember { mutableStateOf(false) }
    var locationError by remember { mutableStateOf<String?>(null) }

    // Auto-detect location on first launch if requested
    LaunchedEffect(autoDetectLocation) {
        if (autoDetectLocation && state.place.isEmpty()) {
            isLoadingLocation = true
            locationViewModel.locationHelper.getCurrentLocation(context) { result ->
                isLoadingLocation = false
                when (result) {
                    is GpsLocationResult.Success -> {
                        onStateChange(
                            state.copy(
                                place = result.placeName,
                                latitude = result.latitude,
                                longitude = result.longitude
                            )
                        )
                        locationError = null
                    }
                    is GpsLocationResult.Error -> {
                        locationError = result.message
                    }
                }
            }
        }
    }

    // Handler for "Use Current Location" button
    val onUseCurrentLocation: () -> Unit = {
        isLoadingLocation = true
        locationError = null
        locationViewModel.locationHelper.getCurrentLocation(context) { result ->
            isLoadingLocation = false
            when (result) {
                is GpsLocationResult.Success -> {
                    onStateChange(
                        state.copy(
                            place = result.placeName,
                            latitude = result.latitude,
                            longitude = result.longitude
                        )
                    )
                    locationError = null
                }
                is GpsLocationResult.Error -> {
                    locationError = result.message
                }
            }
        }
    }

    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = Saffron500,
        unfocusedBorderColor = BorderDark,
        focusedTextColor = TextPrimary,
        unfocusedTextColor = TextPrimary,
        cursorColor = Saffron500,
        focusedContainerColor = SurfaceCard,
        unfocusedContainerColor = SurfaceCard,
        focusedLabelColor = Saffron500,
        unfocusedLabelColor = TextTertiary
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Name field
            OutlinedTextField(
                value = state.name,
                onValueChange = { onStateChange(state.copy(name = it)) },
                label = { Text("Full Name") },
                leadingIcon = {
                    Icon(Icons.Default.Person, contentDescription = null, tint = TextTertiary)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = MaterialTheme.shapes.small,
                singleLine = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // DOB field
            OutlinedTextField(
                value = state.dob,
                onValueChange = { onStateChange(state.copy(dob = it)) },
                label = { Text("Date of Birth (DD/MM/YYYY)") },
                leadingIcon = {
                    Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextTertiary)
                },
                trailingIcon = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Pick date", tint = Saffron500)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Time field
            OutlinedTextField(
                value = state.time,
                onValueChange = { onStateChange(state.copy(time = it)) },
                label = { Text("Birth Time (HH:MM)") },
                leadingIcon = {
                    Icon(Icons.Default.AccessTime, contentDescription = null, tint = TextTertiary)
                },
                trailingIcon = {
                    IconButton(onClick = { showTimePicker = true }) {
                        Icon(Icons.Default.AccessTime, contentDescription = "Pick time", tint = Saffron500)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                readOnly = true
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Place field with autocomplete
            LocationAutocompleteTextField(
                value = state.place,
                onValueChange = { onStateChange(state.copy(place = it)) },
                onLocationSelected = { location ->
                    // Extract city name from displayName (first part before comma)
                    val cityName = location.displayName.split(",").firstOrNull()?.trim()
                        ?: location.toShortDisplayName()

                    onStateChange(
                        state.copy(
                            place = cityName,
                            latitude = location.latitude,
                            longitude = location.longitude
                        )
                    )
                },
                locationRepository = locationViewModel.locationRepository,
                modifier = Modifier.fillMaxWidth(),
                label = "Birth Place",
                placeholder = "Start typing location..."
            )
            Spacer(modifier = Modifier.height(8.dp))

            // Use Current Location Button
            Button(
                onClick = onUseCurrentLocation,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Saffron500.copy(alpha = 0.1f),
                    contentColor = Saffron500
                ),
                enabled = !isLoadingLocation
            ) {
                if (isLoadingLocation) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Saffron500,
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Detecting location...")
                } else {
                    Icon(
                        Icons.Default.MyLocation,
                        contentDescription = "Use current location",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Use Current Location")
                }
            }

            // Show location error if any
            if (locationError != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = locationError!!,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Lat/Lng row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.latitude,
                    onValueChange = { onStateChange(state.copy(latitude = it)) },
                    label = { Text("Latitude") },
                    modifier = Modifier.weight(1f),
                    colors = textFieldColors,
                    shape = MaterialTheme.shapes.small,
                    singleLine = true
                )
                OutlinedTextField(
                    value = state.longitude,
                    onValueChange = { onStateChange(state.copy(longitude = it)) },
                    label = { Text("Longitude") },
                    modifier = Modifier.weight(1f),
                    colors = textFieldColors,
                    shape = MaterialTheme.shapes.small,
                    singleLine = true
                )
            }

            Text(
                text = "Leave lat/lng empty to use default coordinates (recommended: enter accurate coordinates for best results)",
                style = MaterialTheme.typography.bodySmall,
                color = TextTertiary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }

    // Date Picker Dialog
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
                        onStateChange(state.copy(dob = formatted))
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

    // Time Picker Dialog
    if (showTimePicker) {
        val timePickerState = rememberTimePickerState(
            initialHour = 12,
            initialMinute = 0,
            is24Hour = true
        )
        Dialog(onDismissRequest = { showTimePicker = false }) {
            Card(
                colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Select Birth Time",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TimePicker(state = timePickerState)
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel", color = TextSecondary)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = {
                            val formatted = String.format(
                                "%02d:%02d",
                                timePickerState.hour,
                                timePickerState.minute
                            )
                            onStateChange(state.copy(time = formatted))
                            showTimePicker = false
                        }) {
                            Text("OK", color = Saffron500)
                        }
                    }
                }
            }
        }
    }
}
