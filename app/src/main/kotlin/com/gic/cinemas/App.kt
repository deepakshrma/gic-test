package com.gic.cinemas

import com.gic.cinemas.ConsoleUtil.printMenu
import com.gic.cinemas.service.*


fun main(args: Array<String>) {
    val cols = if (args.size < 3) 10 else args[2].toInt()
    val rows = if (args.size < 2) 8 else args[1].toInt()
    val movieName = if (args.isEmpty()) "Inception" else args[0]
    val bookingSystem = BookingSystem(row = rows, column = cols)
    val taskExecutor = TaskExecutor()
    taskExecutor.registerTask(1, BookTicketTask())
    taskExecutor.registerTask(2, CheckTicketTask())

    do {
        printMenu(movieName, bookingSystem.emptySeats)
        val input = readln().trim().toInt()
        if (input == 3) break
        taskExecutor.executeTask(input, bookingSystem)
    } while (true)
    println("Thank you for using GIC Cinemas System. Bye!")
}

