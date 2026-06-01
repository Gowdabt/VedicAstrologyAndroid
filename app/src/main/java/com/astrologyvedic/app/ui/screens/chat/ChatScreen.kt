package com.astrologyvedic.app.ui.screens.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.DeleteSweep
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val listState = rememberLazyListState()

    // Auto-scroll to bottom when new messages arrive
    LaunchedEffect(uiState.messages.size) {
        if (uiState.messages.isNotEmpty()) {
            listState.animateScrollToItem(uiState.messages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        TopAppBar(
            title = { Text("Ask the Stars", color = TextPrimary) },
            actions = {
                if (uiState.messages.isNotEmpty()) {
                    IconButton(onClick = { viewModel.clearHistory() }) {
                        Icon(Icons.Default.DeleteSweep, contentDescription = "Clear", tint = TextTertiary)
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        // Messages list
        Box(modifier = Modifier.weight(1f)) {
            if (uiState.messages.isEmpty() && !uiState.isTyping) {
                // Empty state
                EmptyStateContent(
                    onSuggestionClick = { viewModel.sendMessage(it) }
                )
            } else {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(uiState.messages) { message ->
                        ChatBubble(message = message)
                    }
                    if (uiState.isTyping) {
                        item {
                            TypingIndicator()
                        }
                    }
                }
            }
        }

        // Quick suggestion chips
        if (uiState.messages.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val suggestions = listOf(
                    "Marriage timing?",
                    "Career path?",
                    "Health concerns?",
                    "Lucky period?"
                )
                items(suggestions) { suggestion ->
                    FilterChip(
                        selected = false,
                        onClick = { viewModel.sendMessage(suggestion) },
                        label = {
                            Text(suggestion, style = MaterialTheme.typography.bodySmall)
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
        }

        // Input area
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Cosmic950)
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = uiState.inputText,
                onValueChange = { viewModel.updateInput(it) },
                modifier = Modifier.weight(1f),
                placeholder = {
                    Text(
                        text = "Ask anything about your stars...",
                        color = TextTertiary
                    )
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
                shape = RoundedCornerShape(24.dp),
                singleLine = false,
                maxLines = 4
            )
            IconButton(
                onClick = { viewModel.sendMessage() },
                enabled = uiState.inputText.isNotBlank() && !uiState.isTyping,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        if (uiState.inputText.isNotBlank()) Saffron500
                        else SurfaceCard
                    )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = if (uiState.inputText.isNotBlank()) Color.White else TextTertiary
                )
            }
        }
    }
}

@Composable
private fun EmptyStateContent(onSuggestionClick: (String) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = "Ask the Stars",
            style = MaterialTheme.typography.headlineMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Get personalized astrology insights based on your birth chart",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(32.dp))

        val suggestions = listOf(
            "Marriage timing?",
            "Career path?",
            "Health concerns?",
            "Lucky period?"
        )
        suggestions.forEach { suggestion ->
            Card(
                onClick = { onSuggestionClick(suggestion) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceCard),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = suggestion,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(message: ChatUiMessage) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Card(
            modifier = Modifier.widthIn(max = screenWidth * 0.8f),
            colors = CardDefaults.cardColors(
                containerColor = if (message.isUser) Saffron500.copy(alpha = 0.15f)
                else SurfaceCardElevated
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (message.isUser) 16.dp else 4.dp,
                bottomEnd = if (message.isUser) 4.dp else 16.dp
            )
        ) {
            Text(
                text = message.message,
                style = MaterialTheme.typography.bodyMedium,
                color = TextPrimary,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
private fun TypingIndicator() {
    val infiniteTransition = rememberInfiniteTransition(label = "typing")
    val dot1Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot1"
    )
    val dot2Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot2"
    )
    val dot3Alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "dot3"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Card(
            colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
            shape = RoundedCornerShape(16.dp, 16.dp, 16.dp, 4.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .alpha(dot1Alpha)
                        .clip(CircleShape)
                        .background(Saffron400)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .alpha(dot2Alpha)
                        .clip(CircleShape)
                        .background(Saffron400)
                )
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .alpha(dot3Alpha)
                        .clip(CircleShape)
                        .background(Saffron400)
                )
            }
        }
    }
}
