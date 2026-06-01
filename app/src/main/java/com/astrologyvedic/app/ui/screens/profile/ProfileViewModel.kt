package com.astrologyvedic.app.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.local.dao.ReportHistoryDao
import com.astrologyvedic.app.data.local.entities.ProfileEntity
import com.astrologyvedic.app.data.local.entities.ReportHistoryEntity
import com.astrologyvedic.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val profile: ProfileEntity? = null,
    val profiles: List<ProfileEntity> = emptyList(),
    val reportHistory: List<ReportHistoryEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val reportHistoryDao: ReportHistoryDao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadDefaultProfile()
        loadProfiles()
        loadReportHistory()
    }

    private fun loadDefaultProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val profile = profileRepository.getDefaultProfile()
                _uiState.update { it.copy(profile = profile, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    private fun loadProfiles() {
        viewModelScope.launch {
            profileRepository.getAllProfiles().collect { profiles ->
                _uiState.update { it.copy(profiles = profiles) }
            }
        }
    }

    private fun loadReportHistory() {
        viewModelScope.launch {
            reportHistoryDao.getAll().collect { history ->
                _uiState.update { it.copy(reportHistory = history) }
            }
        }
    }

    fun saveProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, saveSuccess = false) }
            val result = if (profile.id == 0L) {
                profileRepository.insertProfile(profile)
            } else {
                profileRepository.updateProfile(profile).map { profile.id }
            }
            result.onSuccess {
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                loadDefaultProfile()
            }.onFailure { e ->
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }

    fun updateProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            profileRepository.updateProfile(profile)
                .onSuccess {
                    _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
                    loadDefaultProfile()
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isSaving = false, error = e.message) }
                }
        }
    }

    fun deleteProfile(profile: ProfileEntity) {
        viewModelScope.launch {
            profileRepository.deleteProfile(profile)
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun setDefaultProfile(profileId: Long) {
        viewModelScope.launch {
            profileRepository.setDefault(profileId)
                .onSuccess { loadDefaultProfile() }
                .onFailure { e ->
                    _uiState.update { it.copy(error = e.message) }
                }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(saveSuccess = false) }
    }
}
