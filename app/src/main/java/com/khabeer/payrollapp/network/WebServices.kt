package com.khabeer.payrollapp.network

import com.khabeer.payrollapp.login.model.LoginBody
import com.khabeer.payrollapp.login.model.ResponseLogin
import com.khabeer.payrollapp.payroll.model.ResponsePayroll
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WebServices {

    @POST("LogIn")
    suspend fun login(
        @Body body: LoginBody,
    ): Response<ResponseLogin>

    @GET("GetPayroll")
    suspend fun getPayroll(): Response<ResponsePayroll>
}