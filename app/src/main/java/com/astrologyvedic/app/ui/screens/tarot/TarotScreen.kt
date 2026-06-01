package com.astrologyvedic.app.ui.screens.tarot

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.BorderDark
import com.astrologyvedic.app.ui.theme.Cosmic800
import com.astrologyvedic.app.ui.theme.Cosmic950
import com.astrologyvedic.app.ui.theme.Saffron400
import com.astrologyvedic.app.ui.theme.Saffron500
import com.astrologyvedic.app.ui.theme.SurfaceCard
import com.astrologyvedic.app.ui.theme.SurfaceCardElevated
import com.astrologyvedic.app.ui.theme.SurfaceDark
import com.astrologyvedic.app.ui.theme.TextPrimary
import com.astrologyvedic.app.ui.theme.TextSecondary
import com.astrologyvedic.app.ui.theme.TextTertiary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TarotScreen(
    navController: NavController,
    viewModel: TarotViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        TopAppBar(
            title = { Text("Tarot Reading", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            actions = {
                if (uiState.hasResult) {
                    IconButton(onClick = { viewModel.reset() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "New Reading", tint = Saffron500)
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        when {
            uiState.isLoading -> {
                LoadingState(message = "Drawing your cards...")
            }
            uiState.error != null && !uiState.hasResult -> {
                ErrorState(
                    message = uiState.error!!,
                    onRetry = { viewModel.drawCards() }
                )
            }
            uiState.hasResult -> {
                // Results view
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Card display
                    if (!uiState.isFlipped) {
                        Text(
                            text = "Tap the cards to reveal",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextTertiary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        items(uiState.cards) { card ->
                            TarotCardView(
                                card = card,
                                isFlipped = uiState.isFlipped,
                                onClick = { viewModel.flipCards() }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    if (uiState.isFlipped) {
                        // Overall message
                        ResultCard(title = "Message from the Cards") {
                            Text(
                                text = uiState.overallMessage,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Individual card interpretations
                        uiState.cards.forEach { card ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                                shape = MaterialTheme.shapes.medium
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = card.name,
                                            style = MaterialTheme.typography.titleSmall,
                                            color = Saffron400,
                                            fontWeight = FontWeight.Bold
                                        )
                                        if (card.isReversed) {
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "(Reversed)",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = TextTertiary
                                            )
                                        }
                                    }
                                    if (card.position.isNotBlank()) {
                                        Text(
                                            text = "Position: ${card.position}",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = TextTertiary
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = card.interpretation,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = TextSecondary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            else -> {
                // Selection form
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Choose Your Spread",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Spread type selector
                    SpreadType.entries.forEach { spread ->
                        FilterChip(
                            selected = uiState.selectedSpread == spread,
                            onClick = { viewModel.selectSpread(spread) },
                            label = {
                                Text(
                                    text = spread.displayName,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = SurfaceCard,
                                selectedContainerColor = Saffron500.copy(alpha = 0.15f),
                                labelColor = TextSecondary,
                                selectedLabelColor = Saffron400
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = BorderDark,
                                selectedBorderColor = Saffron500,
                                enabled = true,
                                selected = uiState.selectedSpread == spread
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Question input
                    Text(
                        text = "Your Question (optional)",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = uiState.question,
                        onValueChange = { viewModel.updateQuestion(it) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text("What guidance do you seek?", color = TextTertiary)
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

                    Spacer(modifier = Modifier.height(24.dp))

                    FilledTonalButton(
                        onClick = { viewModel.drawCards() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = Saffron500,
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "Draw Cards",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun TarotCardView(
    card: TarotCard,
    isFlipped: Boolean,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        targetValue = if (isFlipped) 180f else 0f,
        animationSpec = tween(durationMillis = 600),
        label = "card_flip"
    )

    Box(
        modifier = Modifier
            .width(120.dp)
            .aspectRatio(0.6f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (rotation <= 90f) {
            // Card back
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Cosmic800,
                        RoundedCornerShape(12.dp)
                    )
                    .padding(2.dp)
                    .background(
                        Color(0xFF1A1040),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "?",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Saffron400,
                    fontWeight = FontWeight.Bold
                )
            }
        } else {
            // Card front (appears mirrored due to rotationY, so we counter-rotate)
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f }
                    .background(
                        Color(card.colorHex),
                        RoundedCornerShape(12.dp)
                    )
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = card.name,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    if (card.position.isNotBlank()) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = card.position,
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.7f),
                            textAlign = TextAlign.Center
                        )
                    }
                    if (card.isReversed) {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Reversed",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.5f)
                        )
                    }
                }
            }
        }
    }
}
