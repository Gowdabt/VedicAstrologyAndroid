package com.astrologyvedic.app.ui.screens.kundli

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.KundliRequest
import com.astrologyvedic.app.data.api.models.PersonRequest
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.astrologyvedic.app.ui.components.PersonFormState
import com.astrologyvedic.app.ui.components.toPersonRequest
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PlanetInfo(
    val planet: String,
    val sign: String,
    val house: String,
    val nakshatra: String,
    val degree: String
)

data class KundliUiState(
    val personForm: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val result: JsonObject? = null,
    val planets: List<PlanetInfo> = emptyList(),
    val selectedTab: Int = 0
)

@HiltViewModel
class KundliViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(KundliUiState())
    val uiState: StateFlow<KundliUiState> = _uiState.asStateFlow()

    init {
        loadDefaultProfile()
    }

    private fun loadDefaultProfile() {
        viewModelScope.launch {
            profileRepository.getDefaultProfile()?.let { profile ->
                _uiState.value = _uiState.value.copy(
                    personForm = PersonFormState(
                        name = profile.name,
                        dob = profile.dob,
                        time = profile.time,
                        place = profile.place,
                        latitude = profile.latitude.toString(),
                        longitude = profile.longitude.toString()
                    )
                )
            }
        }
    }

    fun updatePersonForm(form: PersonFormState) {
        _uiState.value = _uiState.value.copy(personForm = form)
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun generateKundli() {
        val person = _uiState.value.personForm.toPersonRequest() ?: return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val request = KundliRequest(person = person)
            astrologyRepository.getKundli(request)
                .onSuccess { response ->
                    val planets = parsePlanets(response)
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        result = response,
                        planets = planets
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to generate Kundli"
                    )
                }
        }
    }

    private fun parsePlanets(response: JsonObject): List<PlanetInfo> {
        val planets = mutableListOf<PlanetInfo>()
        try {
            val analysis = response.getAsJsonObject("analysis")
            val planetsArray = analysis?.getAsJsonArray("planets")
            planetsArray?.forEach { element ->
                val obj = element.asJsonObject
                planets.add(
                    PlanetInfo(
                        planet = obj.get("name")?.asString ?: "",
                        sign = obj.get("sign")?.asString ?: "",
                        house = obj.get("house")?.asString ?: "",
                        nakshatra = obj.get("nakshatra")?.asString ?: "",
                        degree = obj.get("degree")?.asString ?: ""
                    )
                )
            }
        } catch (_: Exception) {
            // Return default planets if parsing fails
            val defaultPlanets = listOf("Sun", "Moon", "Mars", "Mercury", "Jupiter", "Venus", "Saturn", "Rahu", "Ketu")
            defaultPlanets.forEach { name ->
                planets.add(PlanetInfo(planet = name, sign = "--", house = "--", nakshatra = "--", degree = "--"))
            }
        }
        return planets
    }
}
