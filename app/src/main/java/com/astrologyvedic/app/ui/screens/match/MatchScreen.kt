package com.astrologyvedic.app.ui.screens.match

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.PersonForm
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.components.isValid
import com.astrologyvedic.app.ui.theme.Cosmic800
import com.astrologyvedic.app.ui.theme.Cosmic950
import com.astrologyvedic.app.ui.theme.Saffron400
import com.astrologyvedic.app.ui.theme.Saffron500
import com.astrologyvedic.app.ui.theme.Success
import com.astrologyvedic.app.ui.theme.SurfaceCard
import com.astrologyvedic.app.ui.theme.SurfaceCardElevated
import com.astrologyvedic.app.ui.theme.SurfaceDark
import com.astrologyvedic.app.ui.theme.TextPrimary
import com.astrologyvedic.app.ui.theme.TextSecondary
import com.astrologyvedic.app.ui.theme.TextTertiary
import com.astrologyvedic.app.ui.theme.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchScreen(
    navController: NavController,
    viewModel: MatchViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        TopAppBar(
            title = { Text("Match Making", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            actions = {
                if (uiState.hasResult) {
                    IconButton(onClick = { /* TODO: Share */ }) {
                        Icon(Icons.Default.Share, contentDescription = "Share", tint = Saffron500)
                    }
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        when {
            uiState.isLoading -> {
                LoadingState(message = "Analyzing compatibility...")
            }
            uiState.error != null && !uiState.hasResult -> {
                ErrorState(
                    message = uiState.error!!,
                    onRetry = { viewModel.checkCompatibility() }
                )
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    if (!uiState.hasResult) {
                        // Person 1 form
                        PersonForm(
                            title = "Person 1 (Boy)",
                            state = uiState.person1Form,
                            onStateChange = { viewModel.updatePerson1Form(it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Person 2 form
                        PersonForm(
                            title = "Person 2 (Girl)",
                            state = uiState.person2Form,
                            onStateChange = { viewModel.updatePerson2Form(it) }
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        FilledTonalButton(
                            onClick = { viewModel.checkCompatibility() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = uiState.person1Form.isValid() && uiState.person2Form.isValid(),
                            colors = ButtonDefaults.filledTonalButtonColors(
                                containerColor = Saffron500,
                                contentColor = Color.White
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = "Check Compatibility",
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    } else {
                        // Results
                        GunaScoreCard(
                            totalScore = uiState.totalScore,
                            maxScore = uiState.maxScore,
                            verdict = uiState.verdict
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Koota scores
                        ResultCard(title = "Koota Scores") {
                            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                                uiState.kootaScores.forEach { koota ->
                                    KootaScoreRow(koota = koota)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun GunaScoreCard(
    totalScore: Int,
    maxScore: Int,
    verdict: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
        shape = MaterialTheme.shapes.medium
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Guna Milan Score",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Circular progress
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(140.dp)
            ) {
                CircularProgressIndicator(
                    progress = { totalScore.toFloat() / maxScore.toFloat() },
                    modifier = Modifier.fillMaxSize(),
                    color = when {
                        totalScore >= 28 -> Success
                        totalScore >= 21 -> Saffron500
                        totalScore >= 14 -> Warning
                        else -> Color(0xFFEF4444)
                    },
                    strokeWidth = 10.dp,
                    trackColor = Cosmic800,
                    strokeCap = StrokeCap.Round
                )
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "$totalScore",
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextPrimary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "out of $maxScore",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = verdict,
                style = MaterialTheme.typography.titleLarge,
                color = when (verdict) {
                    "Highly Compatible" -> Success
                    "Good Match" -> Saffron500
                    "Average" -> Warning
                    else -> Color(0xFFEF4444)
                },
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun KootaScoreRow(koota: KootaScore) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = koota.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextPrimary,
                    fontWeight = FontWeight.Medium
                )
                if (koota.description.isNotBlank()) {
                    Text(
                        text = koota.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextTertiary
                    )
                }
            }
            Text(
                text = "${koota.obtained}/${koota.maximum}",
                style = MaterialTheme.typography.bodyMedium,
                color = Saffron400,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { if (koota.maximum > 0) koota.obtained.toFloat() / koota.maximum.toFloat() else 0f },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp),
            color = Saffron500,
            trackColor = Cosmic800,
            strokeCap = StrokeCap.Round
        )
    }
}
