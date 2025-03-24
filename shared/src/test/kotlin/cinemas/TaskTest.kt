package cinemas

import com.gic.cinemas.model.Ticket
import com.gic.cinemas.repositories.TicketRepository
import com.gic.cinemas.service.*
import io.mockk.*
import kotlin.test.*

class TaskTest {

    private val mockBookingSystem = mockk<BookingSystem>(relaxed = true)

    @BeforeTest
    fun setup() {
        clearAllMocks()
    }

    @Test
    fun `test BookTicketTask successful booking`() {
        val ticket = Ticket("GIC0001", status = BookingStatus.RESRV)

        every { mockBookingSystem.reserveSeats(1) } returns ticket
        every { mockBookingSystem.bookIt("GIC0001") } returns ticket
        every { mockBookingSystem.printSeats(ticket) } just Runs

        val bookTicketTask = BookTicketTask()

        mockkStatic(::readln)
        every { readln() } returnsMany listOf("1", "")

        bookTicketTask.run(mockBookingSystem)

        verifyOrder {
            mockBookingSystem.reserveSeats(1)
            mockBookingSystem.printSeats(ticket)
            mockBookingSystem.bookIt("GIC0001")
            mockBookingSystem.printSeats(ticket)
        }
    }

    @Test
    fun `test BookTicketTask invalid seat input`() {
        val ticket = Ticket("GIC0002", status = BookingStatus.RESRV)
        every { mockBookingSystem.reserveSeats(any(), any(), any()) } returns ticket
        every { mockBookingSystem.bookIt("GIC0002") } returns ticket

        val bookTicketTask = BookTicketTask()

        mockkStatic(::readln)
        every { readln() } returnsMany listOf("1", "B03", "")

        bookTicketTask.run(mockBookingSystem)
        verifyOrder {
            mockBookingSystem.reserveSeats(1, -1, 0)
            mockBookingSystem.printSeats(ticket)
            mockBookingSystem.reserveSeats(1, -2, 0)
            mockBookingSystem.bookIt("GIC0002")
            mockBookingSystem.printSeats(ticket)
        }
    }

    @Test
    fun `test CheckTicketTask when ticket exists`() {
        val ticket = Ticket("GIC0003", mutableSetOf(), BookingStatus.BOOKD)
        mockkObject(TicketRepository)
        every { TicketRepository.getTicketById("GIC0003") } returns ticket

        val checkTicketTask = CheckTicketTask()

        mockkStatic(::readln)
        every { readln() } returns "GIC0003"

        checkTicketTask.run(mockBookingSystem)

        verify { mockBookingSystem.printSeats(ticket) }
    }

    @Test
    fun `test CheckTicketTask when ticket does not exist`() {
        mockkObject(TicketRepository)
        every { TicketRepository.getTicketById("GIC9999") } returns null

        val checkTicketTask = CheckTicketTask()

        mockkStatic(::readln)
        every { readln() } returns "GIC9999"

        checkTicketTask.run(mockBookingSystem)

        verify(exactly = 0) { mockBookingSystem.printSeats(any()) }
    }

    @Test
    fun `test TaskExecutor execution`() {
        val mockTask = mockk<Task>(relaxed = true)
        val executor = TaskExecutor()
        executor.registerTask(1, mockTask)
        executor.executeTask(1, mockBookingSystem)
        verify { mockTask.run(mockBookingSystem) }
    }
}