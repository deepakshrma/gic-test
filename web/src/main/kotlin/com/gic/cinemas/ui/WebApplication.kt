package com.gic.cinemas.ui

import com.gic.cinemas.service.BookingSystem
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>) {
    val bookingSystem = BookingSystem(8, 10)
    embeddedServer(Netty, port = 8080) {
        configureSerialization()
        configureRouting(bookingSystem)
    }.start(wait = true)
}

fun Application.configureSerialization() {
    install(CORS) {
        allowHost("0.0.0.0:63342")
        anyHost()
    }
    install(ContentNegotiation) {
        json()
    }
}

