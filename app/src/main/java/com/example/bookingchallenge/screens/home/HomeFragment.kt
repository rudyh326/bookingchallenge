package com.example.bookingchallenge.screens.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bookingchallenge.databinding.FragmentHomeBinding
import com.example.bookingchallenge.utils.hideKeyboard

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this, HomeViewModelFactory(activity.application))[HomeViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.root.setOnClickListener { hideKeyboard(it) }

        binding.bookingList.adapter = BookingListAdapter()

        binding.addButton.setOnClickListener {
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToNewBookFragment())
        }

        binding.logoutButton.setOnClickListener {
            viewModel.deleteToken()
            this.findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToLoginFragment())
        }

        return binding.root
    }

}