package com.khabeer.payrollapp.base

import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.khabeer.payrollapp.R
import com.khabeer.payrollapp.utils.ViewsManager

abstract class BaseFragment @JvmOverloads constructor(
    @LayoutRes layoutId: Int = 0
) : Fragment(layoutId) {

    lateinit var snackBar: Snackbar

    protected fun makeToast(text: String) {
        Snackbar.make(requireView(), text, Snackbar.LENGTH_LONG).show()
    }

    protected fun showLoadingIndicator() {
        (requireActivity() as ViewsManager).showProgressBar()
    }

    protected fun hideLoadingIndicator() {
        (requireActivity() as ViewsManager).hideProgressBar()
    }

    fun showSnackBar(
        message: String, posActionName: String,
        onClickListener: View.OnClickListener
    ) {
        snackBar = Snackbar.make(requireView(), message, Snackbar.LENGTH_INDEFINITE)
            .setAction(posActionName, onClickListener)
            .setActionTextColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
        snackBar.show()
    }
}
