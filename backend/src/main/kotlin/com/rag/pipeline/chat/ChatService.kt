package com.rag.pipeline

import com.rag.pipeline.chat.model.PaymentInstructionDTO
import org.springframework.ai.chat.client.ChatClient
import org.springframework.stereotype.Component

@Component
class ChatService(builder: ChatClient.Builder) {
    private val chatClient: ChatClient = builder.defaultSystem("You are a helpful assistant. Answer concisely.").build()

    fun ask(message: String): String = chatClient.prompt().user(message).call().content() ?: "No Response"

    fun extractPayment(text: String): PaymentInstructionDTO = chatClient.prompt()
        .user {
            it.text("Extract the payment details from: {input}")
                .param("input", text)
        }
        .call()
        .entity(PaymentInstructionDTO::class.java)!!

}