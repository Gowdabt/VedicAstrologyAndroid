package com.astrologyvedic.app.ui.screens.meditation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MeditationUiState(
    val selectedDuration: Int = 5,
    val remainingSeconds: Int = 300,
    val isRunning: Boolean = false,
    val isComplete: Boolean = false,
    val selectedSound: String = "Silent"
)

@HiltViewModel
class MeditationViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MeditationUiState())
    val uiState: StateFlow<MeditationUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    val durations = listOf(5, 10, 15, 20)
    val sounds = listOf("Silent", "Ocean Waves", "Rain", "Birds", "Om Chanting", "Temple Bells")

    fun selectDuration(minutes: Int) {
        if (!_uiState.value.isRunning) {
            _uiState.value = _uiState.value.copy(selectedDuration = minutes, remainingSeconds = minutes * 60, isComplete = false)
        }
    }

    fun selectSound(sound: String) {
        _uiState.value = _uiState.value.copy(selectedSound = sound)
    }

    fun toggleTimer() {
        if (_uiState.value.isRunning) {
            pause()
        } else {
            start()
        }
    }

    private fun start() {
        _uiState.value = _uiState.value.copy(isRunning = true, isComplete = false)
        timerJob = viewModelScope.launch {
            while (_uiState.value.remainingSeconds > 0 && _uiState.value.isRunning) {
                delay(1000)
                if (_uiState.value.isRunning) {
                    _uiState.value = _uiState.value.copy(remainingSeconds = _uiState.value.remainingSeconds - 1)
                }
            }
            if (_uiState.value.remainingSeconds <= 0) {
                _uiState.value = _uiState.value.copy(isRunning = false, isComplete = true)
            }
        }
    }

    private fun pause() {
        _uiState.value = _uiState.value.copy(isRunning = false)
        timerJob?.cancel()
    }

    fun reset() {
        timerJob?.cancel()
        _uiState.value = _uiState.value.copy(
            remainingSeconds = _uiState.value.selectedDuration * 60,
            isRunning = false, isComplete = false
        )
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}
