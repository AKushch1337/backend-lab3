package com.example

import com.akushch.plugins.configureRouting
import com.example.plugins.configureStatusPages
import com.example.plugins.configureValidation
import io.ktor.serialization.gson.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import java.text.DateFormat

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
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

