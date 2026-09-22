package com.rag.pipeline.util

import org.slf4j.LoggerFactory
import org.springframework.ai.document.Document
import org.springframework.ai.vectorstore.VectorStore
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Component

private const val SEED_DATA_FILE = "seed-data.csv"

@Component
class DataSeeder(
    private val vectorStore: VectorStore,
    private val jdbcTemplate: JdbcTemplate
) {
    @EventListener(ApplicationReadyEvent::class)
    fun run() {
        val count = jdbcTemplate.queryForObject(
            "SELECT count(*) FROM vector_store", Int::class.java
        ) ?: 0
        if (count > 0) {
            log.info("Vector store already has {} rows, skipping seed.", count)
            return
        }

        val docs = ClassPathResource(SEED_DATA_FILE).inputStream
            .bufferedReader()
            .useLines { lines ->
                lines.drop(1) // skip header
                    .filter { it.isNotBlank() }
                    .map { line ->
                        val (category, text) = line.split(",", limit = 2)
                        Document(text, mapOf("category" to category))
                    }
                    .toList()
            }

        vectorStore.add(docs)                              // embeds + stores
        log.info("Seeded {} documents into the vector store.", docs.size)

    }

    companion object {
        private val log = LoggerFactory.getLogger(DataSeeder::class.java)
    }
}