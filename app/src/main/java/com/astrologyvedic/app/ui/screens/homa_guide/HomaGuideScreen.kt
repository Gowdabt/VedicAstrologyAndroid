package com.astrologyvedic.app.ui.screens.homa_guide

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.astrologyvedic.app.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomaGuideScreen(
    navController: NavController,
    viewModel: HomaGuideViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize().background(SurfaceDark)) {
        TopAppBar(
            title = { Text("Homa Guide", color = TextPrimary) },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextPrimary)
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Cosmic950)
        )

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp)) {
            uiState.homas.forEachIndexed { index, homa ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceCardElevated),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.toggleHoma(index) }.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(homa.name, style = MaterialTheme.typography.titleMedium, color = TextPrimary)
                                Text(homa.purpose, style = MaterialTheme.typography.bodySmall, color = Saffron400)
                            }
                            Icon(
                                if (homa.isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null, tint = Saffron500
                            )
                        }

                        AnimatedVisibility(visible = homa.isExpanded) {
                            Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                                Text("Materials", style = MaterialTheme.typography.titleSmall, color = Saffron400, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                homa.materials.forEach { Text("- $it", style = MaterialTheme.typography.bodySmall, color = TextSecondary) }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Steps", style = MaterialTheme.typography.titleSmall, color = Saffron400, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                homa.steps.forEachIndexed { i, step -> Text("${i + 1}. $step", style = MaterialTheme.typography.bodySmall, color = TextSecondary, modifier = Modifier.padding(vertical = 2.dp)) }

                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Key Mantras", style = MaterialTheme.typography.titleSmall, color = Saffron400, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.height(4.dp))
                                homa.mantras.forEach { Text(it, style = MaterialTheme.typography.bodySmall, color = TextSecondary) }
                            }
                        }
                    }
                }
            }
        }
    }
}
