package com.astrologyvedic.app.ui.screens.numerology

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NumerologyScreen(
    navController: NavController,
    viewModel: NumerologyViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        TopAppBar(
            title = { Text("Numerology", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            if (!uiState.hasResult && !uiState.isLoading) {
                Text(
                    text = "Discover your numerology numbers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        OutlinedTextField(
                            value = uiState.name,
                            onValueChange = { viewModel.updateName(it) },
                            label = { Text("Full Name") },
                            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = TextTertiary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Saffron500,
                                unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = Saffron500,
                                focusedContainerColor = SurfaceCard,
                                unfocusedContainerColor = SurfaceCard
                            ),
                            shape = MaterialTheme.shapes.small,
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = uiState.dob,
                            onValueChange = { viewModel.updateDob(it) },
                            label = { Text("Date of Birth (DD/MM/YYYY)") },
                            leadingIcon = { Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = TextTertiary) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Saffron500,
                                unfocusedBorderColor = BorderDark,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary,
                                cursorColor = Saffron500,
                                focusedContainerColor = SurfaceCard,
                                unfocusedContainerColor = SurfaceCard
                            ),
                            shape = MaterialTheme.shapes.small,
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                FilledTonalButton(
                    onClick = { viewModel.calculate() },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = uiState.name.isNotBlank() && uiState.dob.isNotBlank(),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = Saffron500,
                        contentColor = Color.White
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("Calculate", style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(vertical = 4.dp))
                }
            }

            if (uiState.isLoading) {
                LoadingState(message = "Calculating your numbers...")
            }

            uiState.error?.let { error ->
                ErrorState(message = error, onRetry = { viewModel.calculate() })
            }

            if (uiState.hasResult) {
                uiState.numbers.forEach { number ->
                    ResultCard(title = number.type) {
                        Column {
                            Text(
                                text = "${number.value}",
                                style = MaterialTheme.typography.displaySmall,
                                color = Saffron500,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = number.meaning,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}
