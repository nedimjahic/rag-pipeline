package com.rag.pipeline.chat.model

data class AnswerDTO(val text: String, val sources: List<RetrievedChunkDTO>)