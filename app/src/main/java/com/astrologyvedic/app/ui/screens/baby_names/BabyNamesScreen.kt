package com.astrologyvedic.app.ui.screens.baby_names

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BabyNamesScreen(navController: NavController, viewModel: BabyNamesViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Baby Names", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                            OutlinedTextField(value = uiState.selectedNakshatra, onValueChange = {}, readOnly = true, label = { Text("Nakshatra") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard), shape = MaterialTheme.shapes.small)
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { viewModel.nakshatras.forEach { n -> DropdownMenuItem(text = { Text(n) }, onClick = { viewModel.selectNakshatra(n); expanded = false }) } }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Gender", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            listOf("Boy", "Girl").forEach { g -> FilterChip(selected = uiState.selectedGender == g, onClick = { viewModel.selectGender(g) }, label = { Text(g) }, colors = FilterChipDefaults.filterChipColors(selectedContainerColor = Saffron500, selectedLabelColor = Color.White, containerColor = SurfaceCard, labelColor = TextSecondary)) }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.search() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.selectedNakshatra.isNotBlank(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Find Names", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Finding names...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.search() }) }
            if (uiState.hasResult) {
                Text("Names for ${uiState.selectedNakshatra} (${uiState.selectedGender})", style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                Spacer(modifier = Modifier.height(12.dp))
                uiState.names.forEach { name ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.small) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(name.name, style = MaterialTheme.typography.bodyLarge, color = Saffron400, fontWeight = FontWeight.Medium)
                            Text(name.meaning, style = MaterialTheme.typography.bodySmall, color = TextSecondary)
                        }
                    }
                }
                if (uiState.names.isEmpty()) { Text("No names found", style = MaterialTheme.typography.bodyMedium, color = TextSecondary) }
            }
        }
    }
}
