package com.example.plugins

import com.example.categoryValidation
import com.example.recordValidation
import com.example.userValidation
import io.ktor.server.application.*
import io.ktor.server.plugins.requestvalidation.*

fun Application.configureValidation() {
    install(RequestValidation) {
        userValidation()
        categoryValidation()
        recordValidation()
    }
}