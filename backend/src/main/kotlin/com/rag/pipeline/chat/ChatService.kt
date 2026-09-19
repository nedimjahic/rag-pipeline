package com.rag.pipeline

import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Component

@Component
class ChatService(builder: ChatClient.Builder) {
    private val chatClient: ChatClient = builder.defaultSystem("You are a helpful assistant. Answer concisely.").build()

    fun ask(message: String): String {
        return chatClient.prompt().user(message).call().content() ?: "No Response"
    }
}