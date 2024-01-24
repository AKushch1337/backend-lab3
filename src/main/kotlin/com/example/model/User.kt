package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

data class User(
    var id: Int,
    val name: String,
    val defaultCurrencyId: Int?
)

object Users : IntIdTable() {
    val name = varchar("name", 255)
    val defaultCurrencyId = integer("default_currency_id").references(Currencies.id).nullable()
}
