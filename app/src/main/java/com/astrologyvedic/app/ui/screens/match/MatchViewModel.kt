package com.astrologyvedic.app.ui.screens.match

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.MatchRequest
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

data class KootaScore(
    val name: String,
    val obtained: Int,
    val maximum: Int,
    val description: String = ""
)

data class MatchUiState(
    val person1Form: PersonFormState = PersonFormState(),
    val person2Form: PersonFormState = PersonFormState(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val totalScore: Int = 0,
    val maxScore: Int = 36,
    val kootaScores: List<KootaScore> = emptyList(),
    val verdict: String = "",
    val hasResult: Boolean = false
)

@HiltViewModel
class MatchViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MatchUiState())
    val uiState: StateFlow<MatchUiState> = _uiState.asStateFlow()

    init {
        loadDefaultAsPersonOne()
    }

    private fun loadDefaultAsPersonOne() {
        viewModelScope.launch {
            profileRepository.getDefaultProfile()?.let { profile ->
                _uiState.value = _uiState.value.copy(
                    person1Form = PersonFormState(
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

    fun updatePerson1Form(form: PersonFormState) {
        _uiState.value = _uiState.value.copy(person1Form = form)
    }

    fun updatePerson2Form(form: PersonFormState) {
        _uiState.value = _uiState.value.copy(person2Form = form)
    }

    fun checkCompatibility() {
        val person1 = _uiState.value.person1Form.toPersonRequest() ?: return
        val person2 = _uiState.value.person2Form.toPersonRequest() ?: return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val request = MatchRequest(person1 = person1, person2 = person2)

            astrologyRepository.getMatch(request)
                .onSuccess { response ->
                    parseResult(response)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to check compatibility"
                    )
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val totalScore = analysis?.get("total_score")?.asInt ?: 0
            val maxScore = analysis?.get("max_score")?.asInt ?: 36

            val kootaScores = mutableListOf<KootaScore>()
            try {
                val kootas = analysis?.getAsJsonArray("kootas")
                kootas?.forEach { element ->
                    val obj = element.asJsonObject
                    kootaScores.add(
                        KootaScore(
                            name = obj.get("name")?.asString ?: "",
                            obtained = obj.get("obtained")?.asInt ?: 0,
                            maximum = obj.get("maximum")?.asInt ?: 0,
                            description = obj.get("description")?.asString ?: ""
                        )
                    )
                }
            } catch (_: Exception) {
                // Use default kootas
                kootaScores.addAll(getDefaultKootas())
            }

            if (kootaScores.isEmpty()) {
                kootaScores.addAll(getDefaultKootas())
            }

            val verdict = when {
                totalScore >= 28 -> "Highly Compatible"
                totalScore >= 21 -> "Good Match"
                totalScore >= 14 -> "Average"
                else -> "Below Average"
            }

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                totalScore = totalScore,
                maxScore = maxScore,
                kootaScores = kootaScores,
                verdict = verdict,
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                error = "Failed to parse match results"
            )
        }
    }

    private fun getDefaultKootas(): List<KootaScore> = listOf(
        KootaScore("Varna", 0, 1, "Spiritual compatibility"),
        KootaScore("Vasya", 0, 2, "Mutual attraction"),
        KootaScore("Tara", 0, 3, "Destiny compatibility"),
        KootaScore("Yoni", 0, 4, "Physical compatibility"),
        KootaScore("Graha Maitri", 0, 5, "Mental compatibility"),
        KootaScore("Gana", 0, 6, "Temperament"),
        KootaScore("Bhakoot", 0, 7, "Love and affection"),
        KootaScore("Nadi", 0, 8, "Health and genes")
    )
}
