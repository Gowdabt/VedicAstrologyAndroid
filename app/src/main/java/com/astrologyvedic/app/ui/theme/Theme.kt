package com.astrologyvedic.app.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Saffron500,
    onPrimary = Cosmic950,
    primaryContainer = Saffron600,
    onPrimaryContainer = Saffron200,
    secondary = Cosmic400,
    onSecondary = Cosmic950,
    secondaryContainer = Cosmic700,
    onSecondaryContainer = Cosmic200,
    tertiary = Cosmic300,
    onTertiary = Cosmic900,
    tertiaryContainer = Cosmic800,
    onTertiaryContainer = Cosmic200,
    error = Error,
    onError = Cosmic950,
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Cosmic950,
    onBackground = TextPrimary,
    surface = SurfaceDark,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceCard,
    onSurfaceVariant = TextSecondary,
    outline = BorderDark,
    outlineVariant = BorderLight,
    inverseSurface = Cosmic200,
    inverseOnSurface = Cosmic900,
    inversePrimary = Saffron600,
    surfaceTint = Saffron500
)

@Composable
fun VedicAstrologyTheme(
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
