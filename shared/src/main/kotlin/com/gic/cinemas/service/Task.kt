package com.gic.cinemas.service

import com.gic.cinemas.exceptions.GICException
import com.gic.cinemas.model.parseSeat
import com.gic.cinemas.repositories.TicketRepository
import com.gic.cinemas.repositories.TicketRepository.deleteTicket

interface Task {
    fun run(bookingSystem: BookingSystem)
}

class BookTicketTask : Task {
    override fun run(bookingSystem: BookingSystem) {
        do {
            try {
                println("Please enter number of tickets to book")
                val ticketToBook = readln().trim().toInt()
                var ticket = bookingSystem.reserveSeats(ticketToBook)
                bookingSystem.printSeats(ticket)
                println("Enter blank to accept seat selection, or enter new seat position Example: B03")
                val input1 = readln().trim()
                if (input1 == "") {
                    ticket = bookingSystem.bookIt(ticket.id)
                    bookingSystem.printSeats(ticket)
                    break
                }
                deleteTicket(ticket)
                val seat = parseSeat(input1)
                ticket = bookingSystem.reserveSeats(ticketToBook, bookingSystem.row - seat.row - 1)
                ticket = bookingSystem.bookIt(ticket.id)
                bookingSystem.printSeats(ticket)
                break
            } catch (e: GICException) {
                println(e.message)
            }
        } while (true)
    }
}

class CheckTicketTask : Task {
    override fun run(bookingSystem: BookingSystem) {
        println("Enter booking id, or empty to go back to main menu")
        val ticketId = readln()
        TicketRepository.getTicketById(ticketId)?.let {
            bookingSystem.printSeats(it)
        }
    }
}

class TaskExecutor {
    private val taskMap = mutableMapOf<Int, Task>()

    fun registerTask(option: Int, task: Task) {
        taskMap[option] = task
    }

    fun executeTask(option: Int, bookingSystem: BookingSystem) {
        taskMap[option]?.run(bookingSystem) ?: println("Invalid option!")
    }
}