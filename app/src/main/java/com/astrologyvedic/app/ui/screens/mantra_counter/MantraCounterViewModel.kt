package com.astrologyvedic.app.ui.screens.mantra_counter

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.repository.MantraRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

data class MantraCounterUiState(
    val selectedMantra: String = "Om Namah Shivaya",
    val count: Int = 0,
    val target: Int = 108,
    val totalCount: Int = 0,
    val isComplete: Boolean = false
)

@HiltViewModel
class MantraCounterViewModel @Inject constructor(
    private val mantraRepository: MantraRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MantraCounterUiState())
    val uiState: StateFlow<MantraCounterUiState> = _uiState.asStateFlow()

    val mantras = listOf(
        "Om Namah Shivaya",
        "Om Gan Ganapataye Namah",
        "Hare Krishna Hare Rama",
        "Om Namo Bhagavate Vasudevaya",
        "Gayatri Mantra",
        "Maha Mrityunjaya Mantra",
        "Om Shri Lakshmi Namah",
        "Om Aim Saraswatyai Namah"
    )

    init { loadCounts() }

    private fun loadCounts() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val total = mantraRepository.getTotalCount(_uiState.value.selectedMantra)
            _uiState.value = _uiState.value.copy(totalCount = total)
        }
    }

    fun selectMantra(mantra: String) {
        _uiState.value = _uiState.value.copy(selectedMantra = mantra, count = 0, isComplete = false)
        loadCounts()
    }

    fun updateTarget(target: Int) {
        _uiState.value = _uiState.value.copy(target = target)
    }

    fun increment() {
        val newCount = _uiState.value.count + 1
        val isComplete = newCount >= _uiState.value.target
        _uiState.value = _uiState.value.copy(count = newCount, isComplete = isComplete, totalCount = _uiState.value.totalCount + 1)

        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            mantraRepository.incrementCount(_uiState.value.selectedMantra, today, _uiState.value.target)
        }
    }

    fun reset() {
        _uiState.value = _uiState.value.copy(count = 0, isComplete = false)
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            mantraRepository.resetCount(_uiState.value.selectedMantra, today)
        }
    }
}
