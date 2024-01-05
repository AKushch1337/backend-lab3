package com.example.model

import org.jetbrains.exposed.dao.id.IntIdTable

data class Category(
    var id: Int = 0,
    val name: String
)

object Categories : IntIdTable() {
    val name = varchar("name", 255)
}