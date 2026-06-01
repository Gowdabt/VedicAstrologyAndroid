package com.astrologyvedic.app.data.api.models

import com.google.gson.annotations.SerializedName

data class ChatRequest(
    @SerializedName("question")
    val question: String,
    @SerializedName("person")
    val person: PersonRequest? = null,
    @SerializedName("chat_history")
    val chatHistory: List<ChatMessage> = emptyList(),
    @SerializedName("output_language")
    val outputLanguage: String = "en"
)

data class ChatMessage(
    @SerializedName("role")
    val role: String,
    @SerializedName("content")
    val content: String
)
