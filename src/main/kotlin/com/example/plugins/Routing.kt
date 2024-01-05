package com.akushch.plugins

import com.example.model.Category
import com.example.model.HealthCheckResponse
import com.example.model.Record
import com.example.model.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicInteger

fun Application.configureRouting() {
    val users = mutableListOf<User>()
    val categories = mutableListOf<Category>()
    val records = mutableListOf<Record>()
    val userIdCounter = AtomicInteger(1)
    val categoryIdCounter = AtomicInteger(1)
    val recordIdCounter = AtomicInteger(1)
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
                    val user = users.find { it.id == userId }
                    if (user != null) {
                        call.respond(user)
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
                    val user = users.find { it.id == userId }
                    if (user != null) {
                        users.remove(user)
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
                newUser.id = userIdCounter.getAndIncrement()
                users.add(newUser)
                call.respond(HttpStatusCode.Created, newUser)
            }

        }

        route("/users") {
            get {
                call.respond(users)
            }
        }
        route("/category") {
            get {
                call.respond(categories)
            }

            post {
                val newCategory = call.receive<Category>()
                newCategory.id = categoryIdCounter.getAndIncrement()
                categories.add(newCategory)
                call.respond(HttpStatusCode.Created, newCategory)
            }

            delete("/{categoryId}") {
                val categoryId = call.parameters["categoryId"]?.toIntOrNull()
                if (categoryId != null) {
                    val category = categories.find { it.id == categoryId }
                    if (category != null) {
                        categories.remove(category)
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
                    userId != null && categoryId != null -> records.filter { it.userId == userId && it.categoryId == categoryId }
                    userId != null -> records.filter { it.userId == userId }
                    categoryId != null -> records.filter { it.categoryId == categoryId }
                    else -> {
                        call.respond(HttpStatusCode.BadRequest, "Either user_id or category_id is required")
                        return@get
                    }
                }

                call.respond(filteredRecords)
            }

            post {
                val newRecord = call.receive<Record>()
                newRecord.id = recordIdCounter.getAndIncrement()
                newRecord.createdAt = LocalDateTime.now().toString()
                records.add(newRecord)
                call.respond(HttpStatusCode.Created, newRecord)
            }

            get("/{recordId}") {
                val recordId = call.parameters["recordId"]?.toIntOrNull()
                if (recordId != null) {
                    val record = records.find { it.id == recordId }
                    if (record != null) {
                        call.respond(record)
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
                    val record = records.find { it.id == recordId }
                    if (record != null) {
                        records.remove(record)
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


