package com.astrologyvedic.app.ui.screens.palm_reading

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.GenericRequest
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

data class PalmLine(
    val name: String,
    val description: String,
    val interpretation: String
)

data class PalmReadingUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasResult: Boolean = false,
    val palmLines: List<PalmLine> = emptyList(),
    val overallReading: String = "",
    val hasProfile: Boolean = false,
    val showCamera: Boolean = false
)

@HiltViewModel
class PalmReadingViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PalmReadingUiState())
    val uiState: StateFlow<PalmReadingUiState> = _uiState.asStateFlow()

    init {
        checkProfile()
    }

    private fun checkProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getDefaultProfile()
            _uiState.value = _uiState.value.copy(hasProfile = profile != null)
        }
    }

    fun onCameraClick() {
        _uiState.value = _uiState.value.copy(showCamera = true)
    }

    fun onImageCaptured() {
        // Since we use DOB-based palm analysis, trigger the reading
        _uiState.value = _uiState.value.copy(showCamera = false)
        generateReading()
    }

    fun onGallerySelected() {
        // Same flow - trigger DOB-based reading
        generateReading()
    }

    fun generateReading() {
        viewModelScope.launch {
            val profile = profileRepository.getDefaultProfile()
            if (profile == null) {
                _uiState.value = _uiState.value.copy(
                    error = "Please set up your profile first."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            val person = PersonRequest(
                name = profile.name,
                dob = profile.dob,
                time = profile.time,
                place = profile.place,
                latitude = profile.latitude,
                longitude = profile.longitude,
                timezone = profile.timezone
            )

            val request = GenericRequest(person = person)
            astrologyRepository.getPalmReading(request)
                .onSuccess { response ->
                    parseResult(response)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to generate palm reading"
                    )
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val palmLines = mutableListOf<PalmLine>()

            try {
                val linesArray = analysis?.getAsJsonArray("lines")
                linesArray?.forEach { element ->
                    val obj = element.asJsonObject
                    palmLines.add(
                        PalmLine(
                            name = obj.get("name")?.asString ?: "",
                            description = obj.get("description")?.asString ?: "",
                            interpretation = obj.get("interpretation")?.asString ?: ""
                        )
                    )
                }
            } catch (_: Exception) { }

            if (palmLines.isEmpty()) {
                palmLines.addAll(getDefaultPalmLines())
            }

            val overall = analysis?.get("overall")?.asString
                ?: "Based on your birth chart analysis, your palm reveals a life of growth, determination, and success."

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasResult = true,
                palmLines = palmLines,
                overallReading = overall
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                hasResult = true,
                palmLines = getDefaultPalmLines(),
                overallReading = "Based on your birth chart analysis, your palm reveals a life of growth, determination, and success."
            )
        }
    }

    private fun getDefaultPalmLines(): List<PalmLine> = listOf(
        PalmLine(
            name = "Heart Line",
            description = "Starts from the edge of the palm under the little finger",
            interpretation = "Your heart line suggests deep emotional connections and a passionate nature in relationships."
        ),
        PalmLine(
            name = "Head Line",
            description = "Starts between the thumb and index finger",
            interpretation = "A clear head line indicates strong analytical thinking and creative problem-solving abilities."
        ),
        PalmLine(
            name = "Life Line",
            description = "Curves around the base of the thumb",
            interpretation = "Your life line shows vitality and enthusiasm for life with potential for significant journeys."
        ),
        PalmLine(
            name = "Fate Line",
            description = "Runs from the base of the palm towards the middle finger",
            interpretation = "The fate line reveals a strong sense of purpose and career achievements through self-determination."
        )
    )
}
