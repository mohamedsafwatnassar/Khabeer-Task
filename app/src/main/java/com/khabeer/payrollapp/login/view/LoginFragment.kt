package com.khabeer.payrollapp.login.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.khabeer.payrollapp.R
import com.khabeer.payrollapp.base.BaseFragment
import com.khabeer.payrollapp.databinding.FragmentLoginBinding
import com.khabeer.payrollapp.helpers.EventObserver
import com.khabeer.payrollapp.login.model.LoginBody
import com.khabeer.payrollapp.login.viewmodel.AuthViewModel
import com.khabeer.payrollapp.utils.ViewsManager

class LoginFragment : BaseFragment() {

    private lateinit var binding: FragmentLoginBinding

    private val vm: AuthViewModel by viewModels()

    private lateinit var phoneNumber: String
    private lateinit var password: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as ViewsManager).hideToolbar()

        // buttons listener
        btnListener()

        // observable to mutable live data in view model
        subscribeData()
    }

    private fun btnListener() {
        binding.btnLogin.setOnClickListener {
            if (validate()) {
                val loginBody = LoginBody(
                    MobileNumber = phoneNumber,
                    password = password
                )
                vm.login(loginBody)
            }
        }
    }

    private fun subscribeData() {
        lifecycleScope.launchWhenStarted {
            vm.loginState.collect(EventObserver(
                onSuccess = {
                    hideLoadingIndicator()
                    makeToast(it.englishMessage.toString())
                    findNavController().navigate(R.id.action_LoginFragment_to_PayrollFragment)
                },
                onError = {
                    showSnackBar(it, getString(R.string.try_again)) {
                        if (validate()) {
                            val loginBody = LoginBody(
                                MobileNumber = phoneNumber,
                                password = password
                            )
                            vm.login(loginBody)
                        }
                        snackBar.dismiss()
                    }
                }, onLoading = {
                    hideLoadingIndicator()
                },
                onStopLoading = {
                    hideLoadingIndicator()
                },
            ))
        }
    }

    // validation on edit text
    private fun validate(): Boolean {
        var isValid = true

        phoneNumber = binding.edtPhone.text.toString().trim()
        password = binding.edtPassword.text.toString().trim()

        if (phoneNumber.isEmpty()) {
            binding.edtPhone.error = getString(R.string.required)
            isValid = false
        } else if (phoneNumber.length < 11) {
            binding.edtPhone.error = getString(R.string.valid_phone)
            isValid = false
        }
        if (password.isEmpty()) {
            binding.edtPassword.error = getString(R.string.required)
            isValid = false
        } else if (password.length < 6) {
            binding.edtPassword.error = getString(R.string.valid_password)
            isValid = false
        }
        return isValid
    }

}