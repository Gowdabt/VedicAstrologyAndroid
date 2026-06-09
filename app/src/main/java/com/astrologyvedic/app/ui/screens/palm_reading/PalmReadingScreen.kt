package com.astrologyvedic.app.ui.screens.palm_reading

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PalmReadingScreen(
    navController: NavController,
    viewModel: PalmReadingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        TopAppBar(
            title = { Text("Palm Reading", color = MaterialTheme.colorScheme.onSurface) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onSurface)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
        )

        when {
            uiState.isLoading -> {
                LoadingState(message = "Reading your palm lines...")
            }
            uiState.error != null && !uiState.hasResult -> {
                ErrorState(
                    message = uiState.error!!,
                    onRetry = { viewModel.generateReading() }
                )
            }
            uiState.hasResult -> {
                // Results view
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    // Overall reading
                    ResultCard(title = "Overall Reading", showShareButton = true) {
                        Text(
                            text = uiState.overallReading,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Individual lines
                    uiState.palmLines.forEach { line ->
                        PalmLineCard(line = line)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
            else -> {
                // Initial state - camera/upload prompt
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Palm outline guide
                    PalmOutlineGuide()

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Position your palm within the guide",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "We'll analyze your palm lines using your birth chart data for accurate readings",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Camera button
                    FilledTonalButton(
                        onClick = { viewModel.onImageCaptured() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Take Photo",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Gallery button
                    OutlinedButton(
                        onClick = { viewModel.onGallerySelected() },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            brush = androidx.compose.ui.graphics.SolidColor(MaterialTheme.colorScheme.primary)
                        ),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Choose from Gallery",
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

@Composable
private fun PalmOutlineGuide() {
    val primaryColor = MaterialTheme.colorScheme.primary
    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .aspectRatio(0.75f)
            .border(2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(16.dp)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            val w = size.width
            val h = size.height
            val strokeStyle = Stroke(width = 2f, cap = StrokeCap.Round)

            // Simplified palm outline
            val palmPath = Path().apply {
                moveTo(w * 0.3f, h)
                cubicTo(w * 0.1f, h * 0.7f, w * 0.1f, h * 0.4f, w * 0.2f, h * 0.25f)
                cubicTo(w * 0.25f, h * 0.15f, w * 0.3f, h * 0.05f, w * 0.35f, h * 0.1f)
                lineTo(w * 0.4f, h * 0.2f)
                moveTo(w * 0.4f, h * 0.2f)
                cubicTo(w * 0.4f, h * 0.1f, w * 0.45f, h * 0.0f, w * 0.5f, h * 0.05f)
                lineTo(w * 0.5f, h * 0.2f)
                moveTo(w * 0.5f, h * 0.2f)
                cubicTo(w * 0.55f, h * 0.05f, w * 0.6f, h * 0.0f, w * 0.62f, h * 0.08f)
                lineTo(w * 0.6f, h * 0.22f)
                moveTo(w * 0.6f, h * 0.22f)
                cubicTo(w * 0.65f, h * 0.12f, w * 0.72f, h * 0.1f, w * 0.72f, h * 0.2f)
                lineTo(w * 0.7f, h * 0.3f)
                cubicTo(w * 0.9f, h * 0.4f, w * 0.9f, h * 0.7f, w * 0.7f, h)
            }
            drawPath(palmPath, primaryColor.copy(alpha = 0.5f), style = strokeStyle)

            // Heart line
            val heartLine = Path().apply {
                moveTo(w * 0.2f, h * 0.35f)
                cubicTo(w * 0.35f, h * 0.3f, w * 0.55f, h * 0.28f, w * 0.75f, h * 0.32f)
            }
            drawPath(heartLine, primaryColor.copy(alpha = 0.7f), style = Stroke(width = 1.5f))

            // Head line
            val headLine = Path().apply {
                moveTo(w * 0.2f, h * 0.42f)
                cubicTo(w * 0.35f, h * 0.44f, w * 0.5f, h * 0.46f, w * 0.65f, h * 0.48f)
            }
            drawPath(headLine, primaryColor.copy(alpha = 0.7f), style = Stroke(width = 1.5f))

            // Life line
            val lifeLine = Path().apply {
                moveTo(w * 0.25f, h * 0.3f)
                cubicTo(w * 0.2f, h * 0.5f, w * 0.22f, h * 0.7f, w * 0.35f, h * 0.85f)
            }
            drawPath(lifeLine, primaryColor.copy(alpha = 0.7f), style = Stroke(width = 1.5f))
        }

        Text(
            text = "Place palm here",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.outline,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        )
    }
}

@Composable
private fun PalmLineCard(line: PalmLine) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = line.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = line.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = line.interpretation,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
