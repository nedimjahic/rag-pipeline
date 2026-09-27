package com.rag.pipeline

import com.rag.pipeline.chat.model.AnswerDTO
import com.rag.pipeline.chat.model.PaymentInstructionDTO
import com.rag.pipeline.chat.model.RetrievedChunkDTO
import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Component

@Component
class ChatService(private val vectorStore: VectorStore, builder: ChatClient.Builder) {
    private val chatClient: ChatClient = builder.defaultSystem("You are a helpful assistant. Answer concisely.").build()

    fun ask(message: String): AnswerDTO {
        val response = chatClient.prompt()
            .advisors(QuestionAnswerAdvisor.builder(vectorStore).build())
            .user(message)
            .call()
            .chatResponse()!!

        val retrieved = response.metadata.get<List<Document>>(QuestionAnswerAdvisor.RETRIEVED_DOCUMENTS) ?: emptyList()
        val sources = retrieved.map {
            RetrievedChunkDTO(it.text ?: "", it.metadata["category"]?.toString() ?: "", it.score ?: 0.0)

        }

        return AnswerDTO(response.result!!.output.text ?: "", sources)
    }

    fun extractPayment(text: String): PaymentInstructionDTO = chatClient.prompt()
        .user {
            it.text("Extract the payment details from: {input}")
                .param("input", text)
        }
        .call()
        .entity(PaymentInstructionDTO::class.java)!!

}