package com.astrologyvedic.app.ui.screens.vastu

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VastuScreen(navController: NavController, viewModel: VastuViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var expandedType by remember { mutableStateOf(false) }
    var expandedDir by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Vastu Analysis", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ExposedDropdownMenuBox(expanded = expandedType, onExpandedChange = { expandedType = it }) {
                            OutlinedTextField(value = uiState.propertyType, onValueChange = {}, readOnly = true, label = { Text("Property Type") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedType) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard), shape = MaterialTheme.shapes.small)
                            ExposedDropdownMenu(expanded = expandedType, onDismissRequest = { expandedType = false }) { viewModel.propertyTypes.forEach { t -> DropdownMenuItem(text = { Text(t) }, onClick = { viewModel.updatePropertyType(t); expandedType = false }) } }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(expanded = expandedDir, onExpandedChange = { expandedDir = it }) {
                            OutlinedTextField(value = uiState.facingDirection, onValueChange = {}, readOnly = true, label = { Text("Facing Direction") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDir) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard), shape = MaterialTheme.shapes.small)
                            ExposedDropdownMenu(expanded = expandedDir, onDismissRequest = { expandedDir = false }) { viewModel.directions.forEach { d -> DropdownMenuItem(text = { Text(d) }, onClick = { viewModel.updateDirection(d); expandedDir = false }) } }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.propertyType.isNotBlank() && uiState.facingDirection.isNotBlank(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Analyze", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Analyzing Vastu...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                if (uiState.analysis.isNotBlank()) { ResultCard(title = "Analysis") { Text(uiState.analysis, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) }; Spacer(modifier = Modifier.height(12.dp)) }
                if (uiState.tips.isNotEmpty()) { ResultCard(title = "Vastu Tips") { Column { uiState.tips.forEachIndexed { i, t -> Text("${i + 1}. $t", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.padding(vertical = 2.dp)) } } } }
            }
        }
    }
}
