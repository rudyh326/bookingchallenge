package com.example.bookingchallenge.network

import com.example.bookingchallenge.BuildConfig
import com.example.bookingchallenge.utils.moshi
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private val client = OkHttpClient.Builder().addInterceptor(Interceptor { chain ->
    val request = chain.request().newBuilder()
        .addHeader("Accept", "application/json")
        .build()
    chain.proceed(request)
}).build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .client(client)
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BuildConfig.BASE_URL)
    .build()

interface BookingApiService {

    @POST("auth")
    fun login(@Body request: AuthRequest): Deferred<TokenResponse>

    @POST("booking")
    fun addNewBooking(@Body request: NetworkBooking): Deferred<NetworkBookingResponse>

}

object BookingApi {
    val retrofitService : BookingApiService by lazy {
        retrofit.create(BookingApiService::class.java)
    }
}