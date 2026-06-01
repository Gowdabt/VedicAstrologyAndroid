package com.astrologyvedic.app.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.astrologyvedic.app.R
import com.astrologyvedic.app.ui.theme.Cosmic950
import com.astrologyvedic.app.ui.theme.Saffron500
import com.astrologyvedic.app.ui.theme.SurfaceDark
import com.astrologyvedic.app.ui.theme.TextSecondary

data class BottomNavItem(
    val route: String,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val bottomNavItems = listOf(
    BottomNavItem(
        route = Routes.Home.route,
        label = "Home",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home
    ),
    BottomNavItem(
        route = Routes.Services.route,
        label = "Services",
        selectedIcon = Icons.Filled.Star,
        unselectedIcon = Icons.Outlined.StarOutline
    ),
    BottomNavItem(
        route = Routes.Chat.route,
        label = "Chat",
        selectedIcon = Icons.Filled.Star, // placeholder, will use custom
        unselectedIcon = Icons.Outlined.StarOutline
    ),
    BottomNavItem(
        route = Routes.Calculators.route,
        label = "Calculators",
        selectedIcon = Icons.Filled.Calculate,
        unselectedIcon = Icons.Outlined.Calculate
    ),
    BottomNavItem(
        route = Routes.Profile.route,
        label = "Profile",
        selectedIcon = Icons.Filled.Person,
        unselectedIcon = Icons.Outlined.Person
    )
)

@Composable
fun BottomNavBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = SurfaceDark,
        contentColor = TextSecondary
    ) {
        bottomNavItems.forEach { item ->
            val isSelected = currentRoute == item.route
            NavigationBarItem(
                selected = isSelected,
                onClick = { onNavigate(item.route) },
                icon = {
                    when (item.label) {
                        "Chat" -> {
                            Icon(
                                painter = if (isSelected) painterResource(R.drawable.ic_chat_filled)
                                else painterResource(R.drawable.ic_chat_outlined),
                                contentDescription = item.label
                            )
                        }
                        else -> {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        }
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Cosmic950,
                    selectedTextColor = Saffron500,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary,
                    indicatorColor = Saffron500
                )
            )
        }
    }
}
