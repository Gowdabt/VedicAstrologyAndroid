package com.astrologyvedic.app.ui.screens.horoscope_share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.AstrologyRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HoroscopeShareUiState(val isLoading: Boolean = false, val error: String? = null, val rashi: String = "", val prediction: String = "", val luckyNumber: String = "", val hasResult: Boolean = false)

@HiltViewModel
class HoroscopeShareViewModel @Inject constructor(private val astrologyRepository: AstrologyRepository, private val profileRepository: ProfileRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HoroscopeShareUiState())
    val uiState: StateFlow<HoroscopeShareUiState> = _uiState.asStateFlow()
    init { loadHoroscope() }
    fun loadHoroscope() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            val profile = profileRepository.getDefaultProfile()
            astrologyRepository.getTools(mapOf("type" to "horoscope_share", "name" to (profile?.name ?: ""), "dob" to (profile?.dob ?: "")))
                .onSuccess { r -> val a = r.getAsJsonObject("analysis"); _uiState.value = _uiState.value.copy(isLoading = false, rashi = a?.get("rashi")?.asString ?: "", prediction = a?.get("prediction")?.asString ?: "", luckyNumber = a?.get("lucky_number")?.asString ?: "", hasResult = true) }
                .onFailure { e -> _uiState.value = _uiState.value.copy(isLoading = false, error = e.message ?: "Failed") }
        }
    }
}
