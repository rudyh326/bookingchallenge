package com.example.bookingchallenge.domain

data class Booking(
    val bookingid : Int,
    val firstname : String,
    val lastname : String,
    val totalprice : Int,
    val depositpaid : Boolean,
    val checkin : String,
    val checkout : String,
    val additionalneeds : String
)
