package com.gic.cinemas.model

import com.gic.cinemas.service.BookingStatus
import kotlinx.serialization.Serializable

@Serializable
data class Ticket(val id: String, val seats: MutableSet<Seat> = mutableSetOf(), val status: BookingStatus = BookingStatus.RESRV)
