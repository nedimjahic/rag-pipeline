package com.rag.pipeline.chat

import com.rag.pipeline.ChatService
import com.rag.pipeline.chat.model.AnswerDTO
import com.rag.pipeline.chat.model.ChatRequestDTO
import com.rag.pipeline.chat.model.ExtractRequestDTO
import com.rag.pipeline.chat.model.PaymentInstructionDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/chat")
class ChatController(private val chatService: ChatService) {
    @PostMapping("/ask")
    fun chat(@RequestBody req: ChatRequestDTO): AnswerDTO = chatService.ask(req.text)

    @PostMapping("/extract")
    fun extract(@RequestBody req: ExtractRequestDTO): PaymentInstructionDTO = chatService.extractPayment(req.text)
}