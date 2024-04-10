package com.example.bookingchallenge.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bookingchallenge.databinding.FragmentLoginBinding
import com.example.bookingchallenge.utils.hideKeyboard

class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(this,LoginViewModelFactory(activity.application))[LoginViewModel::class.java]
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentLoginBinding.inflate(inflater)

        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        binding.root.setOnClickListener { hideKeyboard(it) }

        viewModel.navigateToHome.observe(viewLifecycleOwner){
            if (it) {
                this.findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToHomeFragment())
                viewModel.authenticationComplete()
            }
        }

        return binding.root
    }

}