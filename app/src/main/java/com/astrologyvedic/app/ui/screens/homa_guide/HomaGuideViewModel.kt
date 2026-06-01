package com.astrologyvedic.app.ui.screens.homa_guide

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class HomaInfo(
    val name: String,
    val purpose: String,
    val materials: List<String>,
    val steps: List<String>,
    val mantras: List<String>,
    val isExpanded: Boolean = false
)

data class HomaGuideUiState(
    val homas: List<HomaInfo> = emptyList()
)

@HiltViewModel
class HomaGuideViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(HomaGuideUiState(homas = getDefaultHomas()))
    val uiState: StateFlow<HomaGuideUiState> = _uiState.asStateFlow()

    fun toggleHoma(index: Int) {
        val homas = _uiState.value.homas.toMutableList()
        homas[index] = homas[index].copy(isExpanded = !homas[index].isExpanded)
        _uiState.value = _uiState.value.copy(homas = homas)
    }

    private fun getDefaultHomas(): List<HomaInfo> = listOf(
        HomaInfo("Ganapathi Homam", "Remove obstacles", listOf("Ghee", "Modak", "Durva grass", "Red flowers", "Coconut"), listOf("Light the sacred fire", "Invoke Lord Ganesha", "Offer ghee into fire", "Chant mantras 108 times", "Offer Purnahuti"), listOf("Om Gam Ganapataye Namaha")),
        HomaInfo("Navagraha Homam", "Pacify planets", listOf("Nine grains", "Ghee", "Samidhas", "Nine types of flowers"), listOf("Set up Homa Kundam", "Light fire with samidhas", "Invoke nine planets", "Offer specific items per planet", "Purnahuti"), listOf("Om Suryaya Namaha", "Om Chandraya Namaha")),
        HomaInfo("Maha Mrityunjaya Homam", "Health and longevity", listOf("Ghee", "Durva grass", "White sesame", "Milk", "Honey"), listOf("Prepare Homa Kundam", "Light sacred fire", "Chant Mrityunjaya mantra", "Offer ghee with each chant", "Complete 108 offerings"), listOf("Om Tryambakam Yajamahe Sugandhim Pushtivardhanam")),
        HomaInfo("Sudarshana Homam", "Protection from evil", listOf("Ghee", "Red lotus", "Turmeric", "Kumkum", "Samidhas"), listOf("Set up fire altar", "Invoke Lord Sudarshana", "Offer ghee and flowers", "Chant Sudarshana mantra", "Purnahuti"), listOf("Om Sudarshanaaya Vidmahe")),
        HomaInfo("Lakshmi Kubera Homam", "Wealth and abundance", listOf("Ghee", "Gold coins (symbolic)", "Lotus", "Honey", "Rice"), listOf("Prepare sacred space", "Invoke Lakshmi and Kubera", "Offer ghee oblations", "Chant Sri Suktam", "Offer Purnahuti"), listOf("Om Shreem Mahalakshmiyei Namaha"))
    )
}
