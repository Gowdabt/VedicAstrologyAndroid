package com.astrologyvedic.app.data.repository

import com.astrologyvedic.app.data.api.models.ChatMessage
import com.astrologyvedic.app.data.api.models.ChatRequest
import com.astrologyvedic.app.data.api.models.PersonRequest
import com.astrologyvedic.app.data.local.dao.ChatDao
import com.astrologyvedic.app.data.local.entities.ChatMessageEntity
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow

class ChatRepository(
    private val chatDao: ChatDao,
    private val astrologyRepository: AstrologyRepository
) {

    fun getMessages(profileId: Long): Flow<List<ChatMessageEntity>> =
        chatDao.getMessages(profileId)

    fun getAllMessages(): Flow<List<ChatMessageEntity>> =
        chatDao.getAllMessages()

    suspend fun sendMessage(
        question: String,
        person: PersonRequest?,
        chatHistory: List<ChatMessage>,
        profileId: Long?,
        outputLanguage: String = "en"
    ): Result<JsonObject> {
        return try {
            // Save user message locally
            chatDao.insert(
                ChatMessageEntity(
                    message = question,
                    isUser = true,
                    profileId = profileId
                )
            )

            // Call API
            val request = ChatRequest(
                question = question,
                person = person,
                chatHistory = chatHistory,
                outputLanguage = outputLanguage
            )
            val result = astrologyRepository.getAiChat(request)

            // Save AI response locally
            result.onSuccess { response ->
                val aiMessage = response.get("analysis")?.asJsonObject
                    ?.get("response")?.asString ?: response.toString()
                chatDao.insert(
                    ChatMessageEntity(
                        message = aiMessage,
                        isUser = false,
                        profileId = profileId
                    )
                )
            }

            result
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearHistory(profileId: Long): Result<Unit> {
        return try {
            chatDao.clearHistory(profileId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun clearAllHistory(): Result<Unit> {
        return try {
            chatDao.clearAllHistory()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
