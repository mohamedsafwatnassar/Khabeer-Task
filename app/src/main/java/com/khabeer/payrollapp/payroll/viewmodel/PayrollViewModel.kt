package com.khabeer.payrollapp.payroll.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khabeer.payrollapp.helpers.DataState
import com.khabeer.payrollapp.helpers.Event
import com.khabeer.payrollapp.login.model.LoginBody
import com.khabeer.payrollapp.login.model.ResponseLogin
import com.khabeer.payrollapp.network.ApiManager
import com.khabeer.payrollapp.network.WebServices
import com.khabeer.payrollapp.payroll.model.ResponsePayroll
import com.khabeer.payrollapp.utils.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject

class PayrollViewModel : ViewModel() {

    private val apiManager = ApiManager().getClient()!!.create(WebServices::class.java)

    private val _getPayrollState =
        MutableStateFlow<Event<DataState<ResponsePayroll>>>(Event(DataState.Loading()))

    val getPayrollState: MutableStateFlow<Event<DataState<ResponsePayroll>>> = _getPayrollState

    // call api to get payroll or user
    fun getPayroll() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiManager.getPayroll()
                Log.d("getPayroll", "response $response")

                if (response.isSuccessful) {
                    val payroll = response.body()!!
                    Log.d("getPayroll", "payroll.payroll?.employee?.get(0)?.eMPDATAAR ${payroll.payroll?.employee?.get(0)?.eMPDATAAR}")

                    _getPayrollState.emit(Event(DataState.Success(payroll)))
                } else {
                    _getPayrollState.emit(Event(DataState.Error("Something went wrong...")))
                }
            } catch (error: Exception) {
                _getPayrollState.emit(Event(DataState.Error(error.localizedMessage)))
            }
        }
    }
}