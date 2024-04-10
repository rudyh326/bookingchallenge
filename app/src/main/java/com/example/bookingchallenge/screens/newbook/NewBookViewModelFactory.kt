package com.example.bookingchallenge.screens.newbook

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class NewBookViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewBookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NewBookViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}

