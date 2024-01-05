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
    }
}


