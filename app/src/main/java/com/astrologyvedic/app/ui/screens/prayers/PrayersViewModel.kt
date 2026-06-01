package com.astrologyvedic.app.ui.screens.prayers

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class Prayer(
    val title: String,
    val text: String
)

data class DeityPrayers(
    val deity: String,
    val prayers: List<Prayer>,
    val isExpanded: Boolean = false
)

data class PrayersUiState(
    val deities: List<DeityPrayers> = emptyList()
)

@HiltViewModel
class PrayersViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(PrayersUiState(deities = getDefaultPrayers()))
    val uiState: StateFlow<PrayersUiState> = _uiState.asStateFlow()

    fun toggleDeity(index: Int) {
        val deities = _uiState.value.deities.toMutableList()
        deities[index] = deities[index].copy(isExpanded = !deities[index].isExpanded)
        _uiState.value = _uiState.value.copy(deities = deities)
    }

    private fun getDefaultPrayers(): List<DeityPrayers> = listOf(
        DeityPrayers("Lord Ganesha", listOf(
            Prayer("Ganesh Vandana", "Vakratunda Mahakaya Suryakoti Samaprabha\nNirvighnam Kuru Me Deva Sarva Kaaryeshu Sarvada"),
            Prayer("Ganesh Aarti", "Jai Ganesh Jai Ganesh Jai Ganesh Deva\nMata Jaki Parvati Pita Mahadeva")
        )),
        DeityPrayers("Lord Shiva", listOf(
            Prayer("Shiv Stuti", "Karpur Gauram Karunavataram\nSansara Saram Bhujagendra Haram"),
            Prayer("Maha Mrityunjaya", "Om Tryambakam Yajamahe Sugandhim Pushtivardhanam\nUrvarukamiva Bandhanan Mrityor Mukshiya Maamritat")
        )),
        DeityPrayers("Lord Vishnu", listOf(
            Prayer("Vishnu Stotram", "Shantakaram Bhujagashayanam Padmanabham Suresham\nVishvadharam Gaganasadrisham Meghavarnam Shubhangam"),
            Prayer("Om Namo Bhagavate", "Om Namo Bhagavate Vasudevaya")
        )),
        DeityPrayers("Goddess Lakshmi", listOf(
            Prayer("Lakshmi Aarti", "Om Jai Lakshmi Mata, Maiya Jai Lakshmi Mata\nTumko Nishdin Sevat Hari Vishnu Dhata"),
            Prayer("Lakshmi Stotram", "Namastestu Mahamaye Shri Pithe Sura Pujite\nShankha Chakra Gada Haste Mahalakshmi Namostute")
        )),
        DeityPrayers("Goddess Saraswati", listOf(
            Prayer("Saraswati Vandana", "Ya Kundendu Tusharahara Dhavala Ya Shubhravastravrita\nYa Veena Varadanda Manditakara Ya Shveta Padmasana"),
            Prayer("Saraswati Stotram", "Saraswati Namastubhyam Varade Kamarupini\nVidyarambham Karishyami Siddhir Bhavatu Me Sada")
        )),
        DeityPrayers("Lord Hanuman", listOf(
            Prayer("Hanuman Chalisa (Opening)", "Shri Guru Charan Saroj Raj Nij Mann Mukur Sudhari\nBarnau Raghuvar Bimal Jasu Jo Dayaku Phal Chari"),
            Prayer("Bajrang Baan", "Nishchay Prey Shri Ram Ko Bachana\nSun Hanuman Atulith Bal Dhama")
        )),
        DeityPrayers("Lord Krishna", listOf(
            Prayer("Krishna Aarti", "Aarti Kunj Bihari Ki Shri Girdhar Krishna Murari Ki"),
            Prayer("Hare Krishna Mahamantra", "Hare Krishna Hare Krishna Krishna Krishna Hare Hare\nHare Rama Hare Rama Rama Rama Hare Hare")
        )),
        DeityPrayers("Sun God (Surya)", listOf(
            Prayer("Gayatri Mantra", "Om Bhur Bhuva Swaha Tat Savitur Varenyam\nBhargo Devasya Dhimahi Dhiyo Yo Nah Prachodayat"),
            Prayer("Surya Namaskar Mantra", "Om Mitraya Namaha Om Ravaye Namaha\nOm Suryaya Namaha Om Bhanave Namaha")
        ))
    )
}
