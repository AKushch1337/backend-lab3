package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable
import java.time.LocalDateTime
import java.time.LocalDateTime.now


data class Record(
    var id: Int = 0,
    val userId: Int,
    val categoryId: Int,
    var createdAt: String,
    val amount: Double,
    val currencyId: Int?
)


object Records : IntIdTable() {
    val userId = integer("userId").references(Users.id)
    val categoryId = integer("categoryId").references(Categories.id)
    val createdAt = varchar("createdAt", 255).default(now().toString()) // Set default to the current time
    val amount = double("amount")
    val currencyId = integer("currency_id").references(Currencies.id).nullable()
}