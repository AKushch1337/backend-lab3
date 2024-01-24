package com.example.model

import com.example.model.Users.nullable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

data class Currency(
    val id: Int,
    val code: String,
    val name: String
)

object Currencies : IntIdTable() {
    val code = varchar("code", 3).uniqueIndex()
    val name = varchar("name", 255)
}
