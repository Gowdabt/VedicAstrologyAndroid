package com.astrologyvedic.app.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Language
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.astrologyvedic.app.ui.navigation.AppNavigation
import com.astrologyvedic.app.ui.navigation.BottomNavBar
import com.astrologyvedic.app.ui.navigation.Routes
import com.astrologyvedic.app.ui.navigation.bottomNavItems
import com.astrologyvedic.app.ui.theme.Cosmic950
import com.astrologyvedic.app.ui.theme.Saffron500
import com.astrologyvedic.app.ui.theme.TextPrimary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val bottomNavRoutes = bottomNavItems.map { it.route }
    val showBottomBar = currentRoute in bottomNavRoutes

    val topBarTitle = when (currentRoute) {
        Routes.Home.route -> "Vedic Astrology"
        Routes.Services.route -> "Services"
        Routes.Chat.route -> "AI Chat"
        Routes.Calculators.route -> "Calculators"
        Routes.Profile.route -> "Profile"
        else -> "Vedic Astrology"
    }

    Scaffold(
        topBar = {
            if (showBottomBar) {
                TopAppBar(
                    title = {
                        Text(
                            text = topBarTitle,
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    actions = {
                        IconButton(onClick = {
                            navController.navigate(Routes.Language.route)
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Language,
                                contentDescription = "Language",
                                tint = Saffron500
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Cosmic950,
                        titleContentColor = TextPrimary
                    )
                )
            }
        },
        bottomBar = {
            if (showBottomBar) {
                BottomNavBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        },
        containerColor = Cosmic950
    ) { innerPadding ->
        AppNavigation(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
