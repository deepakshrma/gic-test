package cinemas

import com.gic.cinemas.model.Ticket
import com.gic.cinemas.repositories.TicketRepository
import com.gic.cinemas.service.BookingStatus

import kotlin.test.*

class TicketRepositoryTest {

    @BeforeTest
    fun setup() {
        TicketRepository.tickets = mutableListOf()
    }

    @Test
    fun `test Update Ticket Status`() {
        val ticket = Ticket(id = "GIC00001", status = BookingStatus.EMPTY, seats = mutableSetOf())
        TicketRepository.tickets.add(ticket)

        val updatedTicket = TicketRepository.updateTicketStatus(ticket, BookingStatus.BOOKD)

        assertEquals(BookingStatus.BOOKD, updatedTicket.status)
        assertNotNull(TicketRepository.getTicketById(ticket.id))
    }

    @Test
    fun `test Delete Ticket`() {
        val ticket = Ticket(id = "GIC00002", status = BookingStatus.RESRV, seats = mutableSetOf())
        TicketRepository.tickets.add(ticket)

        TicketRepository.deleteTicket(ticket)

        assertNull(TicketRepository.getTicketById(ticket.id))
    }

    @Test
    fun `test Get Ticket By Id`() {
        val ticket = Ticket(id = "GIC00003", status = BookingStatus.RESRV, seats = mutableSetOf())
        TicketRepository.tickets.add(ticket)

        val foundTicket = TicketRepository.getTicketById("GIC00003")

        assertNotNull(foundTicket)
        assertEquals("GIC00003", foundTicket?.id)
    }

    @Test
    fun `testGenerate Ticket Id`() {
        TicketRepository.tickets.add(Ticket(id = "GIC00001", status = BookingStatus.RESRV, seats = mutableSetOf()))
        TicketRepository.tickets.add(Ticket(id = "GIC00002", status = BookingStatus.RESRV, seats = mutableSetOf()))
        val newId = TicketRepository.generateTicketId()

        assertEquals("GIC00003", newId)
    }
}