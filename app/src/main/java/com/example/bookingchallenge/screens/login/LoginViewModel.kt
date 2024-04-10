package com.example.bookingchallenge.screens.login

import android.app.Application
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.bookingchallenge.common.Resource
import com.example.bookingchallenge.repositories.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val _state = MutableStateFlow<AuthState?>(null)

    val state: StateFlow<AuthState?>
        get() = _state

    private val _navigateToHome = MutableLiveData<Boolean>()

    val navigateToHome: LiveData<Boolean>
        get() = _navigateToHome

    private val prefs = application.getSharedPreferences("prefs", MODE_PRIVATE)
    private val sessionRepository = SessionRepository(prefs)


    fun login(username: String, password: String) {
        sessionRepository.login(username, password).onEach { result ->
            when(result) {
                is Resource.Loading -> {
                    _state.value = AuthState(isLoading = true)
                }
                is Resource.Success -> {
                    _state.value = AuthState(token = result.data)
                    _navigateToHome.value = true
                }
                is Resource.Error -> {
                    _state.value = AuthState(error = result.message)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun authenticationComplete() {
        _navigateToHome.value = false
    }

}

data class AuthState(
    val isLoading: Boolean = false,
    val token: String? = null,
    val error: String? = null
)