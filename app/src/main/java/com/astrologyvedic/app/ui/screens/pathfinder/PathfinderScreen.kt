package com.astrologyvedic.app.ui.screens.pathfinder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.BorderDark
import com.astrologyvedic.app.ui.theme.Cosmic950
import com.astrologyvedic.app.ui.theme.Saffron400
import com.astrologyvedic.app.ui.theme.Saffron500
import com.astrologyvedic.app.ui.theme.SurfaceCard
import com.astrologyvedic.app.ui.theme.SurfaceCardElevated
import com.astrologyvedic.app.ui.theme.SurfaceDark
import com.astrologyvedic.app.ui.theme.TextPrimary
import com.astrologyvedic.app.ui.theme.TextSecondary
import com.astrologyvedic.app.ui.theme.TextTertiary

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun PathfinderScreen(
    navController: NavController,
    viewModel: PathfinderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        TopAppBar(
            title = { Text("Pathfinder", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            actions = {
                if (uiState.hasResult) {
                    IconButton(onClick = { viewModel.reset() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "New Question", tint = Saffron500)
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        when {
            uiState.isLoading -> {
                LoadingState(message = "Consulting the stars...")
            }
            uiState.error != null && !uiState.hasResult -> {
                ErrorState(
                    message = uiState.error!!,
                    onRetry = { viewModel.submitCustomQuestion() }
                )
            }
            uiState.hasResult -> {
                // Answer display
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Question
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Saffron500.copy(alpha = 0.1f)),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Your Question",
                                style = MaterialTheme.typography.labelMedium,
                                color = Saffron400
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uiState.selectedQuestion,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Answer
                    ResultCard(title = "Astrological Guidance", showShareButton = true) {
                        Text(
                            text = uiState.answer,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            else -> {
                // Question selection
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = "What do you want to know?",
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Select a question or ask your own",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Predefined question chips
                    Text(
                        text = "Popular Questions",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextTertiary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        viewModel.predefinedQuestions.forEach { question ->
                            FilterChip(
                                selected = false,
                                onClick = { viewModel.selectQuestion(question) },
                                label = {
                                    Text(
                                        text = question,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = SurfaceCard,
                                    labelColor = TextSecondary
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    borderColor = BorderDark,
                                    enabled = true,
                                    selected = false
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Custom question
                    Text(
                        text = "Or ask your own question",
                        style = MaterialTheme.typography.titleSmall,
                        color = TextTertiary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.customQuestion,
                        onValueChange = { viewModel.updateCustomQuestion(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("Type your question here...", color = TextTertiary)
                        },
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
                        minLines = 3,
                        maxLines = 5
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    FilledTonalButton(
                        onClick = { viewModel.submitCustomQuestion() },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = uiState.customQuestion.isNotBlank(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Saffron500,
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Ask",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }
}
