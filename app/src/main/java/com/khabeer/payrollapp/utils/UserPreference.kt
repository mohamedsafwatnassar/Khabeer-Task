package com.khabeer.payrollapp.utils

import com.orhanobut.hawk.Hawk

object UserPreference {

    fun saveUserToken(token: String) {
        Hawk.put("currentUser", token)
    }

    fun getUserToken(): String? {
        return Hawk.get<String>("currentUser")
    }
}