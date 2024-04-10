package com.example.bookingchallenge.screens.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bookingchallenge.databinding.BookingViewItemBinding
import com.example.bookingchallenge.domain.Booking

class BookingListAdapter : ListAdapter<Booking, BookingListAdapter.BookingViewHolder>(
    DiffCallback
) {

    class BookingViewHolder(private var binding: BookingViewItemBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(booking: Booking) {
            binding.booking = booking
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.bookingid == newItem.bookingid
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        return BookingViewHolder(BookingViewItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        val product = getItem(position)
        holder.bind(product)
    }

}