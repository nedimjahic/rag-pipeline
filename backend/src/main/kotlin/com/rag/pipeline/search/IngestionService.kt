package com.rag.pipeline.search

import org.springframework.ai.reader.tika.TikaDocumentReader
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

@Component
class IngestionService(private val vectorStore: VectorStore) {
    fun ingest(resource: Resource): Int {
        val raw = TikaDocumentReader(resource).read()
        val chunks = TokenTextSplitter.builder().build().apply(raw)
        vectorStore.add(chunks)
        return chunks.size
    }
}