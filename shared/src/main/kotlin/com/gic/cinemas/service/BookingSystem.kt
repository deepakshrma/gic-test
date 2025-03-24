package com.gic.cinemas.service

import com.gic.cinemas.repositories.TicketRepository.getTicketById
import com.gic.cinemas.repositories.TicketRepository.generateTicketId
import com.gic.cinemas.repositories.TicketRepository.tickets
import com.gic.cinemas.repositories.TicketRepository.updateTicketStatus
import com.gic.cinemas.exceptions.GICException
import com.gic.cinemas.model.Seat
import com.gic.cinemas.model.Ticket
import java.util.*
import kotlin.jvm.Throws

enum class BookingStatus(val symbol: String) {
    EMPTY(" . "),
    RESRV(" o "),
    BOOKD(" # ")
}

class BookingSystem(val row: Int, val column: Int) {
    var emptySeats = row * column
        private set
    var seats = Array(row) { Array(column) { BookingStatus.EMPTY } }
        private set
    fun bookIt(ticketId: String): Ticket {
        val ticket = getTicketById(ticketId) ?: throw Exception("Ticket not found with id $ticketId")
        ticket.seats.forEach {
            seats[it.row][it.column] = BookingStatus.BOOKD
        }
        emptySeats -= ticket.seats.size
        return updateTicketStatus(ticket, BookingStatus.BOOKD)
    }


    //TODO: improve algorithm
    @Throws(RuntimeException::class)
    fun reserveSeats(count: Int, r: Int = row - 1, c: Int = (column - 1) / 2): Ticket {
        if (r < 0 || r > row || c < 0 || c > row) {
            throw GICException("Sorry, Invalid seat selection [$r, $c]")
        }
        if (count > emptySeats) {
            throw GICException("Sorry, There are only $emptySeats available.")
        }
        val visited = mutableSetOf<Seat>()
        val ticket = Ticket(generateTicketId(), mutableSetOf(), BookingStatus.RESRV)

        // Use breath first search with left and right greediness
        fun bfsWithPriority(r: Int, c: Int) {
            val q = LinkedList<Seat>()
            val s = Seat(r, c)

            // Left and right greedy, if center is booked.
            if (seats[s.row][s.column] != BookingStatus.EMPTY) {
                var i = s.column - 1
                var j = s.column + 1
                do {
                    if (seats[s.row][i] == BookingStatus.EMPTY) {
                        q.add(Seat(s.row, i))
                        break
                    }
                    if (seats[s.row][j] == BookingStatus.EMPTY) {
                        q.add(Seat(s.row, j))
                        break
                    }
                    i--
                    j++
                } while (i >= 0 && j < row)
                if (q.isEmpty() && r > 0) {
                    bfsWithPriority(r - 1, c)
                }
            } else {
                q.add(s)
            }

            while (q.isNotEmpty() && ticket.seats.size != count) {
                val seat = q.poll()
                if (visited.contains(seat)) {
                    continue
                }
                visited.add(seat)
                if (seats[seat.row][seat.column] == BookingStatus.EMPTY && !ticket.seats.contains(
                        Seat(
                            seat.row,
                            seat.column
                        )
                    )
                ) {
                    ticket.seats.add(Seat(seat.row, seat.column))
                }

                if (seat.column > 0 && seats[seat.row][seat.column - 1] == BookingStatus.EMPTY) {
                    q.add(Seat(seat.row, seat.column - 1))
                }
                if (seat.column < column - 1 && seats[seat.row][seat.column + 1] == BookingStatus.EMPTY) {
                    q.add(Seat(seat.row, seat.column + 1))
                }
                if (seat.column == 0 || seat.column == column - 1) {
                    q.add(Seat(seat.row - 1, (column - 1) / 2))
                }
            }
        }
        bfsWithPriority(r, c)
        if (ticket.seats.size != count) {
            throw GICException("Sorry, Unable to reserve seats")
        }
        tickets.add(ticket)
        return ticket
    }

    fun generatePrintableString(ticket: Ticket): String {
        val buffer = StringBuffer()
        if (ticket.status == BookingStatus.RESRV) {
            buffer.append("Successfully, reserved ${ticket.seats.size} seats\n")
        }
        buffer.append(
            """
            Booking Id: ${ticket.id}
            Selected seats:
            ${"SCREEN".padStart(column / 2 * 3 + 3)}
            ${Array(column * 3) { "-" }.joinToString("")}
        """.trimIndent() + "\n"
        )
        val seatMap = ticket.seats.associateWith { true }
        for (i in seats.indices) {
            val rowStr = seats[i].mapIndexed { j, it ->
                if (seatMap.contains(Seat(i, j))) BookingStatus.RESRV.symbol else it.symbol
            }.joinToString("")
            buffer.append("%s %s\n".format(Char('A'.code + row - i - 1), rowStr))
        }
        buffer.append("${Array(column + 1) { it }.joinToString("  ")}\n")
        return buffer.toString()
    }

    fun printSeats(ticket: Ticket) = println(generatePrintableString(ticket))

}
