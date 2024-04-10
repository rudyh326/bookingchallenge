package com.example.bookingchallenge.screens.newbook

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.bookingchallenge.R
import com.example.bookingchallenge.databinding.FragmentNewBookBinding
import com.example.bookingchallenge.utils.hideKeyboard
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import kotlinx.coroutines.launch

class NewBookFragment : DialogFragment() {
    private lateinit var binding: FragmentNewBookBinding

    private val viewModel: NewBookViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, NewBookViewModelFactory(activity.application))[NewBookViewModel::class.java]
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NO_TITLE, R.style.CustomDialogTheme)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewBookBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        this.isCancelable = false

        binding.root.setOnClickListener { hideKeyboard(it) }

        binding.datePickerIcon.setOnClickListener {
            datePickerDialog()
        }


        lifecycleScope.launch {
            viewModel.bookingFinished.collect {
                if (it) {
                    dismissBookingDialog()
                    viewModel.onBookingFinished()
                }
            }
        }

        binding.cancelButton.setOnClickListener { dismissBookingDialog() }

        return binding.root
    }

    private fun datePickerDialog() {

        val constraintsBuilder = CalendarConstraints.Builder()
            .setValidator(DateValidatorPointForward.now())
            .build()

        val builder = MaterialDatePicker.Builder.dateRangePicker()
            .setTitleText("Select dates")
            .setCalendarConstraints(constraintsBuilder)
            .setTheme(R.style.ThemeMaterialCalendar)

        val datePicker = builder.build()
        datePicker.isCancelable = false
        datePicker.addOnPositiveButtonClickListener { selection ->
            val startDate = selection.first
            val endDate = selection.second

            viewModel.setNights(startDate,endDate)
            viewModel.setDates(startDate,endDate)

        }

        datePicker.show(childFragmentManager, "DATE_PICKER")
    }

    private fun dismissBookingDialog() { this.dismiss() }

}
