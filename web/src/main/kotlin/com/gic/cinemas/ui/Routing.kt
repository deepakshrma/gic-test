package com.gic.cinemas.ui

import com.gic.cinemas.model.parseSeat
import com.gic.cinemas.repositories.TicketRepository
import com.gic.cinemas.service.BookingSystem
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class ReserveRequest(val count: Int, val start: String?)

fun Application.configureRouting(bookingSystem: BookingSystem) {
    routing {
        route("/booking") {
            get("/seats") {
                call.respond(bookingSystem.seats)
            }
            get("/seats/{id}") {
                val id = call.parameters["id"]
                val ticket = id?.let { TicketRepository.getTicketById(id) }
                if (ticket != null) {
                    call.respond(ticket)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Ticket not found")
                }
            }

            post("/reserve") {
                val request = call.receive<ReserveRequest>()
                if (!request.start.isNullOrBlank()) {
                    val seat = parseSeat(request.start)
                    val ticket = bookingSystem.reserveSeats(request.count, bookingSystem.row - seat.row - 1)
                    call.respond(HttpStatusCode.Created, ticket)
                } else {
                    val ticket = bookingSystem.reserveSeats(request.count)
                    call.respond(ticket)
                }
            }


            patch ("/bookit/{id}") {
                val id = call.parameters["id"]!!
                val ticket = bookingSystem.bookIt(id)
                call.respond(ticket)
            }
        }
        staticResources("/", "static")
    }
}
