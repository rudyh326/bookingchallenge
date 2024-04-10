package com.example.bookingchallenge.network

import com.example.bookingchallenge.domain.Booking
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AuthRequest(
    val username : String,
    val password : String
)

@JsonClass(generateAdapter = true)
data class TokenResponse(
    val token : String
)

@JsonClass(generateAdapter = true)
data class NetworkBookingResponse(
    val bookingid : Int,
    val booking: NetworkBooking
)

@JsonClass(generateAdapter = true)
data class NetworkBooking(
    val firstname : String,
    val lastname : String,
    val totalprice : Int,
    val depositpaid : Boolean,
    val bookingdates : BookingDates,
    val additionalneeds : String
)

@JsonClass(generateAdapter = true)
data class BookingDates(
    val checkin : String,
    val checkout : String,
)

fun NetworkBookingResponse.asDomainModel(): Booking {
    return Booking(
        bookingid = this.bookingid,
        firstname = this.booking.firstname,
        lastname = this.booking.lastname,
        totalprice = this.booking.totalprice,
        depositpaid = this.booking.depositpaid,
        checkin = this.booking.bookingdates.checkin,
        checkout = this.booking.bookingdates.checkout,
        additionalneeds = this.booking.additionalneeds
    )
}

