package com.astrologyvedic.app.ui.screens.face_reading

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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FaceReadingScreen(navController: NavController, viewModel: FaceReadingViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Face Reading", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = it }) {
                            OutlinedTextField(value = uiState.faceShape, onValueChange = {}, readOnly = true, label = { Text("Face Shape") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard), shape = MaterialTheme.shapes.small)
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) { viewModel.faceShapes.forEach { s -> DropdownMenuItem(text = { Text(s) }, onClick = { viewModel.updateFaceShape(s); expanded = false }) } }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = uiState.dob, onValueChange = { viewModel.updateDob(it) }, label = { Text("Date of Birth (DD/MM/YYYY)") }, leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextTertiary) }, modifier = Modifier.fillMaxWidth(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard), shape = MaterialTheme.shapes.small, singleLine = true)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.faceShape.isNotBlank(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Get Reading", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Analyzing face shape...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                if (uiState.reading.isNotBlank()) { ResultCard(title = "Personality Reading") { Text(uiState.reading, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) }; Spacer(modifier = Modifier.height(12.dp)) }
                if (uiState.traits.isNotEmpty()) { ResultCard(title = "Key Traits") { Column { uiState.traits.forEach { Text("- $it", style = MaterialTheme.typography.bodyMedium, color = TextSecondary, modifier = Modifier.padding(vertical = 2.dp)) } } } }
            }
        }
    }
}
