package com.astrologyvedic.app.ui.screens.birth_rectification

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
fun BirthRectificationScreen(navController: NavController, viewModel: BirthRectificationViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val tfColors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Saffron500, unfocusedBorderColor = BorderDark, focusedTextColor = TextPrimary, unfocusedTextColor = TextPrimary, focusedContainerColor = SurfaceCard, unfocusedContainerColor = SurfaceCard)
    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(title = { Text("Birth Time Rectification", color = TextPrimary) }, navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary) } }, colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950))
        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Basic Info", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = uiState.dob, onValueChange = { viewModel.updateField("dob", it) }, label = { Text("Date of Birth (DD/MM/YYYY)") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = MaterialTheme.shapes.small, singleLine = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = uiState.approximateTime, onValueChange = { viewModel.updateField("time", it) }, label = { Text("Approximate Birth Time (HH:MM)") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = MaterialTheme.shapes.small, singleLine = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = uiState.place, onValueChange = { viewModel.updateField("place", it) }, label = { Text("Birth Place") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = MaterialTheme.shapes.small, singleLine = true)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Life Events (for rectification)", style = MaterialTheme.typography.titleSmall, color = TextPrimary)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = uiState.marriageDate, onValueChange = { viewModel.updateField("marriage", it) }, label = { Text("Marriage Date (optional)") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = MaterialTheme.shapes.small, singleLine = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = uiState.firstJobDate, onValueChange = { viewModel.updateField("job", it) }, label = { Text("First Job Date (optional)") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = MaterialTheme.shapes.small, singleLine = true)
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = uiState.firstChildDate, onValueChange = { viewModel.updateField("child", it) }, label = { Text("First Child Born (optional)") }, modifier = Modifier.fillMaxWidth(), colors = tfColors, shape = MaterialTheme.shapes.small, singleLine = true)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                FilledTonalButton(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth(), enabled = uiState.dob.isNotBlank(), colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White), shape = MaterialTheme.shapes.medium) { Text("Rectify Birth Time", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp)) }
            }
            if (uiState.isLoading) { LoadingState(message = "Rectifying birth time...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }
            if (uiState.hasResult) {
                ResultCard(title = "Rectified Birth Time") { Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) { Text(uiState.rectifiedTime, style = MaterialTheme.typography.displaySmall, color = Saffron500, fontWeight = FontWeight.Bold); if (uiState.confidence.isNotBlank()) { Spacer(modifier = Modifier.height(4.dp)); Text("Confidence: ${uiState.confidence}", style = MaterialTheme.typography.bodyMedium, color = Success) } } }
                if (uiState.explanation.isNotBlank()) { Spacer(modifier = Modifier.height(12.dp)); ResultCard(title = "Explanation") { Text(uiState.explanation, style = MaterialTheme.typography.bodyMedium, color = TextSecondary) } }
            }
        }
    }
}
