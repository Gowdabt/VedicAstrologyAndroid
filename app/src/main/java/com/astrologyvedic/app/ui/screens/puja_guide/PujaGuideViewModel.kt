package com.astrologyvedic.app.ui.screens.puja_guide

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PujaInfo(
    val name: String,
    val deity: String,
    val benefits: String,
    val materials: List<String> = emptyList(),
    val steps: List<String> = emptyList()
)

data class PujaGuideUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val pujas: List<PujaInfo> = emptyList(),
    val selectedPuja: PujaInfo? = null,
    val hasDetail: Boolean = false
)

@HiltViewModel
class PujaGuideViewModel @Inject constructor(
    private val astrologyRepository: AstrologyRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PujaGuideUiState(pujas = getDefaultPujas()))
    val uiState: StateFlow<PujaGuideUiState> = _uiState.asStateFlow()

    fun selectPuja(puja: PujaInfo) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            astrologyRepository.getPujaGuide(mapOf("puja" to puja.name))
                .onSuccess { response ->
                    val detail = parsePujaDetail(response, puja)
                    _uiState.value = _uiState.value.copy(isLoading = false, selectedPuja = detail, hasDetail = true)
                }
                .onFailure {
                    _uiState.value = _uiState.value.copy(isLoading = false, selectedPuja = puja, hasDetail = true)
                }
        }
    }

    fun clearSelection() {
        _uiState.value = _uiState.value.copy(selectedPuja = null, hasDetail = false)
    }

    private fun parsePujaDetail(response: JsonObject, fallback: PujaInfo): PujaInfo {
        return try {
            val analysis = response.getAsJsonObject("analysis")
            val materials = mutableListOf<String>()
            val steps = mutableListOf<String>()
            analysis?.getAsJsonArray("materials")?.forEach { materials.add(it.asString) }
            analysis?.getAsJsonArray("steps")?.forEach { steps.add(it.asString) }
            fallback.copy(materials = materials, steps = steps, benefits = analysis?.get("benefits")?.asString ?: fallback.benefits)
        } catch (_: Exception) { fallback }
    }

    private fun getDefaultPujas(): List<PujaInfo> = listOf(
        PujaInfo("Ganesh Puja", "Lord Ganesha", "Remove obstacles, new beginnings",
            listOf("Modak", "Durva grass", "Red flowers", "Coconut", "Incense"), listOf("Invoke Ganesha", "Offer flowers", "Chant Om Gan Ganapataye Namah 108 times", "Offer Modak", "Perform Aarti")),
        PujaInfo("Lakshmi Puja", "Goddess Lakshmi", "Wealth, prosperity",
            listOf("Lotus flowers", "Gold coins", "Rice", "Turmeric", "Vermillion"), listOf("Clean altar", "Place Lakshmi idol", "Light diya", "Offer lotus", "Chant Sri Suktam", "Perform Aarti")),
        PujaInfo("Shiva Puja", "Lord Shiva", "Destruction of negativity, spiritual growth",
            listOf("Bel leaves", "Milk", "Water", "White flowers", "Dhatura"), listOf("Abhishekam with water", "Milk abhishekam", "Offer Bel leaves", "Chant Om Namah Shivaya", "Perform Aarti")),
        PujaInfo("Saraswati Puja", "Goddess Saraswati", "Knowledge, wisdom, arts",
            listOf("White flowers", "Books", "Musical instruments", "White cloth", "Fruits"), listOf("Place Saraswati idol on white cloth", "Offer white flowers", "Place books for blessing", "Chant Saraswati Vandana", "Perform Aarti")),
        PujaInfo("Navgraha Puja", "Nine Planets", "Balance planetary influences",
            listOf("Nine types of grains", "Nine colored cloths", "Flowers", "Ghee lamp"), listOf("Set up Navgraha yantra", "Offer specific grains to each planet", "Chant individual planet mantras", "Perform Havan", "Perform Aarti")),
        PujaInfo("Satyanarayan Puja", "Lord Vishnu", "Fulfillment of wishes, family harmony",
            listOf("Banana", "Wheat flour", "Sugar", "Ghee", "Tulsi"), listOf("Prepare Prasad", "Invoke Lord Vishnu", "Read Satyanarayan Katha", "Offer Prasad", "Distribute to all"))
    )
}
