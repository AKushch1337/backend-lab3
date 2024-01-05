package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable
import java.time.LocalDateTime


data class Record(
    var id: Int = 0,
    val userId: Int,
    val categoryId: Int,
    var createdAt: String = LocalDateTime.now(),
    val amount: Double,
    val currency: Currency? = null,
    val userCurrencyCode: String? = null  // Nullable column for user-specific currency code
)


object Records : IntIdTable() {
    val userId = integer("userId").references(Users.id)
    val categoryId = integer("categoryId").references(Categories.id)
    val createdAt = varchar("createdAt", 255)
    val amount = double("amount")
    val currencyCode = varchar("currency_code", 3).nullable()  // Nullable column
    val userCurrencyCode = varchar("user_currency_code", 3).nullable()  // Nullable column
}