package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

data class User(
    var id: Int,
    val name: String,
    val defaultCurrency: Currency,
    val currencyCode: String? = null  // Nullable column for user-specific currency
)

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val defaultCurrencyCode = varchar("default_currency_code", 3).nullable()
    val currencyCode = varchar("currency_code", 3).nullable()  // Nullable column for user-specific currency
}
