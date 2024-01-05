package com.example.model


data class Record(
    var id: Int = 0,
    val userId: Int,
    val categoryId: Int,
    val createdAt: String,
    val amount: Double
)
