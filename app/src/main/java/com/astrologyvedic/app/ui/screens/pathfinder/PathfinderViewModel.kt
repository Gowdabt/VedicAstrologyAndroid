package com.astrologyvedic.app.ui.screens.pathfinder

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.PersonRequest
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PathfinderUiState(
    val selectedQuestion: String = "",
    val customQuestion: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val answer: String = "",
    val hasResult: Boolean = false,
    val hasProfile: Boolean = false
)

@HiltViewModel
class PathfinderViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PathfinderUiState())
    val uiState: StateFlow<PathfinderUiState> = _uiState.asStateFlow()

    val predefinedQuestions = listOf(
        "When will I get married?",
        "Job or Business?",
        "Will I become famous?",
        "When promotion?",
        "About my children",
        "Health outlook"
    )

    init {
        checkProfile()
    }

    private fun checkProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getDefaultProfile()
            _uiState.value = _uiState.value.copy(hasProfile = profile != null)
        }
    }

    fun selectQuestion(question: String) {
        _uiState.value = _uiState.value.copy(selectedQuestion = question)
        askQuestion(question)
    }

    fun updateCustomQuestion(question: String) {
        _uiState.value = _uiState.value.copy(customQuestion = question)
    }

    fun submitCustomQuestion() {
        val question = _uiState.value.customQuestion.trim()
        if (question.isNotBlank()) {
            askQuestion(question)
        }
    }

    fun reset() {
        _uiState.value = _uiState.value.copy(
            hasResult = false,
            answer = "",
            selectedQuestion = "",
            error = null
        )
    }

    private fun askQuestion(question: String) {
        viewModelScope.launch {
            val profile = profileRepository.getDefaultProfile()
            if (profile == null) {
                _uiState.value = _uiState.value.copy(
                    error = "Please set up your profile to get personalized answers."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
                selectedQuestion = question
            )

            val person = PersonRequest(
                name = profile.name,
                dob = profile.dob,
                time = profile.time,
                place = profile.place,
                latitude = profile.latitude,
                longitude = profile.longitude,
                timezone = profile.timezone
            )

            val request = buildMap {
                put("person", person)
                put("question", question)
                put("output_language", "en")
            }

            astrologyRepository.getPathfinder(request)
                .onSuccess { response ->
                    parseResult(response)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to get answer"
                    )
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val answer = analysis?.get("answer")?.asString
                ?: analysis?.get("response")?.asString
                ?: response.toString()

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                answer = answer,
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                answer = "Based on your birth chart analysis, the planetary positions suggest positive outcomes. Please consult a detailed reading for more specifics.",
                hasResult = true
            )
        }
    }
}
