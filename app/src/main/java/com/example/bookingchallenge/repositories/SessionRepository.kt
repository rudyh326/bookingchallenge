package com.example.bookingchallenge.repositories

import android.content.SharedPreferences
import com.example.bookingchallenge.common.Resource
import com.example.bookingchallenge.network.AuthRequest
import com.example.bookingchallenge.network.BookingApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.UnknownHostException

class SessionRepository(
    private val prefs: SharedPreferences
) {

    fun login(username: String, password: String) : Flow<Resource<String?>> = flow {
        emit(Resource.Loading())
        try {
            val response = BookingApi.retrofitService.login(request = AuthRequest(username,password)).await()
            prefs.edit()
                .putString("token", response.token)
                .apply()
            val token = prefs.getString("token", null)
            emit(Resource.Success(token))
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

}