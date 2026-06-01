package com.astrologyvedic.app.ui.screens.daily

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.DailyRequest
import com.astrologyvedic.app.data.api.models.PersonRequest
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class DailySection(
    val title: String,
    val content: String,
    val icon: String = ""
)

data class DailyUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val summary: String = "",
    val luckyColor: String = "",
    val luckyNumber: String = "",
    val sections: List<DailySection> = emptyList(),
    val remedies: List<String> = emptyList(),
    val hasProfile: Boolean = false
)

@HiltViewModel
class DailyViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyUiState())
    val uiState: StateFlow<DailyUiState> = _uiState.asStateFlow()

    init {
        loadDailyHoroscope()
    }

    fun loadDailyHoroscope() {
        viewModelScope.launch {
            val profile = profileRepository.getDefaultProfile()
            if (profile == null) {
                // Show generic horoscope without profile
                _uiState.value = _uiState.value.copy(
                    hasProfile = false,
                    isLoading = false,
                    error = null,
                    summary = "Welcome to Daily Horoscope! Create your profile for personalized predictions based on your birth chart.",
                    luckyColor = "Gold",
                    luckyNumber = "7",
                    sections = listOf(
                        DailySection("Career", "Focus on important tasks that require your attention. Good day for planning and organizing."),
                        DailySection("Love & Relationships", "Communication is key today. Express your feelings openly."),
                        DailySection("Health", "Take time for physical activity and proper rest. Stay hydrated."),
                        DailySection("Finance", "Review your expenses and budget carefully. Avoid impulse purchases.")
                    ),
                    remedies = listOf(
                        "Chant Om Namah Shivaya 108 times",
                        "Offer water to the Sun at sunrise",
                        "Practice gratitude meditation"
                    )
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true, error = null, hasProfile = true)

            val person = PersonRequest(
                name = profile.name,
                dob = profile.dob,
                time = profile.time,
                place = profile.place,
                latitude = profile.latitude,
                longitude = profile.longitude,
                timezone = profile.timezone
            )

            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val request = DailyRequest(person = person, date = today)

            astrologyRepository.getDaily(request)
                .onSuccess { response ->
                    parseResponse(response)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load daily horoscope"
                    )
                }
        }
    }

    private fun parseResponse(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val summary = analysis?.get("summary")?.asString ?: "Your daily horoscope is ready."
            val luckyColor = analysis?.get("lucky_color")?.asString ?: "Gold"
            val luckyNumber = analysis?.get("lucky_number")?.asString ?: "7"

            val sections = mutableListOf<DailySection>()
            analysis?.get("career")?.asString?.let {
                sections.add(DailySection("Career", it))
            }
            analysis?.get("love")?.asString?.let {
                sections.add(DailySection("Love & Relationships", it))
            }
            analysis?.get("health")?.asString?.let {
                sections.add(DailySection("Health", it))
            }
            analysis?.get("finance")?.asString?.let {
                sections.add(DailySection("Finance", it))
            }

            if (sections.isEmpty()) {
                sections.add(DailySection("Career", "Focus on tasks that require attention to detail today."))
                sections.add(DailySection("Love & Relationships", "Good day for communication with your partner."))
                sections.add(DailySection("Health", "Take time for physical activity and rest."))
                sections.add(DailySection("Finance", "Avoid major financial decisions today."))
            }

            val remedies = mutableListOf<String>()
            try {
                val remediesArray = analysis?.getAsJsonArray("remedies")
                remediesArray?.forEach { remedies.add(it.asString) }
            } catch (_: Exception) {
                remedies.add("Chant Om Namah Shivaya 108 times")
                remedies.add("Offer water to the Sun at sunrise")
                remedies.add("Wear your lucky color today")
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                summary = summary,
                luckyColor = luckyColor,
                luckyNumber = luckyNumber,
                sections = sections,
                remedies = remedies
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                summary = "Your daily horoscope is ready.",
                luckyColor = "Gold",
                luckyNumber = "7",
                sections = listOf(
                    DailySection("Career", "Focus on tasks that require attention to detail today."),
                    DailySection("Love & Relationships", "Good day for communication with your partner."),
                    DailySection("Health", "Take time for physical activity and rest."),
                    DailySection("Finance", "Avoid major financial decisions today.")
                ),
                remedies = listOf(
                    "Chant Om Namah Shivaya 108 times",
                    "Offer water to the Sun at sunrise",
                    "Wear your lucky color today"
                )
            )
        }
    }
}
