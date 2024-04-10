package com.example.bookingchallenge

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingchallenge.domain.Booking
import com.example.bookingchallenge.screens.home.BookingListAdapter
import com.example.bookingchallenge.screens.login.AuthState
import com.google.android.material.progressindicator.CircularProgressIndicator

@BindingAdapter("authStatus")
fun bindAuthStatus(textView: TextView, state: AuthState?) {
    if (state != null) {
        if (state.isLoading) {
            textView.setTextColor(Color.DKGRAY)
            textView.text = textView.context.getString(R.string.pending)
        }
        if (!state.error.isNullOrBlank()) {
            textView.setTextColor(Color.RED)
            textView.text = state.error
        }
    }
}

@BindingAdapter("bookingsStateError")
fun bindBookingsStateError(textView: TextView, state: String?) {
    if (!state.isNullOrBlank()) {
        textView.text = state
        textView.visibility = View.VISIBLE
    }
    else {
        textView.text = null
        textView.visibility = View.GONE
    }
}

@BindingAdapter("bookingsStateLoading")
fun bindBookingsStateLoading(imageView: CircularProgressIndicator, state: Boolean?) {
    if (state != null) {
        if (state) {
            imageView.visibility = View.VISIBLE
        }
        else imageView.visibility = View.GONE
    }
}

@BindingAdapter("bookingListData")
fun bindBookingRecyclerView(recyclerView: RecyclerView, data: List<Booking>?) {
    val adapter = recyclerView.adapter as BookingListAdapter
    adapter.submitList(data)
}

@BindingAdapter("bookingID")
fun bindBookingID(textView: TextView, bookingID: Int?) {
    textView.text = bookingID?.toString() ?: "N/A"
}

@BindingAdapter("bookingFirstName")
fun bindBookingFirstName(textView: TextView, firstName: String?) {
    textView.text = firstName ?: "N/A"
}

@BindingAdapter("bookingLastName")
fun bindBookingLastName(textView: TextView, lastName: String?) {
    textView.text = lastName ?: "N/A"
}

@BindingAdapter("bookingTotalPrice")
fun bindBookingTotalPrice(textView: TextView, totalPrice: Int?) {
    textView.text = totalPrice?.toString() ?: "N/A"
}

@BindingAdapter("bookingDepositPaid")
fun bindBookingDepositPaid(textView: TextView, depositPaid: Boolean?) {
    textView.text = depositPaid?.toString() ?: "N/A"
}

@BindingAdapter("bookingCheckIn")
fun bindBookingCheckIn(textView: TextView, checkIn: String?) {
    textView.text = checkIn ?: "N/A"
}

@BindingAdapter("bookingCheckOut")
fun bindBookingCheckOut(textView: TextView, checkOut: String?) {
    textView.text = checkOut ?: "N/A"
}

@BindingAdapter("bookingAdditionalNeeds")
fun bindBookingAdditionalNeeds(textView: TextView, additionalNeeds: String?) {
    textView.text = additionalNeeds ?: "N/A"
}

@BindingAdapter("newBookingDate")
fun bindNewBookingCheckDate(textView: TextView, date: String?) {
    if (!date.isNullOrEmpty()){
        textView.text = date
    } else textView.hint = "yyyy-MM-dd"
}

@SuppressLint("SetTextI18n")
@BindingAdapter("newBookingTotalPayment")
fun bindNewBookingTotalPayment(textView: TextView, payment: Int?) {
    if (payment!=null) textView.text = "$payment $"
    else textView.hint = "($)"
}

@BindingAdapter("newEditButton")
fun bindNewEditButton(imageButton: ImageButton, toShow: Boolean?) {
    if (toShow == true) imageButton.visibility = View.VISIBLE
    else imageButton.visibility = View.INVISIBLE
}



