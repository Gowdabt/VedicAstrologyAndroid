package com.astrologyvedic.app.ui.screens.tarot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class SpreadType(val displayName: String, val cardCount: Int) {
    SINGLE("Single Card", 1),
    THREE_CARD("Three Card (Past/Present/Future)", 3),
    CELTIC_CROSS("Celtic Cross", 10)
}

data class TarotCard(
    val name: String,
    val position: String,
    val interpretation: String,
    val isReversed: Boolean = false,
    val colorHex: Long = 0xFF3D2B89
)

data class TarotUiState(
    val selectedSpread: SpreadType = SpreadType.SINGLE,
    val question: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val cards: List<TarotCard> = emptyList(),
    val overallMessage: String = "",
    val hasResult: Boolean = false,
    val isFlipped: Boolean = false
)

@HiltViewModel
class TarotViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TarotUiState())
    val uiState: StateFlow<TarotUiState> = _uiState.asStateFlow()

    fun selectSpread(spread: SpreadType) {
        _uiState.value = _uiState.value.copy(selectedSpread = spread)
    }

    fun updateQuestion(question: String) {
        _uiState.value = _uiState.value.copy(question = question)
    }

    fun drawCards() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null, isFlipped = false)

            val request = buildMap {
                put("spread_type", _uiState.value.selectedSpread.name.lowercase())
                put("question", _uiState.value.question)
                put("card_count", _uiState.value.selectedSpread.cardCount)
            }

            astrologyRepository.getTarot(request)
                .onSuccess { response ->
                    parseResult(response)
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Failed to draw cards"
                    )
                }
        }
    }

    fun flipCards() {
        _uiState.value = _uiState.value.copy(isFlipped = true)
    }

    fun reset() {
        _uiState.value = TarotUiState()
    }

    private fun parseResult(response: JsonObject) {
        try {
            val analysis = response.getAsJsonObject("analysis")
            val cards = mutableListOf<TarotCard>()

            try {
                val cardsArray = analysis?.getAsJsonArray("cards")
                cardsArray?.forEach { element ->
                    val obj = element.asJsonObject
                    cards.add(
                        TarotCard(
                            name = obj.get("name")?.asString ?: "",
                            position = obj.get("position")?.asString ?: "",
                            interpretation = obj.get("interpretation")?.asString ?: "",
                            isReversed = obj.get("reversed")?.asBoolean ?: false
                        )
                    )
                }
            } catch (_: Exception) { }

            if (cards.isEmpty()) {
                cards.addAll(getDefaultCards())
            }

            val overall = analysis?.get("overall_message")?.asString
                ?: "The cards reveal a period of transformation and growth. Trust your intuition."

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                cards = cards,
                overallMessage = overall,
                hasResult = true
            )
        } catch (_: Exception) {
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                cards = getDefaultCards(),
                overallMessage = "The cards reveal a period of transformation and growth. Trust your intuition.",
                hasResult = true
            )
        }
    }

    private fun getDefaultCards(): List<TarotCard> {
        val spread = _uiState.value.selectedSpread
        val allCards = listOf(
            TarotCard("The Fool", "Present", "New beginnings and unlimited potential await you.", colorHex = 0xFF5B3FB0),
            TarotCard("The Magician", "Influence", "You have all the tools needed to manifest your desires.", colorHex = 0xFFEA580C),
            TarotCard("The High Priestess", "Past", "Hidden knowledge and intuition guide your path.", colorHex = 0xFF2D1B69),
            TarotCard("The Empress", "Future", "Abundance, creativity, and nurturing energy surround you.", colorHex = 0xFF4ADE80),
            TarotCard("The Emperor", "Foundation", "Structure and authority provide stability.", colorHex = 0xFFEF4444),
            TarotCard("The Star", "Hopes", "Hope and inspiration illuminate your journey.", colorHex = 0xFF60A5FA),
            TarotCard("The Moon", "Challenges", "Illusions may cloud judgment. Seek clarity.", colorHex = 0xFF9B7DD4),
            TarotCard("The Sun", "Outcome", "Joy, success, and vitality are on the horizon.", colorHex = 0xFFFBBF24),
            TarotCard("Wheel of Fortune", "Above", "Cycles of change bring new opportunities.", colorHex = 0xFF3D2B89),
            TarotCard("The World", "Final Outcome", "Completion and fulfillment of your goals.", colorHex = 0xFF4ADE80)
        )
        return allCards.take(spread.cardCount)
    }
}
