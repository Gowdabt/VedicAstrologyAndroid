package com.astrologyvedic.app.ui.screens.temple_finder

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

data class TempleInfo(
    val name: String,
    val location: String,
    val significance: String
)

data class PlanetTemples(
    val planet: String,
    val temples: List<TempleInfo>
)

data class TempleFinderUiState(
    val planetTemples: List<PlanetTemples> = emptyList(),
    val selectedPlanet: Int = 0
)

@HiltViewModel
class TempleFinderViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(TempleFinderUiState(planetTemples = getDefaultTemples()))
    val uiState: StateFlow<TempleFinderUiState> = _uiState.asStateFlow()

    fun selectPlanet(index: Int) {
        _uiState.value = _uiState.value.copy(selectedPlanet = index)
    }

    private fun getDefaultTemples(): List<PlanetTemples> = listOf(
        PlanetTemples("Surya (Sun)", listOf(
            TempleInfo("Suryanar Kovil", "Tamil Nadu", "Main Navagraha temple for Sun"),
            TempleInfo("Konark Sun Temple", "Odisha", "UNESCO World Heritage site")
        )),
        PlanetTemples("Chandra (Moon)", listOf(
            TempleInfo("Thingalur", "Tamil Nadu", "Navagraha temple for Moon"),
            TempleInfo("Somnath Temple", "Gujarat", "Jyotirlinga associated with Moon")
        )),
        PlanetTemples("Mangal (Mars)", listOf(
            TempleInfo("Vaitheeswaran Kovil", "Tamil Nadu", "Navagraha temple for Mars"),
            TempleInfo("Mangalnath Temple", "Ujjain", "Birth place of Mars")
        )),
        PlanetTemples("Budha (Mercury)", listOf(
            TempleInfo("Thiruvenkadu", "Tamil Nadu", "Navagraha temple for Mercury")
        )),
        PlanetTemples("Guru (Jupiter)", listOf(
            TempleInfo("Alangudi", "Tamil Nadu", "Navagraha temple for Jupiter")
        )),
        PlanetTemples("Shukra (Venus)", listOf(
            TempleInfo("Kanjanur", "Tamil Nadu", "Navagraha temple for Venus")
        )),
        PlanetTemples("Shani (Saturn)", listOf(
            TempleInfo("Thirunallar", "Tamil Nadu", "Navagraha temple for Saturn"),
            TempleInfo("Shani Shingnapur", "Maharashtra", "Famous Shani temple")
        )),
        PlanetTemples("Rahu", listOf(
            TempleInfo("Thirunageswaram", "Tamil Nadu", "Navagraha temple for Rahu")
        )),
        PlanetTemples("Ketu", listOf(
            TempleInfo("Keezhperumpallam", "Tamil Nadu", "Navagraha temple for Ketu")
        ))
    )
}
