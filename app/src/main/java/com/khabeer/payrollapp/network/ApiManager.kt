package com.khabeer.payrollapp.network

import com.google.gson.GsonBuilder
import com.khabeer.payrollapp.BuildConfig
import com.khabeer.payrollapp.utils.UserPreference
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiManager {

    private var retrofit: Retrofit? = null

    private var client = OkHttpClient.Builder().addInterceptor { chain ->
        val newRequest: Request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${UserPreference.getUserToken()}")
            .build()
        chain.proceed(newRequest)
    }.build()

    fun getClient(): Retrofit? {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        if (retrofit == null) {
            retrofit =
                Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .baseUrl(BuildConfig.BASE_URL)
                    .client(client)
                    .build()
        }
        return retrofit
    }
}