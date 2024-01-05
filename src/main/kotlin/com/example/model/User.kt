package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table

data class User(
    var id: Int,
    val name: String,
)

object Users : IntIdTable() {
    val name = varchar("name", 255)
}
