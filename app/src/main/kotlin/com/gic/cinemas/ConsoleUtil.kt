package com.gic.cinemas

object ConsoleUtil {
    fun printMenu(movie: String, seatsAvailable: Int) {
        println(
            """
        Welcome to GIC Cinemas
        [1] Book tickets for $movie ($seatsAvailable seats available)
        [2] Check booking
        [3] Exit
        
        Please enter your selection:
    """.trimIndent()
        )
    }
}