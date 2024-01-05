package com.example.model


data class Record(
    var id: Int = 0,
    val userId: Int,
    val categoryId: Int,
    var createdAt: String,
    val amount: Double
)
