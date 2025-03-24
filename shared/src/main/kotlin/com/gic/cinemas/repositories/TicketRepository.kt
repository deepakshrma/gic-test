package com.gic.cinemas.repositories

import com.gic.cinemas.model.Ticket
import com.gic.cinemas.service.BookingStatus


// Mocked Database
object TicketRepository {
    var tickets = mutableListOf<Ticket>()
    fun updateTicketStatus(ticket: Ticket, status: BookingStatus): Ticket {
        tickets = tickets.filter { it.id != ticket.id }.toMutableList()
        val updatedTicket = ticket.copy(status = status)
        tickets.add(updatedTicket)
        return updatedTicket
    }

    fun deleteTicket(ticket: Ticket) {
        tickets = tickets.filter { it != ticket }.toMutableList()
    }

    fun getTicketById(id: String): Ticket? {
        return tickets.find { it.id == id }
    }

    fun generateTicketId(): String = "GIC${(tickets.size + 1).toString().padStart(5, '0')}"
}