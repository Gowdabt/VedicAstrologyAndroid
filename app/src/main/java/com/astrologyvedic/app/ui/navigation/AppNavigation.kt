package com.astrologyvedic.app.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.astrologyvedic.app.ui.screens.ayanamsa.AyanamsaScreen
import com.astrologyvedic.app.ui.screens.baby_names.BabyNamesScreen
import com.astrologyvedic.app.ui.screens.birth_rectification.BirthRectificationScreen
import com.astrologyvedic.app.ui.screens.celebrity.CelebrityScreen
import com.astrologyvedic.app.ui.screens.chart_comparison.ChartComparisonScreen
import com.astrologyvedic.app.ui.screens.chat.ChatScreen
import com.astrologyvedic.app.ui.screens.chinese_zodiac.ChineseZodiacScreen
import com.astrologyvedic.app.ui.screens.choghadiya.ChoghadiyaScreen
import com.astrologyvedic.app.ui.screens.daily.DailyScreen
import com.astrologyvedic.app.ui.screens.daily_mantra.DailyMantraScreen
import com.astrologyvedic.app.ui.screens.dasamsa.DasamsaScreen
import com.astrologyvedic.app.ui.screens.calculators.CalculatorsScreen
import com.astrologyvedic.app.ui.screens.services.ServicesScreen
import com.astrologyvedic.app.ui.screens.face_reading.FaceReadingScreen
import com.astrologyvedic.app.ui.screens.festivals.FestivalsScreen
import com.astrologyvedic.app.ui.screens.gemstone.GemstoneScreen
import com.astrologyvedic.app.ui.screens.guna_milan.GunaMilanScreen
import com.astrologyvedic.app.ui.screens.homa_guide.HomaGuideScreen
import com.astrologyvedic.app.ui.screens.home.HomeScreen
import com.astrologyvedic.app.ui.screens.hora.HoraScreen
import com.astrologyvedic.app.ui.screens.horoscope_share.HoroscopeShareScreen
import com.astrologyvedic.app.ui.screens.kaalsarp.KaalsarpScreen
import com.astrologyvedic.app.ui.screens.kp_astrology.KPAstrologyScreen
import com.astrologyvedic.app.ui.screens.kundli.KundliScreen
import com.astrologyvedic.app.ui.screens.lal_kitab.LalKitabScreen
import com.astrologyvedic.app.ui.screens.life_report.LifeReportScreen
import com.astrologyvedic.app.ui.screens.love_compatibility.LoveCompatScreen
import com.astrologyvedic.app.ui.screens.lucky.LuckyScreen
import com.astrologyvedic.app.ui.screens.mantra_counter.MantraCounterScreen
import com.astrologyvedic.app.ui.screens.match.MatchScreen
import com.astrologyvedic.app.ui.screens.meditation.MeditationScreen
import com.astrologyvedic.app.ui.screens.muhurat.MuhuratScreen
import com.astrologyvedic.app.ui.screens.nadi.NadiScreen
import com.astrologyvedic.app.ui.screens.name_match.NameMatchScreen
import com.astrologyvedic.app.ui.screens.navamsa.NavamsaScreen
import com.astrologyvedic.app.ui.screens.numerology.NumerologyScreen
import com.astrologyvedic.app.ui.screens.palm_reading.PalmReadingScreen
import com.astrologyvedic.app.ui.screens.panchang.PanchangScreen
import com.astrologyvedic.app.ui.screens.past_life.PastLifeScreen
import com.astrologyvedic.app.ui.screens.pathfinder.PathfinderScreen
import com.astrologyvedic.app.ui.screens.porutham.PoruthamScreen
import com.astrologyvedic.app.ui.screens.prayers.PrayersScreen
import com.astrologyvedic.app.ui.screens.profile.ProfileScreen
import com.astrologyvedic.app.ui.screens.puja_guide.PujaGuideScreen
import com.astrologyvedic.app.ui.screens.rahu_kaal.RahuKaalScreen
import com.astrologyvedic.app.ui.screens.sade_sati.SadeSatiScreen
import com.astrologyvedic.app.ui.screens.stock_astrology.StockAstrologyScreen
import com.astrologyvedic.app.ui.screens.sunrise_sunset.SunriseSunsetScreen
import com.astrologyvedic.app.ui.screens.tarot.TarotScreen
import com.astrologyvedic.app.ui.screens.temple_finder.TempleFinderScreen
import com.astrologyvedic.app.ui.screens.timeline.TimelineScreen
import com.astrologyvedic.app.ui.screens.transit.TransitScreen
import com.astrologyvedic.app.ui.screens.varga_charts.VargaChartsScreen
import com.astrologyvedic.app.ui.screens.vastu.VastuScreen
import com.astrologyvedic.app.ui.screens.vrat_calendar.VratCalendarScreen
import com.astrologyvedic.app.ui.screens.western.WesternScreen
import com.astrologyvedic.app.ui.screens.yoga_detection.YogaDetectionScreen
import com.astrologyvedic.app.ui.screens.settings.SettingsScreen
import com.astrologyvedic.app.ui.screens.about.AboutScreen
import com.astrologyvedic.app.ui.screens.profile.NotificationsScreen
import com.astrologyvedic.app.ui.screens.profile.HelpSupportScreen
import com.astrologyvedic.app.ui.screens.profile.LanguageScreen
import com.astrologyvedic.app.ui.screens.profile.SavedChartsScreen
import com.astrologyvedic.app.ui.screens.profile.ReportHistoryScreen
import com.astrologyvedic.app.ui.screens.profile.EditProfileScreen

@Composable
fun AppNavigation(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        // Bottom navigation tabs
        composable(
            Routes.Home.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            HomeScreen(navController = navController)
        }
        composable(
            Routes.Services.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            ServicesScreen(navController = navController)
        }
        composable(
            Routes.Chat.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            ChatScreen(navController = navController)
        }
        composable(
            Routes.Calculators.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            CalculatorsScreen(navController = navController)
        }
        composable(
            Routes.Profile.route,
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            ProfileScreen(navController = navController)
        }

        // Core features — slide in from right
        composable(
            Routes.Kundli.route,
            enterTransition = { slideInHorizontally(tween(250)) { it } + fadeIn(tween(250)) },
            exitTransition = { slideOutHorizontally(tween(250)) { it } + fadeOut(tween(250)) },
            popEnterTransition = { slideInHorizontally(tween(250)) { -it } + fadeIn(tween(250)) },
            popExitTransition = { slideOutHorizontally(tween(250)) { it } + fadeOut(tween(250)) }
        ) {
            KundliScreen(navController = navController)
        }
        composable(
            Routes.Daily.route,
            enterTransition = { slideInHorizontally(tween(250)) { it } + fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(200)) },
            popExitTransition = { slideOutHorizontally(tween(250)) { it } + fadeOut(tween(250)) }
        ) {
            DailyScreen(navController = navController)
        }
        composable(
            Routes.Match.route,
            enterTransition = { slideInHorizontally(tween(250)) { it } + fadeIn(tween(250)) },
            exitTransition = { fadeOut(tween(200)) },
            popExitTransition = { slideOutHorizontally(tween(250)) { it } + fadeOut(tween(250)) }
        ) {
            MatchScreen(navController = navController)
        }
        composable(Routes.Panchang.route) {
            PanchangScreen(navController = navController)
        }
        composable(Routes.Numerology.route) {
            NumerologyScreen(navController = navController)
        }
        composable(Routes.PalmReading.route) {
            PalmReadingScreen(navController = navController)
        }
        composable(Routes.Tarot.route) {
            TarotScreen(navController = navController)
        }
        composable(Routes.MantraCounter.route) {
            MantraCounterScreen(navController = navController)
        }
        composable(Routes.RahuKaal.route) {
            RahuKaalScreen(navController = navController)
        }
        composable(Routes.Choghadiya.route) {
            ChoghadiyaScreen(navController = navController)
        }
        composable(Routes.Hora.route) {
            HoraScreen(navController = navController)
        }
        composable(Routes.SadeSati.route) {
            SadeSatiScreen(navController = navController)
        }
        composable(Routes.Kaalsarp.route) {
            KaalsarpScreen(navController = navController)
        }
        composable(Routes.Muhurat.route) {
            MuhuratScreen(navController = navController)
        }
        composable(Routes.Gemstone.route) {
            GemstoneScreen(navController = navController)
        }
        composable(Routes.LoveCompatibility.route) {
            LoveCompatScreen(navController = navController)
        }
        composable(Routes.Festivals.route) {
            FestivalsScreen(navController = navController)
        }
        composable(Routes.BabyNames.route) {
            BabyNamesScreen(navController = navController)
        }
        composable(Routes.Meditation.route) {
            MeditationScreen(navController = navController)
        }
        composable(Routes.Prayers.route) {
            PrayersScreen(navController = navController)
        }
        composable(Routes.PujaGuide.route) {
            PujaGuideScreen(navController = navController)
        }
        composable(Routes.HomaGuide.route) {
            HomaGuideScreen(navController = navController)
        }
        composable(Routes.VratCalendar.route) {
            VratCalendarScreen(navController = navController)
        }
        composable(Routes.TempleFinder.route) {
            TempleFinderScreen(navController = navController)
        }
        composable(Routes.KpAstrology.route) {
            KPAstrologyScreen(navController = navController)
        }
        composable(Routes.LalKitab.route) {
            LalKitabScreen(navController = navController)
        }
        composable(Routes.Western.route) {
            WesternScreen(navController = navController)
        }
        composable(Routes.ChineseZodiac.route) {
            ChineseZodiacScreen(navController = navController)
        }
        composable(Routes.Nadi.route) {
            NadiScreen(navController = navController)
        }
        composable(Routes.Navamsa.route) {
            NavamsaScreen(navController = navController)
        }
        composable(Routes.Dasamsa.route) {
            DasamsaScreen(navController = navController)
        }
        composable(Routes.VargaCharts.route) {
            VargaChartsScreen(navController = navController)
        }
        composable(Routes.PastLife.route) {
            PastLifeScreen(navController = navController)
        }
        composable(Routes.Pathfinder.route) {
            PathfinderScreen(navController = navController)
        }
        composable(Routes.FaceReading.route) {
            FaceReadingScreen(navController = navController)
        }
        composable(Routes.YogaDetection.route) {
            YogaDetectionScreen(navController = navController)
        }
        composable(Routes.Celebrity.route) {
            CelebrityScreen(navController = navController)
        }
        composable(Routes.ChartComparison.route) {
            ChartComparisonScreen(navController = navController)
        }
        composable(Routes.BirthRectification.route) {
            BirthRectificationScreen(navController = navController)
        }
        composable(Routes.StockAstrology.route) {
            StockAstrologyScreen(navController = navController)
        }
        composable(Routes.HoroscopeShare.route) {
            HoroscopeShareScreen(navController = navController)
        }
        composable(Routes.Lucky.route) {
            LuckyScreen(navController = navController)
        }
        composable(Routes.LifeReport.route) {
            LifeReportScreen(navController = navController)
        }
        composable(Routes.Vastu.route) {
            VastuScreen(navController = navController)
        }
        composable(Routes.Timeline.route) {
            TimelineScreen(navController = navController)
        }
        composable(Routes.Transit.route) {
            TransitScreen(navController = navController)
        }
        composable(Routes.GunaMilan.route) {
            GunaMilanScreen(navController = navController)
        }
        composable(Routes.DailyMantra.route) {
            DailyMantraScreen(navController = navController)
        }
        composable(Routes.SunriseSunset.route) {
            SunriseSunsetScreen(navController = navController)
        }
        composable(Routes.Ayanamsa.route) {
            AyanamsaScreen(navController = navController)
        }
        composable(Routes.NameMatch.route) {
            NameMatchScreen(navController = navController)
        }
        composable(Routes.Porutham.route) {
            PoruthamScreen(navController = navController)
        }
        composable(Routes.AiChat.route) {
            ChatScreen(navController = navController)
        }

        // Profile sub-screens
        composable(Routes.Settings.route) {
            SettingsScreen(navController = navController)
        }
        composable(Routes.About.route) {
            AboutScreen(navController = navController)
        }
        composable(Routes.Notifications.route) {
            NotificationsScreen(navController = navController)
        }
        composable(Routes.HelpSupport.route) {
            HelpSupportScreen(navController = navController)
        }
        composable(Routes.Language.route) {
            LanguageScreen(navController = navController)
        }
        composable(Routes.SavedCharts.route) {
            SavedChartsScreen(navController = navController)
        }
        composable(Routes.ReportHistory.route) {
            ReportHistoryScreen(navController = navController)
        }
        composable(Routes.EditProfile.route) {
            EditProfileScreen(navController = navController)
        }
    }
}

