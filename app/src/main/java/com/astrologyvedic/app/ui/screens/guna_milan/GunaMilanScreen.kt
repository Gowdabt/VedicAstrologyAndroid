package com.astrologyvedic.app.ui.screens.guna_milan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.*
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GunaMilanScreen(navController: NavController, viewModel: GunaMilanViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    var expanded1 by remember { mutableStateOf(false) }
    var expanded2 by remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Guna Milan", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Quick 36-Point Score", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                        Spacer(modifier = Modifier.height(12.dp))
                        ExposedDropdownMenuBox(expanded = expanded1, onExpandedChange = { expanded1 = it }) {
                            OutlinedTextField(value = uiState.nakshatra1, onValueChange = {}, readOnly = true, label = { Text("Boy's Nakshatra") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard), shape = MaterialTheme.shapes.small)
                            ExposedDropdownMenu(expanded = expanded1, onDismissRequest = { expanded1 = false }) { viewModel.nakshatras.forEach { n -> DropdownMenuItem(text = { Text(n) }, onClick = { viewModel.updateNakshatra1(n); expanded1 = false }) } }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        ExposedDropdownMenuBox(expanded = expanded2, onExpandedChange = { expanded2 = it }) {
                            OutlinedTextField(value = uiState.nakshatra2, onValueChange = {}, readOnly = true, label = { Text("Girl's Nakshatra") }, trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2) }, modifier = Modifier.fillMaxWidth().menuAnchor(), colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard), shape = MaterialTheme.shapes.small)
                            ExposedDropdownMenu(expanded = expanded2, onDismissRequest = { expanded2 = false }) { viewModel.nakshatras.forEach { n -> DropdownMenuItem(text = { Text(n) }, onClick = { viewModel.updateNakshatra2(n); expanded2 = false }) } }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.nakshatra1.isNotBlank() && uiState.nakshatra2.isNotBlank(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Calculate Score", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Calculating Guna Milan...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                ResultCard(title = "Guna Milan Score") {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("${uiState.totalScore} / ${uiState.maxScore}", style = MaterialTheme.typography.displaySmall, color = Saffron500, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(uiState.verdict, style = MaterialTheme.typography.titleMedium, color = when { uiState.totalScore >= 28 -> Success; uiState.totalScore >= 14 -> Warning; else -> Error })
                    }
                }
            }
        }
    }
}
