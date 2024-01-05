package com.example

import com.akushch.plugins.configureRouting
import com.example.dao.DatabaseConfig
import com.example.model.Category
import com.example.model.Record
import com.example.model.User
import com.example.plugins.configureStatusPages
import com.example.plugins.configureValidation
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.requestvalidation.*
import java.text.DateFormat

fun main() {
    embeddedServer(Netty, port = 5353, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseConfig.init()
    install(ContentNegotiation) {
        gson {
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }
    configureRouting()
    configureValidation()
    configureStatusPages()
}

fun RequestValidationConfig.userValidation() {
    validate<User> { user ->
        if (user.name.isBlank()) {
            ValidationResult.Invalid("User name must not be empty")
        } else {
            ValidationResult.Valid
        }
    }
}

fun RequestValidationConfig.categoryValidation() {
    validate<Category> { category ->
        if (category.name.isBlank()) {
            ValidationResult.Invalid("Category name must not be empty")
        } else {
            ValidationResult.Valid
        }
    }
}

fun RequestValidationConfig.recordValidation() {
    validate<Record> { record ->
        if (record.userId <= 0) {
            ValidationResult.Invalid("Invalid user ID")
        } else if (record.categoryId <= 0) {
            ValidationResult.Invalid("Invalid category ID")
        } else if (record.amount <= 0.0) {
            ValidationResult.Invalid("Amount must be positive value")
        } else {
            ValidationResult.Valid
        }
    }
}

