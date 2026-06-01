package com.astrologyvedic.app.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.GenericRequest
import com.astrologyvedic.app.data.api.models.PersonRequest
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.astrologyvedic.app.util.LocationHelper
import com.astrologyvedic.app.util.GpsLocationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class HomeUiState(
    val todayDate: String = "",
    val tithi: String = "",
    val nakshatra: String = "",
    val yoga: String = "",
    val rahuKaal: String = "",
    val yamaganda: String = "",
    val gulika: String = "",
    val isLoading: Boolean = false
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository,
    private val locationHelper: LocationHelper
) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadTodayPanchang()
    }

    private fun loadTodayPanchang() {
        val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        _uiState.value = _uiState.value.copy(
            todayDate = dateFormat.format(Date()),
            isLoading = true
        )

        viewModelScope.launch {
            // Fetch profile for location data
            val profile = profileRepository.getDefaultProfile()

            val latitude: Double
            val longitude: Double
            val place: String

            if (profile != null) {
                // Use saved profile location
                latitude = profile.latitude
                longitude = profile.longitude
                place = profile.place
            } else {
                // No profile exists, try GPS location
                if (locationHelper.hasLocationPermission(getApplication())) {
                    val locationResult = locationHelper.getCurrentLocationSuspend(getApplication())
                    val locationData = when (locationResult) {
                        is GpsLocationResult.Success -> Triple(
                            locationResult.latitude.toDoubleOrNull() ?: 28.6139,
                            locationResult.longitude.toDoubleOrNull() ?: 77.2090,
                            locationResult.placeName
                        )
                        is GpsLocationResult.Error -> Triple(28.6139, 77.2090, "New Delhi")
                    }
                    latitude = locationData.first
                    longitude = locationData.second
                    place = locationData.third
                } else {
                    // No permission, fallback to New Delhi
                    latitude = 28.6139
                    longitude = 77.2090
                    place = "New Delhi"
                }
            }

            val timezone = profile?.timezone ?: "+05:30"
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val todayTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())

            // Create PersonRequest with location data
            val personRequest = PersonRequest(
                name = profile?.name ?: "User",
                dob = profile?.dob ?: today,
                time = profile?.time ?: todayTime,
                place = place,
                latitude = latitude,
                longitude = longitude,
                timezone = timezone
            )

            val request = GenericRequest(
                person = personRequest,
                date = today,
                type = "panchang"
            )

            astrologyRepository.getPanchang(request)
                .onSuccess { response ->
                    try {
                        val analysis = response.getAsJsonObject("analysis")
                        val panchang = analysis?.getAsJsonObject("panchang")
                        val kalas = analysis?.getAsJsonObject("kalas")

                        // Tithi
                        val tithiObj = panchang?.getAsJsonObject("tithi")
                        val tithiName = tithiObj?.get("name")?.asString ?: ""
                        val paksha = tithiObj?.get("paksha")?.asString ?: ""
                        val tithiStr = if (paksha.isNotEmpty() && tithiName.isNotEmpty()) "$paksha $tithiName" else tithiName.ifEmpty { "—" }

                        // Nakshatra
                        val nakshatraObj = panchang?.getAsJsonObject("nakshatra")
                        val nakshatraStr = nakshatraObj?.get("name")?.asString ?: "—"

                        // Yoga
                        val yogaObj = panchang?.getAsJsonObject("yoga")
                        val yogaStr = yogaObj?.get("name")?.asString ?: "—"

                        // Rahu Kala
                        val rahuObj = kalas?.getAsJsonObject("rahu_kala")
                        val rahuStr = formatKala(rahuObj, "07:30 - 09:00")

                        // Yamaganda Kala
                        val yamaObj = kalas?.getAsJsonObject("yamaganda_kala")
                        val yamaStr = formatKala(yamaObj, "10:30 - 12:00")

                        // Gulika Kala
                        val gulikaObj = kalas?.getAsJsonObject("gulika_kala")
                        val gulikaStr = formatKala(gulikaObj, "13:30 - 15:00")

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            tithi = tithiStr,
                            nakshatra = nakshatraStr,
                            yoga = yogaStr,
                            rahuKaal = rahuStr,
                            yamaganda = yamaStr,
                            gulika = gulikaStr
                        )
                    } catch (_: Exception) {
                        setDefaultPanchang()
                    }
                }
                .onFailure {
                    setDefaultPanchang()
                }
        }
    }

    private fun formatKala(kalaObj: com.google.gson.JsonObject?, defaultValue: String): String {
        val start = kalaObj?.get("start")?.asString ?: ""
        val end = kalaObj?.get("end")?.asString ?: ""
        return if (start.isNotEmpty() && !start.contains("NaN") && end.isNotEmpty() && !end.contains("NaN")) {
            "$start - $end"
        } else {
            defaultValue
        }
    }

    private fun setDefaultPanchang() {
        _uiState.value = _uiState.value.copy(
            isLoading = false,
            tithi = "Shukla Panchami",
            nakshatra = "Ashwini",
            yoga = "Shubha",
            rahuKaal = "07:30 - 09:00",
            yamaganda = "10:30 - 12:00",
            gulika = "13:30 - 15:00"
        )
    }
}
