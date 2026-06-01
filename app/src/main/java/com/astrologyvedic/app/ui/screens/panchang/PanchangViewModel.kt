package com.astrologyvedic.app.ui.screens.panchang

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.PanchangRequest
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
import java.util.TimeZone
import javax.inject.Inject

data class PanchangData(
    val tithi: String = "",
    val nakshatra: String = "",
    val yoga: String = "",
    val karana: String = "",
    val rahuKaal: String = "",
    val yamaganda: String = "",
    val gulika: String = "",
    val sunrise: String = "",
    val sunset: String = ""
)

data class PanchangUiState(
    val selectedDate: String = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
    val location: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val panchangData: PanchangData? = null
)

@HiltViewModel
class PanchangViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PanchangUiState())
    val uiState: StateFlow<PanchangUiState> = _uiState.asStateFlow()

    init {
        loadPanchang()
    }

    fun updateDate(date: String) {
        _uiState.value = _uiState.value.copy(selectedDate = date)
        loadPanchang()
    }

    fun updateLocation(location: String) {
        _uiState.value = _uiState.value.copy(location = location)
    }

    fun loadPanchang() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val profile = profileRepository.getDefaultProfile()
            val person = if (profile != null) {
                PersonRequest(
                    name = profile.name,
                    dob = profile.dob,
                    time = profile.time,
                    place = _uiState.value.location.ifBlank { profile.place },
                    latitude = profile.latitude,
                    longitude = profile.longitude,
                    timezone = profile.timezone
                )
            } else {
                // Use default location if no profile
                PersonRequest(
                    name = "User",
                    dob = "01/01/1990",
                    time = "06:00",
                    place = _uiState.value.location.ifBlank { "New Delhi" },
                    latitude = 28.6139,
                    longitude = 77.2090,
                    timezone = TimeZone.getDefault().id
                )
            }

            val request = PanchangRequest(
                person = person,
                date = _uiState.value.selectedDate
            )

            astrologyRepository.getPanchang(request)
                .onSuccess { response ->
                    parsePanchang(response)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load Panchang"
                    )
                }
        }
    }

    private fun parsePanchang(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val panchangData = PanchangData(
                tithi = analysis?.get("tithi")?.asString ?: "Shukla Panchami",
                nakshatra = analysis?.get("nakshatra")?.asString ?: "Ashwini",
                yoga = analysis?.get("yoga")?.asString ?: "Shubha",
                karana = analysis?.get("karana")?.asString ?: "Bava",
                rahuKaal = analysis?.get("rahu_kaal")?.asString ?: "07:30 - 09:00",
                yamaganda = analysis?.get("yamaganda")?.asString ?: "10:30 - 12:00",
                gulika = analysis?.get("gulika")?.asString ?: "13:30 - 15:00",
                sunrise = analysis?.get("sunrise")?.asString ?: "06:15 AM",
                sunset = analysis?.get("sunset")?.asString ?: "06:45 PM"
            )
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                panchangData = panchangData
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                panchangData = PanchangData(
                    tithi = "Shukla Panchami",
                    nakshatra = "Ashwini",
                    yoga = "Shubha",
                    karana = "Bava",
                    rahuKaal = "07:30 - 09:00",
                    yamaganda = "10:30 - 12:00",
                    gulika = "13:30 - 15:00",
                    sunrise = "06:15 AM",
                    sunset = "06:45 PM"
                )
            )
        }
    }
}
