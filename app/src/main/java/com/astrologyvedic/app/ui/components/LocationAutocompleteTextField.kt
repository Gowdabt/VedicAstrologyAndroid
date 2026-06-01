package com.astrologyvedic.app.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.astrologyvedic.app.data.api.models.LocationSuggestion
import com.astrologyvedic.app.data.repository.LocationRepository
import com.astrologyvedic.app.ui.theme.BorderDark
import com.astrologyvedic.app.ui.theme.Cosmic800
import com.astrologyvedic.app.ui.theme.Saffron500
import com.astrologyvedic.app.ui.theme.SurfaceCard
import com.astrologyvedic.app.ui.theme.SurfaceCardElevated
import com.astrologyvedic.app.ui.theme.TextPrimary
import com.astrologyvedic.app.ui.theme.TextSecondary
import com.astrologyvedic.app.ui.theme.TextTertiary
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(FlowPreview::class)
@Composable
fun LocationAutocompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onLocationSelected: (LocationSuggestion) -> Unit,
    locationRepository: LocationRepository,
    modifier: Modifier = Modifier,
    label: String = "Birth Place",
    placeholder: String = "Start typing location..."
) {
    var suggestions by remember { mutableStateOf<List<LocationSuggestion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showSuggestions by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val searchQuery = remember { MutableStateFlow("") }

    // Debounced search effect
    LaunchedEffect(Unit) {
        searchQuery
            .debounce(500) // Wait 500ms after user stops typing
            .distinctUntilChanged()
            .collect { query ->
                if (query.length >= 3) {
                    isLoading = true
                    errorMessage = null
                    locationRepository.searchLocations(query)
                        .onSuccess { results ->
                            suggestions = results
                            showSuggestions = results.isNotEmpty()
                            isLoading = false
                        }
                        .onFailure { error ->
                            errorMessage = "Failed to load suggestions"
                            suggestions = emptyList()
                            showSuggestions = false
                            isLoading = false
                        }
                } else {
                    suggestions = emptyList()
                    showSuggestions = false
                    isLoading = false
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

    Box(modifier = modifier) {
        Column {
            OutlinedTextField(
                value = value,
                onValueChange = { newValue ->
                    onValueChange(newValue)
                    searchQuery.value = newValue
                },
                label = { Text(label) },
                placeholder = { Text(placeholder, color = TextTertiary) },
                leadingIcon = {
                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = TextTertiary)
                },
                trailingIcon = {
                    when {
                        isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                strokeWidth = 2.dp,
                                color = Saffron500
                            )
                        }
                        value.isNotEmpty() -> {
                            IconButton(onClick = {
                                onValueChange("")
                                searchQuery.value = ""
                                suggestions = emptyList()
                                showSuggestions = false
                            }) {
                                Icon(Icons.Default.Close, contentDescription = "Clear", tint = TextTertiary)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                shape = MaterialTheme.shapes.small,
                singleLine = true,
                isError = errorMessage != null
            )

            // Error message
            errorMessage?.let { error ->
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(start = 16.dp, top = 4.dp)
                )
            }
        }

        // Suggestions dropdown
        if (showSuggestions && suggestions.isNotEmpty()) {
            Popup(
                alignment = Alignment.TopStart,
                offset = androidx.compose.ui.unit.IntOffset(0, 8),
                properties = PopupProperties(
                    focusable = false,
                    dismissOnBackPress = true,
                    dismissOnClickOutside = true
                ),
                onDismissRequest = { showSuggestions = false }
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = MaterialTheme.shapes.small
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                    ) {
                        items(suggestions) { suggestion ->
                            LocationSuggestionItem(
                                suggestion = suggestion,
                                onClick = {
                                    onLocationSelected(suggestion)
                                    onValueChange(suggestion.toShortDisplayName())
                                    showSuggestions = false
                                    suggestions = emptyList()
                                }
                            )
                            if (suggestion != suggestions.last()) {
                                HorizontalDivider(color = Cosmic800.copy(alpha = 0.3f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LocationSuggestionItem(
    suggestion: LocationSuggestion,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.LocationOn,
            contentDescription = null,
            tint = Saffron500,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = suggestion.toShortDisplayName(),
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (suggestion.toShortDisplayName() != suggestion.displayName) {
                Text(
                    text = suggestion.displayName,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
