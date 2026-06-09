package com.astrologyvedic.app.ui.screens.guna_milan

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Guna Milan") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            if (!uiState.hasResult && !uiState.isLoading) {
                // Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.Info,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "Guna Milan evaluates 8 aspects (Kootas) of compatibility between two people based on their birth Nakshatras. Max score: 36 points.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                // Input Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Select Nakshatras",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Boy's Nakshatra
                        Text(
                            text = "Boy's Nakshatra",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        ExposedDropdownMenuBox(
                            expanded = expanded1,
                            onExpandedChange = { expanded1 = it }
                        ) {
                            OutlinedTextField(
                                value = uiState.nakshatra1,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("Select nakshatra") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded1)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = MaterialTheme.shapes.medium,
                                singleLine = true
                            )
                            ExposedDropdownMenu(
                                expanded = expanded1,
                                onDismissRequest = { expanded1 = false }
                            ) {
                                viewModel.nakshatras.forEach { n ->
                                    DropdownMenuItem(
                                        text = { Text(n) },
                                        onClick = {
                                            viewModel.updateNakshatra1(n)
                                            expanded1 = false
                                        }
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Girl's Nakshatra
                        Text(
                            text = "Girl's Nakshatra",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                        ExposedDropdownMenuBox(
                            expanded = expanded2,
                            onExpandedChange = { expanded2 = it }
                        ) {
                            OutlinedTextField(
                                value = uiState.nakshatra2,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { Text("Select nakshatra") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded2)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor(),
                                shape = MaterialTheme.shapes.medium,
                                singleLine = true
                            )
                            ExposedDropdownMenu(
                                expanded = expanded2,
                                onDismissRequest = { expanded2 = false }
                            ) {
                                viewModel.nakshatras.forEach { n ->
                                    DropdownMenuItem(
                                        text = { Text(n) },
                                        onClick = {
                                            viewModel.updateNakshatra2(n)
                                            expanded2 = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Calculate Button
                Button(
                    onClick = { viewModel.calculate() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    enabled = uiState.nakshatra1.isNotBlank() && uiState.nakshatra2.isNotBlank(),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        "Calculate Score",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Loading State
            if (uiState.isLoading) {
                LoadingState(message = "Calculating Guna Milan...")
            }

            // Error State
            uiState.error?.let {
                ErrorState(message = it, onRetry = { viewModel.calculate() })
            }

            // Results
            if (uiState.hasResult) {
                Spacer(modifier = Modifier.height(8.dp))

                // Score Card with Circular Indicator
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "GUNA MILAN SCORE",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            letterSpacing = 1.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Circular Score Indicator
                        GunaScoreRing(
                            score = uiState.totalScore,
                            maxScore = uiState.maxScore
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Verdict Chip
                        val verdictColor = when {
                            uiState.totalScore >= 28 -> Success
                            uiState.totalScore >= 21 -> MaterialTheme.colorScheme.primary
                            uiState.totalScore >= 14 -> Warning
                            else -> MaterialTheme.colorScheme.error
                        }
                        Surface(
                            color = verdictColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Text(
                                text = uiState.verdict,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                style = MaterialTheme.typography.labelLarge,
                                color = verdictColor,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interpretation Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Interpretation",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = when {
                                uiState.totalScore >= 28 -> "An excellent match with strong compatibility across all aspects. The couple is likely to have a harmonious and fulfilling relationship."
                                uiState.totalScore >= 21 -> "A good match indicating compatibility in most areas. The relationship has strong potential for success."
                                uiState.totalScore >= 14 -> "An average match. While there are areas of compatibility, some adjustments may be needed for harmony."
                                else -> "Below average compatibility. Consider consulting an astrologer for remedial measures before proceeding."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 22.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Score Breakdown Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
                    ),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Score Scale",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        ScaleRow(label = "28-36", description = "Excellent Match", color = Success)
                        ScaleRow(label = "21-27", description = "Good Match", color = MaterialTheme.colorScheme.primary)
                        ScaleRow(label = "14-20", description = "Average", color = Warning)
                        ScaleRow(label = "0-13", description = "Below Average", color = MaterialTheme.colorScheme.error)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
private fun GunaScoreRing(score: Int, maxScore: Int) {
    val progress = score.toFloat() / maxScore.toFloat()
    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000),
        label = "score_progress"
    )

    val progressColor = when {
        score >= 28 -> Success
        score >= 21 -> Color(0xFF4355B9)
        score >= 14 -> Warning
        else -> Color(0xFFBA1A1A)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(140.dp)
    ) {
        Canvas(modifier = Modifier.size(140.dp)) {
            val strokeWidth = 12.dp.toPx()
            val arcSize = Size(size.width - strokeWidth, size.height - strokeWidth)
            val topLeft = Offset(strokeWidth / 2, strokeWidth / 2)

            // Background ring
            drawArc(
                color = progressColor.copy(alpha = 0.15f),
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Progress ring
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "$score",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = "out of $maxScore",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
            )
        }
    }
}

@Composable
private fun ScaleRow(label: String, description: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            color = color.copy(alpha = 0.15f),
            shape = RoundedCornerShape(4.dp),
            modifier = Modifier.width(56.dp)
        ) {
            Text(
                text = label,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = color,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
