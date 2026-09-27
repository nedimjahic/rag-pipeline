package com.rag.pipeline.search

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/ingest")
class IngestionController(private val ingestionService: IngestionService) {
    @PostMapping
    fun ingest(@RequestParam file: MultipartFile): Map<String, Any> {
        val count = ingestionService.ingest(file.resource)
        return mapOf("chunks" to count)
    }
}