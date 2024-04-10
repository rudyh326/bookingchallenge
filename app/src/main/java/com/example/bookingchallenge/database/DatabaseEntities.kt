package com.example.bookingchallenge.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class DatabaseBooking constructor(
    @PrimaryKey
    val encryptedBooking : String,
)
