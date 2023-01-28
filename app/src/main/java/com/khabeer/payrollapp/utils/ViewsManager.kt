package com.khabeer.payrollapp.utils

interface ViewsManager {

    fun showProgressBar()
    fun hideProgressBar()
    fun hideToolbar()
    fun showToolbar()
    fun setToolbarTitle(title:String)
}