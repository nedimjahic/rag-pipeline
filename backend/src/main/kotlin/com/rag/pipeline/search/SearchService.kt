package com.rag.pipeline.search

import org.springframework.ai.vectorstore.SearchRequest
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.stereotype.Component

@Component
class SearchService(private val vectorStore: VectorStore) {
    fun search(text: String) = vectorStore.similaritySearch(
        SearchRequest.builder().query(text).topK(5).build()
    ).map { SearchHitDTO(it.text ?: "", it.score ?: 0.0) }
}