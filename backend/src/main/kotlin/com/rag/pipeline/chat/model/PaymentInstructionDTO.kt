package com.rag.pipeline.chat.model

import jakarta.validation.constraints.Pattern

data class PaymentInstructionDTO(
    val amount: Double,
    val currency: String,
    @Pattern(regexp = "^[A-Z]{2}\\d{2}[A-Z0-9]{1,30}$") val iban: String,
    val dueDate: String
)