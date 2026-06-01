package com.astrologyvedic.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.astrologyvedic.app.ui.MainScreen
import com.astrologyvedic.app.ui.theme.VedicAstrologyTheme
import com.astrologyvedic.app.util.LocationHelper
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var locationHelper: LocationHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        // Initialize location permission launcher
        locationHelper.initializePermissionLauncher(this)

        enableEdgeToEdge()
        setContent {
            VedicAstrologyTheme {
                MainScreen()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        locationHelper.cleanup()
    }
}
