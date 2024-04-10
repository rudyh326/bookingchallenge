package com.example.bookingchallenge.screens.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.bookingchallenge.common.Resource
import com.example.bookingchallenge.database.getDatabase
import com.example.bookingchallenge.domain.Booking
import com.example.bookingchallenge.repositories.BookingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

open class HomeViewModel(application: Application) : AndroidViewModel(application) {

    protected val _bookingsState = MutableStateFlow<BookingsState?>(null)
    val bookingsState: StateFlow<BookingsState?>
        get() = _bookingsState

    private val database = getDatabase(application)
    private val bookingRepository = BookingRepository(database)

    val prefs = application.getSharedPreferences("prefs", Context.MODE_PRIVATE)

    init { getBookings() }

    private fun getBookings() {
        bookingRepository.getBookings().onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _bookingsState.value = BookingsState(isLoading = true)
                }
                is Resource.Success -> {
                    _bookingsState.value = BookingsState(bookings = result.data!!)
                }
                is Resource.Error -> {
                    _bookingsState.value = BookingsState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun deleteToken() { prefs.edit().remove("token").apply() }

}

data class BookingsState(
    val isLoading: Boolean = false,
    val bookings: List<Booking> = emptyList(),
    val error: String? = null
)





