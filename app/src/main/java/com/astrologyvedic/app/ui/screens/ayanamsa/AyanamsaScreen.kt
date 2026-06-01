package com.astrologyvedic.app.ui.screens.ayanamsa

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
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
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AyanamsaScreen(
    navController: NavController,
    viewModel: AyanamsaViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Ayanamsa", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Card(colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated), shape = MaterialTheme.shapes.medium) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = uiState.date,
                            onValueChange = { viewModel.updateDate(it) },
                            label = { Text("Date (DD/MM/YYYY)") },
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
                    onClick = { viewModel.calculate() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.date.isNotBlank(),
                    colors = ButtonDefaults.filledTonalButtonColors(containerColor = Saffron500, contentColor = Color.White),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Calculate", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            if (uiState.isLoading) { LoadingState(message = "Calculating Ayanamsa...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.calculate() }) }

            if (uiState.hasResult) {
                ResultCard(title = "Lahiri Ayanamsa") {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(uiState.lahiriValue, style = MaterialTheme.typography.displaySmall, color = Saffron500, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(uiState.explanation, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                    }
                }
            }
        }
    }
}
