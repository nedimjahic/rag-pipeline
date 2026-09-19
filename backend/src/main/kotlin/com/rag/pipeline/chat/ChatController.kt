package com.rag.pipeline.chat

import com.rag.pipeline.ChatService
import com.rag.pipeline.chat.model.ChatRequestDTO
import com.rag.pipeline.chat.model.ChatResponseDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class ChatController(private val chatService: ChatService) {
    @PostMapping("/chat")
    fun chat(@RequestBody req: ChatRequestDTO): ChatResponseDTO = ChatResponseDTO(chatService.ask(req.message))
}