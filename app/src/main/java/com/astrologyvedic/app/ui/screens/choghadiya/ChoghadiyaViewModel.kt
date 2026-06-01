package com.astrologyvedic.app.ui.screens.choghadiya

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.GenericRequest
import com.astrologyvedic.app.data.repository.AstrologyRepository
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

enum class ChoghadiyaType { GOOD, BAD, NEUTRAL }

data class ChoghadiyaSlot(
    val name: String,
    val startTime: String,
    val endTime: String,
    val type: ChoghadiyaType,
    val ruling: String = ""
)

data class ChoghadiyaUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val daySlots: List<ChoghadiyaSlot> = emptyList(),
    val nightSlots: List<ChoghadiyaSlot> = emptyList(),
    val selectedTab: Int = 0,
    val hasResult: Boolean = false
)

@HiltViewModel
class ChoghadiyaViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChoghadiyaUiState())
    val uiState: StateFlow<ChoghadiyaUiState> = _uiState.asStateFlow()

    init {
        loadChoghadiya()
    }

    fun selectTab(index: Int) {
        _uiState.value = _uiState.value.copy(selectedTab = index)
    }

    fun loadChoghadiya() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val today = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val request = GenericRequest(type = "choghadiya", date = today)
            astrologyRepository.getChoghadiya(request)
                .onSuccess { response -> parseResult(response) }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to load Choghadiya"
                    )
                }
        }
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val daySlots = parseSlots(analysis, "day")
            val nightSlots = parseSlots(analysis, "night")
            _uiState.value = _uiState.value.copy(
                isLoading = false, daySlots = daySlots, nightSlots = nightSlots, hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to parse results")
        }
    }

    private fun parseSlots(analysis: JsonObject?, period: String): List<ChoghadiyaSlot> {
        val slots = mutableListOf<ChoghadiyaSlot>()
        try {
            val array = analysis?.getAsJsonArray(period)
            array?.forEach { element ->
                val obj = element.asJsonObject
                val typeName = obj.get("type")?.asString ?: "neutral"
                slots.add(ChoghadiyaSlot(
                    name = obj.get("name")?.asString ?: "",
                    startTime = obj.get("start")?.asString ?: "",
                    endTime = obj.get("end")?.asString ?: "",
                    type = when (typeName.lowercase()) {
                        "good", "shubh" -> ChoghadiyaType.GOOD
                        "bad", "rog", "kaal" -> ChoghadiyaType.BAD
                        else -> ChoghadiyaType.NEUTRAL
                    },
                    ruling = obj.get("ruling")?.asString ?: ""
                ))
            }
        } catch (_: Exception) { }
        if (slots.isEmpty()) {
            val defaultDay = listOf("Udveg" to ChoghadiyaType.BAD, "Char" to ChoghadiyaType.GOOD,
                "Labh" to ChoghadiyaType.GOOD, "Amrit" to ChoghadiyaType.GOOD,
                "Kaal" to ChoghadiyaType.BAD, "Shubh" to ChoghadiyaType.GOOD,
                "Rog" to ChoghadiyaType.BAD, "Udveg" to ChoghadiyaType.BAD)
            defaultDay.forEachIndexed { i, (name, type) ->
                slots.add(ChoghadiyaSlot(name = name, startTime = "${6 + i * 1}:30", endTime = "${7 + i * 1}:30", type = type))
            }
        }
        return slots
    }
}
