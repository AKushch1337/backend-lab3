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
    }
}


