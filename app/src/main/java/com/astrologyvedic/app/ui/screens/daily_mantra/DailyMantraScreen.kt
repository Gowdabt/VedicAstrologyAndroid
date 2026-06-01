package com.astrologyvedic.app.ui.screens.daily_mantra

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.components.ErrorState
import com.astrologyvedic.app.ui.components.LoadingState
import com.astrologyvedic.app.ui.components.ResultCard
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyMantraScreen(
    navController: NavController,
    viewModel: DailyMantraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val clipboardManager = LocalClipboardManager.current

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Daily Mantra", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (uiState.isLoading) { LoadingState(message = "Loading today's mantra...") }
            uiState.error?.let { ErrorState(message = it, onRetry = { viewModel.loadMantra() }) }

            if (uiState.hasResult) {
                Spacer(modifier = Modifier.height(32.dp))

                Text("Today's Mantra", style = MaterialTheme.typography.titleMedium, color = TextSecondary)
                Spacer(modifier = Modifier.height(16.dp))

                ResultCard(title = uiState.deity) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = uiState.mantra,
                            style = MaterialTheme.typography.headlineSmall,
                            color = Saffron500,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = uiState.meaning,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        FilledTonalButton(
                            onClick = { clipboardManager.setText(AnnotatedString(uiState.mantra)) },
                            colors = ButtonDefaults.filledTonalButtonColors(containerColor = SurfaceCard, contentColor = Saffron500)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Copy Mantra")
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                ResultCard(title = "Why This Mantra?") {
                    Text(uiState.reason, style = MaterialTheme.typography.bodyMedium, color = TextSecondary)
                }
            }
        }
    }
}
