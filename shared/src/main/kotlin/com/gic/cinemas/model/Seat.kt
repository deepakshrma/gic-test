package com.gic.cinemas.model

import kotlinx.serialization.Serializable

@Serializable
data class Seat(val row: Int, val column: Int)

fun parseSeat(s: String): Seat = Seat(s[0].code - 'A'.code, s.substring(1).toInt())
