package com.example.bookingchallenge.screens.newbook

import android.app.Application
import androidx.lifecycle.viewModelScope
import com.example.bookingchallenge.common.Resource
import com.example.bookingchallenge.database.getDatabase
import com.example.bookingchallenge.domain.Booking
import com.example.bookingchallenge.repositories.BookingRepository
import com.example.bookingchallenge.screens.home.BookingsState
import com.example.bookingchallenge.screens.home.HomeViewModel
import com.example.bookingchallenge.utils.calculateNights
import com.example.bookingchallenge.utils.convertDateLongToString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class NewBookViewModel(application: Application) : HomeViewModel(application) {

    private val _newBookingState = MutableStateFlow<NewBookingState?>(null)
    val newBookingState: StateFlow<NewBookingState?>
        get() = _newBookingState

    private val _bookingFinished = MutableStateFlow(false)
    val bookingFinished: StateFlow<Boolean>
        get() = _bookingFinished

    private val _newBookingCheckIn = MutableStateFlow<String?>(null)
    val newBookingCheckIn: StateFlow<String?>
        get() = _newBookingCheckIn

    private val _newBookingCheckOut = MutableStateFlow<String?>(null)
    val newBookingCheckOut: StateFlow<String?>
        get() = _newBookingCheckOut

    private val newBookingFirstNameState = MutableStateFlow<CharSequence?>(null)

    private val newBookingLastNameState = MutableStateFlow<CharSequence?>(null)

    private val _newBookingDepositState = MutableStateFlow(false)
    val newBookingDepositState: StateFlow<Boolean>
        get() = _newBookingDepositState

    private val _newBookingBreakfastState = MutableStateFlow(false)
    val newBookingBreakfastState: StateFlow<Boolean>
        get() = _newBookingBreakfastState

    private val _newBookingLaunchState = MutableStateFlow(false)
    val newBookingLaunchState: StateFlow<Boolean>
        get() = _newBookingLaunchState

    private val _newBookingDinnerState = MutableStateFlow(false)
    val newBookingDinnerState: StateFlow<Boolean>
        get() = _newBookingDinnerState

    private val newBookingNightsState = MutableStateFlow<Int?>(null)

    private val _newBookingPaymentState = MutableStateFlow<Int?>(null)
    val newBookingPaymentState: StateFlow<Int?>
        get() = _newBookingPaymentState

    private val _readyToValidate = MutableStateFlow(false)
    val readyToValidate: StateFlow<Boolean>
        get() = _readyToValidate

    private val _readyToConfirm = MutableStateFlow(false)
    val readyToConfirm: StateFlow<Boolean>
        get() = _readyToConfirm






    fun setDates(checkInLong: Long, checkOutLong: Long) {
        val checkInString = convertDateLongToString(checkInLong, "yyyy-MM-dd")
        val checkOutString = convertDateLongToString(checkOutLong, "yyyy-MM-dd")
        _newBookingCheckIn.value = checkInString
        _newBookingCheckOut.value = checkOutString
        _readyToValidate.value = !newBookingFirstNameState.value.isNullOrBlank() && !newBookingLastNameState.value.isNullOrBlank()
    }

    fun calculatePayments() {
        var addsPricePerNight = 0
        if (newBookingBreakfastState.value) addsPricePerNight += BREAKFAST
        if (newBookingLaunchState.value) addsPricePerNight += LAUNCH
        if (newBookingDinnerState.value) addsPricePerNight += DINNER
        _newBookingPaymentState.value = (ONE_NIGHT * newBookingNightsState.value!!) + (newBookingNightsState.value!!+1)*addsPricePerNight
        _readyToConfirm.value = true
    }

    fun setNights(checkInLong: Long, checkOutLong: Long) { newBookingNightsState.value = calculateNights(checkInLong,checkOutLong) }

    fun onFirstNameStateChanged(text: CharSequence) {
        newBookingFirstNameState.value = text
        _readyToValidate.value = !newBookingFirstNameState.value.isNullOrBlank() && !newBookingLastNameState.value.isNullOrBlank() && !_newBookingCheckIn.value.isNullOrBlank() && !newBookingCheckOut.value.isNullOrBlank()
    }

    fun onLastNameStateChanged(text: CharSequence) {
        newBookingLastNameState.value = text
        _readyToValidate.value = !newBookingFirstNameState.value.isNullOrBlank() && !newBookingLastNameState.value.isNullOrBlank() && !_newBookingCheckIn.value.isNullOrBlank() && !newBookingCheckOut.value.isNullOrBlank()
    }

    fun onDepositStateChanged(checked: Boolean) { _newBookingDepositState.value = checked }
    fun onBreakfastChanged(checked: Boolean) { _newBookingBreakfastState.value = checked }
    fun onLaunchChanged(checked: Boolean) { _newBookingLaunchState.value = checked }
    fun onDinerStateChanged(checked: Boolean) { _newBookingDinnerState.value = checked }

    fun onEditNewBooking () {
        _readyToConfirm.value = false
    }

    fun onBookingFinished() { _bookingFinished.value = false }

    private fun getAdditionalNeeds() : String {
        var additionalNeeds = ""
        if (newBookingBreakfastState.value) additionalNeeds += " Breakfast "
        if (newBookingLaunchState.value) additionalNeeds += " Launch "
        if (newBookingDinnerState.value) additionalNeeds += " Dinner "
        if (additionalNeeds == "") additionalNeeds = "None"
        return additionalNeeds
    }

    private val database = getDatabase(application)
    private val bookingRepository = BookingRepository(database)

    fun addNewBooking() {
        viewModelScope.launch {
            val additionalNeeds = getAdditionalNeeds()
            bookingRepository.addNewBooking(
                newBookingFirstNameState.value.toString(),
                newBookingLastNameState.value.toString(),
                newBookingPaymentState.value!!,
                newBookingDepositState.value,
                newBookingCheckIn.value!!,
                newBookingCheckOut.value!!,
                additionalNeeds
            ).onEach { result ->
                when (result) {
                    is Resource.Loading -> {
                        _newBookingState.value = NewBookingState(isLoading = true)
                    }
                    is Resource.Success -> {
                        _newBookingState.value = NewBookingState(booking = result.data)
                        val bookingList = arrayListOf<Booking>()
                        bookingsState.value.let {

                            if (it != null) bookingList.addAll(it.bookings)
                            bookingList.add(result.data!!)
                            _bookingsState.value = BookingsState(bookings = bookingList, isLoading = false, error = null)
                            _bookingFinished.value = true
                        }
                    }
                    is Resource.Error -> {
                        _newBookingState.value = NewBookingState(error = result.message)
                    }
                }
            }.launchIn(this)
        }
    }

    companion object {
        private const val BREAKFAST = 10
        private const val LAUNCH = 20
        private const val DINNER = 15
        private const val ONE_NIGHT = 50
    }

}

data class NewBookingState(
    val isLoading: Boolean = false,
    val booking: Booking? = null,
    val error: String? = null
)


