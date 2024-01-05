package com.akushch.plugins

import com.example.model.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

fun Application.configureRouting() {
    routing {
        get("/healthcheck") {
            val currentTime = LocalDateTime.now().toString()
            val serverStatus = HttpStatusCode.OK
            val healthCheckResponse = HealthCheckResponse(currentTime, serverStatus)
            call.respond(healthCheckResponse)
        }
        route("/user") {
            get("/{userId}") {
                val userId = call.parameters["userId"]?.toIntOrNull()
                if (userId != null) {
                    val user = transaction { Users.select { Users.id eq userId }.singleOrNull() }
                    if (user != null) {
                        call.respond(User(id = user[Users.id].value, name = user[Users.name]))
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid userId format")
                }
            }

            delete("/{userId}") {
                val userId = call.parameters["userId"]?.toIntOrNull()
                if (userId != null) {
                    val deletedRows = transaction { Users.deleteWhere { Users.id eq userId } }
                    if (deletedRows > 0) {
                        call.respond(HttpStatusCode.OK, "User deleted successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "User not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid userId format")
                }
            }

            post {
                val newUser = call.receive<User>()
                val insertedUser = transaction {
                    Users.insertAndGetId {
                        it[name] = newUser.name
                    }
                }
                call.respond(HttpStatusCode.Created, User(id = insertedUser.value, name = newUser.name))
            }

        }

        route("/users") {
            get {
                val allUsers = transaction {
                    Users.selectAll().map { User(id = it[Users.id].value, name = it[Users.name]) }
                }
                call.respond(allUsers)
            }
        }
        route("/category") {
            get {
                val allCategories = transaction {
                    Categories.selectAll().map { Category(id = it[Categories.id].value, name = it[Categories.name]) }
                }
                call.respond(allCategories)
            }

            post {
                val newCategory = call.receive<Category>()
                val insertedCategory = transaction {
                    Categories.insertAndGetId {
                        it[name] = newCategory.name
                    }
                }
                call.respond(HttpStatusCode.Created, Category(id = insertedCategory.value, name = newCategory.name))
            }

            delete("/{categoryId}") {
                val categoryId = call.parameters["categoryId"]?.toIntOrNull()
                if (categoryId != null) {
                    val deletedRows = transaction { Categories.deleteWhere { Categories.id eq categoryId } }
                    if (deletedRows > 0) {
                        call.respond(HttpStatusCode.OK, "Category deleted successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Category not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid categoryId format")
                }
            }
        }
        route("/record") {
            get {
                val userId = call.parameters["user_id"]?.toIntOrNull()
                val categoryId = call.parameters["category_id"]?.toIntOrNull()

                val filteredRecords = when {
                    userId != null && categoryId != null -> transaction {
                        Records.select { (Records.userId eq userId) and (Records.categoryId eq categoryId) }
                            .map { Record(id = it[Records.id].value, userId = it[Records.userId], categoryId = it[Records.categoryId], createdAt = it[Records.createdAt], amount = it[Records.amount]) }
                    }
                    userId != null -> transaction {
                        Records.select { Records.userId eq userId }
                            .map { Record(id = it[Records.id].value, userId = it[Records.userId], categoryId = it[Records.categoryId], createdAt = it[Records.createdAt], amount = it[Records.amount]) }
                    }
                    categoryId != null -> transaction {
                        Records.select { Records.categoryId eq categoryId }
                            .map { Record(id = it[Records.id].value, userId = it[Records.userId], categoryId = it[Records.categoryId], createdAt = it[Records.createdAt], amount = it[Records.amount]) }
                    }
                    else -> {
                        call.respond(HttpStatusCode.BadRequest, "Either user_id or category_id is required")
                        return@get
                    }
                }

                call.respond(filteredRecords)
            }

            post {
                val newRecord = call.receive<Record>()
                val insertedRecord = transaction {
                    Records.insertAndGetId {
                        it[userId] = newRecord.userId
                        it[categoryId] = newRecord.categoryId
                        it[createdAt] = newRecord.createdAt.toString()
                        it[amount] = newRecord.amount
                    }
                }
                call.respond(HttpStatusCode.Created, Record(id = insertedRecord.value, userId = newRecord.userId, categoryId = newRecord.categoryId, createdAt = newRecord.createdAt, amount = newRecord.amount))
            }

            get("/{recordId}") {
                val recordId = call.parameters["recordId"]?.toIntOrNull()
                if (recordId != null) {
                    val record = transaction { Records.select { Records.id eq recordId }.singleOrNull() }
                    if (record != null) {
                        call.respond(
                            Record(
                                id = record[Records.id].value,
                                userId = record[Records.userId],
                                categoryId = record[Records.categoryId],
                                createdAt = record[Records.createdAt].toString(),
                                amount = record[Records.amount]
                            )
                        )
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Record not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid recordId format")
                }
            }
            delete("/{recordId}") {
                val recordId = call.parameters["recordId"]?.toIntOrNull()
                if (recordId != null) {
                    val deletedRows = transaction { Records.deleteWhere { Records.id eq recordId } }
                    if (deletedRows > 0) {
                        call.respond(HttpStatusCode.OK, "Record deleted successfully")
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Record not found")
                    }
                } else {
                    call.respond(HttpStatusCode.BadRequest, "Invalid recordId format")
                }
            }
        }
    }
}


