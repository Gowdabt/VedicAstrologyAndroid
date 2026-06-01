# Vedic Astrology Android App — Complete Plan

## App Overview
**Name:** Vedic Astrology (वैदिक ज्योतिष)  
**Package:** com.astrologyvedic.app  
**Min SDK:** 26 (Android 8.0)  
**Target SDK:** 36  
**Architecture:** MVVM + Clean Architecture  
**Language:** Kotlin  
**UI:** Jetpack Compose (Material 3)  
**Backend:** Same API as web (https://astrologyvedic.com/api/)

---

## UI Architecture — 5 Bottom Tabs

```
┌─────────────────────────────────────────────┐
│  ● Vedic Astrology          🌐 EN  👤      │  ← Top App Bar
├─────────────────────────────────────────────┤
│                                             │
│              CONTENT AREA                   │
│         (Changes per tab/screen)            │
│                                             │
├─────────────────────────────────────────────┤
│  🏠     ⭐     💬     🔧     👤           │  ← Bottom Nav
│ Home  Explore  Chat  Tools  Profile        │
└─────────────────────────────────────────────┘
```

---

## Tab 1: HOME (Dashboard)

```
┌─────────────────────────────────────┐
│ Today's Panchang        📤 Share    │
│ ┌─────────────────────────────────┐ │
│ │ Tithi: Shukla Chaturthi        │ │
│ │ Nakshatra: Rohini              │ │
│ │ Rahu Kaal: 10:30-12:00        │ │
│ │ Yoga: Shubha                   │ │
│ └─────────────────────────────────┘ │
│                                     │
│ Today's Horoscope                   │
│ ┌───┐ ┌───┐ ┌───┐ ┌───┐          │
│ │♈ │ │♉ │ │♊ │ │♋ │  ← Scroll │
│ │Mes│ │Vri│ │Mit│ │Kar│          │
│ └───┘ └───┘ └───┘ └───┘          │
│                                     │
│ Quick Actions              See All→ │
│ ┌──────┐ ┌──────┐ ┌──────┐       │
│ │ ☀️   │ │ ❤️   │ │ 📅   │       │
│ │Kundli│ │Match │ │Daily │       │
│ └──────┘ └──────┘ └──────┘       │
│                                     │
│ ┌──────┐ ┌──────┐ ┌──────┐       │
│ │ 🔮   │ │ ✋   │ │ 🎴   │       │
│ │AI Ask│ │Palm  │ │Tarot │       │
│ └──────┘ └──────┘ └──────┘       │
│                                     │
│ Lucky Today                         │
│ ┌─────────────────────────────────┐ │
│ │ 🎨 Color: Green  🔢 Number: 7 │ │
│ │ 🧭 Direction: East             │ │
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

---

## Tab 2: EXPLORE (All Features Grid)

```
┌─────────────────────────────────────┐
│ 🔍 Search features...              │
│                                     │
│ [All] [AI] [Calc] [Spirit] [Match] │ ← Filter chips
│                                     │
│ ┌────────┐ ┌────────┐ ┌────────┐  │
│ │  ☀️    │ │  📅    │ │  ❤️    │  │
│ │ Kundli │ │ Daily  │ │ Match  │  │
│ └────────┘ └────────┘ └────────┘  │
│ ┌────────┐ ┌────────┐ ┌────────┐  │
│ │  💬    │ │  ✋    │ │  🎴    │  │
│ │AI Chat │ │ Palm   │ │ Tarot  │  │
│ └────────┘ └────────┘ └────────┘  │
│ ┌────────┐ ┌────────┐ ┌────────┐  │
│ │  🔢    │ │  ⚠️    │ │  ⏰    │  │
│ │Numero  │ │RahuKal │ │Choghad │  │
│ └────────┘ └────────┘ └────────┘  │
│ ┌────────┐ ┌────────┐ ┌────────┐  │
│ │  🛡️    │ │  ⚡    │ │  💎    │  │
│ │SadeSati│ │Kaalsarp│ │Gemstone│  │
│ └────────┘ └────────┘ └────────┘  │
│ ...40+ items in scrollable grid    │
└─────────────────────────────────────┘
```

---

## Tab 3: AI CHAT (Always Accessible)

```
┌─────────────────────────────────────┐
│ 🪐 AI Astrologer                   │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ Hello! I'm your Vedic       │   │
│  │ astrologer. Ask me anything  │   │
│  │ about your chart, transits,  │   │
│  │ marriage timing, career...   │   │
│  └─────────────────────────────┘   │
│                                     │
│       ┌─────────────────────┐      │
│       │ When will I get      │      │
│       │ married?             │      │
│       └─────────────────────┘      │
│                                     │
│  ┌─────────────────────────────┐   │
│  │ Based on your chart:        │   │
│  │ 7th lord Venus in 11th...   │   │
│  │ Jupiter transit in 2026...  │   │
│  │ Favorable period: Jun-Nov   │   │
│  └─────────────────────────────┘   │
│                                     │
│ Quick Questions:                    │
│ [Marriage?] [Career?] [Health?]     │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ Type your question...     📎 ➤ │ │
│ └─────────────────────────────────┘ │
└─────────────────────────────────────┘
```

---

## Tab 4: TOOLS (Calculators & Spiritual)

```
┌─────────────────────────────────────┐
│ Tools & Calculators                 │
│                                     │
│ ━━ Calculators ━━━━━━━━━━━━━━━━━━  │
│ ┌────────────────────────────────┐  │
│ │ 🔢 Numerology                → │  │
│ ├────────────────────────────────┤  │
│ │ ⏰ Rahu Kaal                 → │  │
│ ├────────────────────────────────┤  │
│ │ ⏱️ Choghadiya                → │  │
│ ├────────────────────────────────┤  │
│ │ 🕐 Hora                     → │  │
│ ├────────────────────────────────┤  │
│ │ 🛡️ Sade Sati                → │  │
│ ├────────────────────────────────┤  │
│ │ ⚡ Kaal Sarp Dosha           → │  │
│ ├────────────────────────────────┤  │
│ │ 💎 Gemstone                  → │  │
│ └────────────────────────────────┘  │
│                                     │
│ ━━ Spiritual ━━━━━━━━━━━━━━━━━━━━  │
│ ┌────────────────────────────────┐  │
│ │ 🔥 Puja Guide               → │  │
│ ├────────────────────────────────┤  │
│ │ 📿 Jaap Counter             → │  │
│ ├────────────────────────────────┤  │
│ │ 🧘 Meditation               → │  │
│ ├────────────────────────────────┤  │
│ │ 📖 Prayers & Stotras        → │  │
│ ├────────────────────────────────┤  │
│ │ 🔥 Homa Guide               → │  │
│ └────────────────────────────────┘  │
│                                     │
│ ━━ Astrology Systems ━━━━━━━━━━━━  │
│ ┌────────────────────────────────┐  │
│ │ 🎯 KP Astrology             → │  │
│ ├────────────────────────────────┤  │
│ │ 📕 Lal Kitab                → │  │
│ ├────────────────────────────────┤  │
│ │ 🌍 Western Astrology        → │  │
│ └────────────────────────────────┘  │
└─────────────────────────────────────┘
```

---

## Tab 5: PROFILE

```
┌─────────────────────────────────────┐
│ My Profile                          │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │ 👤 Harshith Gowda              │ │
│ │ DOB: 15 Aug 1995, 10:30 AM     │ │
│ │ Place: Bangalore, India         │ │
│ │ Rashi: Vrishabha | Nakshatra:   │ │
│ │ Rohini                    ✏️    │ │
│ └─────────────────────────────────┘ │
│                                     │
│ Family Members           + Add      │
│ ┌──────────────────────────────┐   │
│ │ 👩 Mom  │ 👨 Dad  │ 👧 Sis │   │
│ └──────────────────────────────┘   │
│                                     │
│ Report History                      │
│ ┌────────────────────────────────┐  │
│ │ 📄 Kundli - 28 May 2026     → │  │
│ │ 📄 Match - 25 May 2026      → │  │
│ │ 📄 Transit - 22 May 2026    → │  │
│ └────────────────────────────────┘  │
│                                     │
│ Settings                            │
│ ┌────────────────────────────────┐  │
│ │ 🌐 Language      English     → │  │
│ │ 🎨 Theme         Dark        → │  │
│ │ 📐 Chart Style   North Indian→ │  │
│ │ 🔔 Notifications On          → │  │
│ │ 📤 Export Data               → │  │
│ └────────────────────────────────┘  │
└─────────────────────────────────────┘
```

---

## Feature Screens (Key Ones)

### Kundli Screen
```
┌─────────────────────────────────────┐
│ ← Kundli (Birth Chart)             │
│                                     │
│ [Form: Name, DOB, Time, Place]      │
│ [       Generate Kundli       ]     │
│                                     │
│ ━━ Results (Tabbed) ━━━━━━━━━━━━━  │
│ [Chart] [Planets] [Dasha] [Yoga]   │
│                                     │
│ ┌─────────────────────────────────┐ │
│ │     North Indian Chart          │ │
│ │    ┌───┬───┬───┬───┐           │ │
│ │    │12 │ 1 │ 2 │ 3 │           │ │
│ │    ├───┼───┼───┼───┤           │ │
│ │    │11 │       │ 4 │           │ │
│ │    ├───┤  ASC  ├───┤           │ │
│ │    │10 │       │ 5 │           │ │
│ │    ├───┼───┼───┼───┤           │ │
│ │    │ 9 │ 8 │ 7 │ 6 │           │ │
│ │    └───┴───┴───┴───┘           │ │
│ └─────────────────────────────────┘ │
│                                     │
│ [📄 Download PDF] [📤 Share]       │
└─────────────────────────────────────┘
```

### Mantra Counter (Jaap)
```
┌─────────────────────────────────────┐
│ ← Jaap Counter                      │
│                                     │
│         ┌─────────────┐            │
│         │             │            │
│         │    108      │            │
│         │   /1008     │            │
│         │             │            │
│         └─────────────┘            │
│                                     │
│    Om Namah Shivaya                 │
│                                     │
│         ┌───────────┐              │
│         │           │              │
│         │   TAP     │   ← Big     │
│         │   HERE    │     button   │
│         │           │              │
│         └───────────┘              │
│                                     │
│   ↺ Reset    ⏸️ Pause   🔔 Alert   │
│                                     │
│ Today: 432 | Total: 12,456         │
│                                     │
│ Mantra: [Om Namah Shivaya    ▼]    │
│ Target: [108 ▼]                     │
└─────────────────────────────────────┘
```

---

## Technical Architecture

```
com.astrologyvedic.app/
├── App.kt                        (Application class)
├── MainActivity.kt               (Single Activity)
├── di/                           (Dependency Injection - Hilt)
│   ├── AppModule.kt
│   ├── NetworkModule.kt
│   └── DatabaseModule.kt
├── data/
│   ├── api/
│   │   ├── AstrologyApi.kt       (Retrofit interface)
│   │   ├── models/               (API request/response DTOs)
│   │   └── interceptors/
│   ├── local/
│   │   ├── AppDatabase.kt        (Room DB)
│   │   ├── dao/                  (DAOs)
│   │   └── entities/             (DB entities)
│   └── repository/
│       ├── KundliRepository.kt
│       ├── PanchangRepository.kt
│       ├── ChatRepository.kt
│       └── ...
├── domain/
│   ├── model/                    (Domain models)
│   ├── usecase/                  (Use cases)
│   └── util/
├── ui/
│   ├── theme/
│   │   ├── Theme.kt             (Material 3 dark/saffron theme)
│   │   ├── Color.kt
│   │   ├── Type.kt
│   │   └── Shape.kt
│   ├── navigation/
│   │   ├── AppNavigation.kt     (NavHost + routes)
│   │   ├── BottomNavBar.kt
│   │   └── Routes.kt
│   ├── components/               (Reusable composables)
│   │   ├── PersonForm.kt
│   │   ├── RashiChart.kt
│   │   ├── LoadingState.kt
│   │   ├── ErrorState.kt
│   │   └── ShareButton.kt
│   ├── screens/
│   │   ├── home/
│   │   │   ├── HomeScreen.kt
│   │   │   └── HomeViewModel.kt
│   │   ├── explore/
│   │   │   └── ExploreScreen.kt
│   │   ├── chat/
│   │   │   ├── ChatScreen.kt
│   │   │   └── ChatViewModel.kt
│   │   ├── tools/
│   │   │   └── ToolsScreen.kt
│   │   ├── profile/
│   │   │   ├── ProfileScreen.kt
│   │   │   └── ProfileViewModel.kt
│   │   ├── kundli/
│   │   │   ├── KundliScreen.kt
│   │   │   └── KundliViewModel.kt
│   │   ├── daily/
│   │   ├── match/
│   │   ├── panchang/
│   │   ├── numerology/
│   │   ├── palm_reading/
│   │   ├── tarot/
│   │   ├── mantra_counter/
│   │   └── ... (all features)
│   └── MainScreen.kt            (Scaffold + BottomNav)
└── util/
    ├── DateUtils.kt
    ├── LocationUtils.kt
    └── ShareUtils.kt
```

---

## Dependencies

```kotlin
// Core
implementation("androidx.core:core-ktx:1.16.0")
implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.0")
implementation("androidx.activity:activity-compose:1.10.1")

// Compose
implementation(platform("androidx.compose:compose-bom:2025.05.00"))
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.material:material-icons-extended")

// Navigation
implementation("androidx.navigation:navigation-compose:2.9.0")

// Hilt (DI)
implementation("com.google.dagger:hilt-android:2.56")
kapt("com.google.dagger:hilt-compiler:2.56")
implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

// Networking
implementation("com.squareup.retrofit2:retrofit:2.11.0")
implementation("com.squareup.retrofit2:converter-gson:2.11.0")
implementation("com.squareup.okhttp3:okhttp:4.12.0")
implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

// Room (Local DB)
implementation("androidx.room:room-runtime:2.7.1")
implementation("androidx.room:room-ktx:2.7.1")
kapt("androidx.room:room-compiler:2.7.1")

// DataStore (Preferences)
implementation("androidx.datastore:datastore-preferences:1.1.7")

// Location
implementation("com.google.android.gms:play-services-location:21.3.0")

// Image loading
implementation("io.coil-kt:coil-compose:2.7.0")

// PDF generation
implementation("com.itextpdf:itext7-core:8.0.5")

// Splash screen
implementation("androidx.core:core-splashscreen:1.0.1")
```

---

## Color Theme (matching website)

```kotlin
// Dark cosmic theme with saffron accents
val Cosmic950 = Color(0xFF3b0764)  // darkest bg
val Cosmic900 = Color(0xFF581c87)
val Cosmic800 = Color(0xFF6b21a8)
val Cosmic700 = Color(0xFF7c3aed)
val Cosmic400 = Color(0xFFd946ef)
val Cosmic200 = Color(0xFFf5d0fe)

val Saffron500 = Color(0xFFf97316)
val Saffron400 = Color(0xFFfb923c)
val Saffron300 = Color(0xFFfdba74)

val DarkBg = Color(0xFF0b1020)     // Main background
val CardBg = Color(0xFF1a1040)     // Card surface
val BorderColor = Color(0xFF2d1b69)
```

---

## Features List (All 60+ from website)

### Core (Tab: Home)
1. Today's Panchang (auto-loads)
2. Daily Horoscope (12 rashis)
3. Lucky Today (number, color, direction)

### Main Services (Tab: Explore → Main)
4. Kundli Generation (birth chart)
5. Daily Analysis (personalized)
6. Kundli Matching (Ashta Koota)
7. Guna Milan (quick nakshatra)
8. Transit Report
9. Timeline (Dasha periods)
10. Life Report (comprehensive)

### AI Features (Tab: Chat + Explore → AI)
11. AI Astrologer Chat (dedicated tab)
12. Palm Reading (camera/gallery)
13. Face Reading
14. Tarot Card Reading
15. Pathfinder (life questions)
16. Yoga Detection
17. Past Life Reading

### Calculators (Tab: Tools)
18. Numerology
19. Rahu Kaal
20. Choghadiya
21. Hora
22. Sade Sati
23. Kaal Sarp Dosha
24. Muhurat Finder
25. Gemstone Recommendation
26. Sunrise/Sunset
27. Ayanamsa
28. Lucky Number/Color

### Spiritual (Tab: Tools)
29. Puja Guide
30. Jaap/Mantra Counter
31. Daily Mantra
32. Meditation Timer
33. Prayers/Stotras Library
34. Homa Guide
35. Festival Calendar
36. Vrat Calendar
37. Temple Finder

### Astrology Systems (Tab: Explore → Systems)
38. KP Astrology
39. Lal Kitab
40. Western Astrology
41. Chinese Zodiac
42. Nadi Astrology
43. Navamsa D9
44. Dasamsa D10
45. 16 Varga Charts

### Matching (Tab: Explore → Matching)
46. Full Kundli Match
47. Love Compatibility
48. 10 Porutham
49. Name Match
50. Guna Milan

### More (Tab: Explore → More)
51. Baby Names
52. Celebrity Horoscopes
53. Chart Comparison
54. Birth Rectification
55. Stock Astrology
56. Horoscope Share (social cards)
57. Vastu
58. Birth Rectification

### Profile & Settings
59. Multi-profile (family)
60. Report History
61. PDF/Image Export
62. Language (10 languages)
63. Dark/Light theme
64. Notifications (daily horoscope push)
65. Chart style preference (N/S Indian)

---

## Build Phases

### Phase 1: Foundation
- Project setup with Hilt, Retrofit, Room, Navigation
- Theme (dark cosmic + saffron)
- Bottom nav with 5 tabs
- Home screen with Panchang + Horoscope
- Profile screen with birth details form
- API integration layer

### Phase 2: Core Features
- Kundli screen with chart rendering
- Daily analysis
- Kundli matching + Guna Milan
- Transit report
- Timeline/Dasha

### Phase 3: AI Chat
- Full chat UI with message bubbles
- Quick question chips
- Birth chart context sent with each message
- Chat history (Room DB)

### Phase 4: Calculators & Spiritual
- All calculator screens
- Mantra counter with haptic feedback
- Meditation timer
- Festival calendar

### Phase 5: Advanced Systems
- KP, Lal Kitab, Western, Chinese
- Divisional charts
- Palm/Face reading (camera integration)
- Tarot card animations

### Phase 6: Polish
- Push notifications (daily horoscope)
- Social sharing (image generation)
- PDF export
- Offline caching
- Multi-language
- Widget (home screen)

---

## API Base URL
```
Production: https://astrologyvedic.com/api/
```

All endpoints same as web — POST requests with JSON body.
