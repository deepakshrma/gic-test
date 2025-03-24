package cinemas

import com.gic.cinemas.repositories.TicketRepository
import com.gic.cinemas.service.BookingStatus
import com.gic.cinemas.service.BookingSystem
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class BookingSystemTest {
    private lateinit var bookingSystem : BookingSystem

    @BeforeTest
    fun init() {
        bookingSystem = BookingSystem(8, 10)
    }
    @Test
    fun `should able to reserve seats`() {
        val ticket = bookingSystem.reserveSeats(4)
        assertEquals(ticket.id, "GIC0000"+ TicketRepository.tickets.size)
        assertEquals(ticket.status, BookingStatus.RESRV)
        val printableStr = bookingSystem.generatePrintableString(ticket)
        printableStr.also {
            assertContains(it, "reserved 4 seats")
            assertContains(it, "Booking Id: ${ticket.id}")
            assertContains(it, "A  .  .  o  o  o  o  .  .  .  . ")
        }

//        bookingSystem.printSeats(ticket)
//        bookingSystem.bookIt(ticket.id)
//        ticket = bookingSystem.reserveSeats(4)
//        bookingSystem.printSeats(ticket)
//        bookingSystem.bookIt(ticket.id)
//
////        println(bookingSystem.getTicketById("GIC00001"))
//        bookingSystem.printSeats(TicketRepository.getTicketById("GIC00001")!!)
//        ticket = bookingSystem.reserveSeats(5)
//        bookingSystem.printSeats(ticket)
//        bookingSystem.bookIt(ticket.id)
//        ticket = bookingSystem.reserveSeats(10)
//        bookingSystem.printSeats(ticket)
//        bookingSystem.bookIt(ticket.id)
//        bookingSystem.printSeats(TicketRepository.getTicketById("GIC00003")!!)
//        bookingSystem.printSeats(TicketRepository.getTicketById("GIC00004")!!)
//        ticket = bookingSystem.reserveSeats(2)
//        bookingSystem.printSeats(ticket)
//        bookingSystem.bookIt(ticket.id)
//        bookingSystem.printSeats(ticket)

    }
    @Test
    fun `should able to book seats`() {
        val ticket = bookingSystem.reserveSeats(5)
        val ticketId = bookingSystem.bookIt(ticket.id)
        assertEquals(TicketRepository.getTicketById(ticketId.id)?.id, ticketId.id)
    }
}