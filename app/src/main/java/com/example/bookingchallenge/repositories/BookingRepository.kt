package com.example.bookingchallenge.repositories

import com.example.bookingchallenge.utils.CryptoManager
import com.example.bookingchallenge.common.Resource
import com.example.bookingchallenge.database.BookingDatabase
import com.example.bookingchallenge.database.DatabaseBooking
import com.example.bookingchallenge.domain.Booking
import com.example.bookingchallenge.network.BookingApi
import com.example.bookingchallenge.network.BookingDates
import com.example.bookingchallenge.network.NetworkBooking
import com.example.bookingchallenge.network.asDomainModel
import com.example.bookingchallenge.utils.moshi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

class BookingRepository(private val database: BookingDatabase) {

    private val adapter = moshi.adapter(Booking::class.java)

    fun addNewBooking(
        firstName: String,
        lastName: String,
        totalPrice: Int,
        depositPaid: Boolean,
        checkIn: String,
        checkOut: String,
        additionalNeeds: String
    ) : Flow<Resource<Booking?>> = flow {
        emit(Resource.Loading())
        try {
            val response = BookingApi.retrofitService.addNewBooking(
                request = NetworkBooking(
                    firstName,
                    lastName,
                    totalPrice,
                    depositPaid,
                    BookingDates(checkIn, checkOut),
                    additionalNeeds
                )
            ).await()
            val booking = response.asDomainModel()
            val json = adapter.toJson(booking)
            val encData = CryptoManager.encrypt(json)
            database.bookingDao.insertBooking(DatabaseBooking(encData))
            emit(Resource.Success(booking))
        }
        catch(e: Exception) {
            when (e) {
                is HttpException,
                is ConnectException,
                is UnknownHostException -> {
                    emit(
                        Resource.Error(
                            message = "Operation Failed, check your internet connection.",
                        )
                    )
                }
                is IOException -> {
                    emit(
                        Resource.Error(
                            message = "Operation Failed, something went wrong!",
                        ))
                }
                else -> {
                    emit(
                        Resource.Error(
                            message = "Operation Failed, try again later!",
                        ))
                }
            }
        }
    }

    fun getBookings() : Flow<Resource<List<Booking>>> = flow {
        emit(Resource.Loading())
        try {
            database.bookingDao.getBookings().collect { encBookings ->
                if (encBookings.isNotEmpty()) {
                    val freshBookings = arrayListOf<Booking>()
                    for (encBooking in encBookings){
                        val decBooking= CryptoManager.decrypt(encBooking.encryptedBooking)
                        val booking = adapter.fromJson(decBooking)
                        if (booking!=null) freshBookings.add(booking)
                    }
                    emit(Resource.Success(freshBookings))
                }
                else emit(Resource.Error(message = "No Bookings Yet !",))
            }
        }

        catch(e: IOException) {
            emit(
                Resource.Error(
                    message = "Loading Bookings Failed, something went wrong!",
                ))
        }
        catch (e: Exception) {
            emit(
                Resource.Error("Loading Bookings Failed, Unknown error!"
                ))
        }
    }
}