package com.khabeer.payrollapp.login.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khabeer.payrollapp.helpers.DataState
import com.khabeer.payrollapp.helpers.Event
import com.khabeer.payrollapp.login.model.LoginBody
import com.khabeer.payrollapp.login.model.ResponseLogin
import com.khabeer.payrollapp.network.ApiManager
import com.khabeer.payrollapp.network.WebServices
import com.khabeer.payrollapp.utils.UserPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val apiManager = ApiManager().getClient()!!.create(WebServices::class.java)

    private val _loginState =
        MutableStateFlow<Event<DataState<ResponseLogin>>>(Event(DataState.Loading()))

    val loginState: MutableStateFlow<Event<DataState<ResponseLogin>>> = _loginState

    // call api login
    fun login(loginBody: LoginBody) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = apiManager.login(loginBody)
                if (response.isSuccessful) {
                    val user = response.body()!!
                    UserPreference.saveUserToken(user.token ?: "")
                    _loginState.emit(Event(DataState.Success(user)))
                } else {
                    _loginState.emit(Event(DataState.Error(response.body()?.englishMessage)))
                }
            } catch (error: Exception) {
                _loginState.emit(Event(DataState.Error(error.localizedMessage)))
            }
        }
    }
}