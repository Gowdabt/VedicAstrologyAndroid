package com.astrologyvedic.app.ui.screens.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.astrologyvedic.app.data.api.models.ChatMessage
import com.astrologyvedic.app.data.api.models.PersonRequest
import com.astrologyvedic.app.data.local.entities.ChatMessageEntity
import com.astrologyvedic.app.data.repository.ChatRepository
import com.astrologyvedic.app.data.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ChatUiMessage(
    val id: Long = 0,
    val message: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

data class ChatUiState(
    val messages: List<ChatUiMessage> = emptyList(),
    val isTyping: Boolean = false,
    val inputText: String = "",
    val error: String? = null,
    val hasProfile: Boolean = false
)

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatRepository: ChatRepository,
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private var personRequest: PersonRequest? = null
    private var profileId: Long? = null

    init {
        loadProfile()
        loadMessages()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            val profile = profileRepository.getDefaultProfile()
            if (profile != null) {
                profileId = profile.id
                personRequest = PersonRequest(
                    name = profile.name,
                    dob = profile.dob,
                    time = profile.time,
                    place = profile.place,
                    latitude = profile.latitude,
                    longitude = profile.longitude,
                    timezone = profile.timezone
                )
                _uiState.value = _uiState.value.copy(hasProfile = true)
            }
        }
    }

    private fun loadMessages() {
        viewModelScope.launch {
            val pid = profileId
            val flow = if (pid != null) {
                chatRepository.getMessages(pid)
            } else {
                chatRepository.getAllMessages()
            }
            flow.collect { entities ->
                val messages = entities.map { entity ->
                    ChatUiMessage(
                        id = entity.id,
                        message = entity.message,
                        isUser = entity.isUser,
                        timestamp = entity.timestamp
                    )
                }
                _uiState.value = _uiState.value.copy(messages = messages)
            }
        }
    }

    fun updateInput(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text)
    }

    fun sendMessage(text: String? = null) {
        val message = (text ?: _uiState.value.inputText).trim()
        if (message.isBlank()) return

        _uiState.value = _uiState.value.copy(inputText = "", isTyping = true, error = null)

        viewModelScope.launch {
            val chatHistory = _uiState.value.messages.takeLast(10).map { msg ->
                ChatMessage(
                    role = if (msg.isUser) "user" else "assistant",
                    content = msg.message
                )
            }

            chatRepository.sendMessage(
                question = message,
                person = personRequest,
                chatHistory = chatHistory,
                profileId = profileId
            ).onSuccess {
                _uiState.value = _uiState.value.copy(isTyping = false)
            }.onFailure { e ->
                _uiState.value = _uiState.value.copy(
                    isTyping = false,
                    error = e.message ?: "Failed to get response"
                )
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            val pid = profileId
            if (pid != null) {
                chatRepository.clearHistory(pid)
            } else {
                chatRepository.clearAllHistory()
            }
        }
    }
}
