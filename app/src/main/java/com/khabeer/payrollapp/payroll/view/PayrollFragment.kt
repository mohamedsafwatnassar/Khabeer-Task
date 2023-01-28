package com.khabeer.payrollapp.payroll.view

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.MPPointF
import com.khabeer.payrollapp.R
import com.khabeer.payrollapp.base.BaseFragment
import com.khabeer.payrollapp.databinding.FragmentPayrollBinding
import com.khabeer.payrollapp.helpers.EventObserver
import com.khabeer.payrollapp.payroll.model.Payroll
import com.khabeer.payrollapp.payroll.viewmodel.PayrollViewModel
import com.khabeer.payrollapp.utils.ViewsManager
import java.util.*

class PayrollFragment : BaseFragment() {

    private lateinit var binding: FragmentPayrollBinding

    private val vm: PayrollViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.getPayroll()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPayrollBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (requireActivity() as ViewsManager).showToolbar()
        (requireActivity() as ViewsManager).setToolbarTitle(getString(R.string.employee_salary))

        // observable to mutable live data in view model
        subscribeData()

        // initialize pie Chart
        pieChartView()
    }

    private fun pieChartView() {
        // on below line we are initializing our
        // variable with their ids.
        val pieChart = binding.pinChartLayout.pieChart

        // on below line we are setting user percent value,
        // setting description as enabled and offset for pie chart
        pieChart.setUsePercentValues(true)
        pieChart.description.isEnabled = false
        pieChart.setExtraOffsets(5f, 10f, 5f, 5f)

        // on below line we are setting drag for our pie chart
        pieChart.dragDecelerationFrictionCoef = 0.95f

        // on below line we are setting hole
        // and hole color for pie chart
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)

        // on below line we are setting circle color and alpha
        pieChart.setTransparentCircleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleAlpha(10)

        // on  below line we are setting hole radius
        pieChart.holeRadius = 3f
        pieChart.transparentCircleRadius = 61f

        // on below line we are setting center text
        pieChart.setDrawCenterText(true)

        // on below line we are setting
        // rotation for our pie chart
        pieChart.rotationAngle = 270f

        // enable rotation of the pieChart by touch
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true

        // on below line we are setting animation for our pie chart
        pieChart.animateY(1400, Easing.EaseInOutQuad)

        // on below line we are disabling our legend for pie chart
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.TRANSPARENT)
        pieChart.setEntryLabelTextSize(12f)

        // on below line we are creating array list and
        // adding data to it to display in pie chart
        val entries: ArrayList<PieEntry> = ArrayList()
        entries.add(PieEntry(40.8f))
        entries.add(PieEntry(59.2f))

        // on below line we are setting pie data set
        val dataSet = PieDataSet(entries, "Mobile OS")

        // on below line we are setting icons.
        dataSet.setDrawIcons(false)

        // on below line we are setting slice for pie
        dataSet.sliceSpace = 3f
        dataSet.iconsOffset = MPPointF(0f, 40f)
        dataSet.selectionShift = 5f

        // add a lot of colors to list
        val colors: ArrayList<Int> = ArrayList()
        colors.add(ContextCompat.getColor(requireContext(), R.color.blue))
        colors.add(ContextCompat.getColor(requireContext(), R.color.yellow))

        // on below line we are setting colors.
        dataSet.colors = colors

        // on below line we are setting pie data set
        val data = PieData(dataSet)
        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(15f)
        data.setValueTypeface(Typeface.DEFAULT_BOLD)
        data.setValueTextColor(Color.WHITE)
        pieChart.data = data

        // undo all highlights
        pieChart.highlightValues(null)

        // loading chart
        pieChart.invalidate()
    }

    private fun subscribeData() {
        lifecycleScope.launchWhenStarted {
            vm.getPayrollState.collect(EventObserver(
                onSuccess = {
                    hideLoadingIndicator()

                    initViews(it.payroll)
                },
                onError = {
                    showSnackBar(it, getString(R.string.try_again)) {
                        vm.getPayroll()
                        snackBar.dismiss()
                    }
                }, onLoading = {
                    hideLoadingIndicator()
                },
                onStopLoading = {
                    hideLoadingIndicator()
                }
            ))
        }
    }

    private fun initViews(payroll: Payroll?) {
        binding.txtUserName.text = payroll?.employee?.get(0)?.eMPDATAEN ?: ""
        binding.txtJobName.text = payroll?.employee?.get(0)?.jOBNAMEAR ?: ""
        binding.txtTotalPrice.text = "Total payroll: -374.20 EGP"

        binding.txtDate.text = payroll?.date

        binding.pinChartLayout.txtEntitlements.text = "773.23 ج الاستحقاقات"
        binding.pinChartLayout.txtDeductions.text = "1120.43 ج الاستقطاعات"
        binding.pinChartLayout.txtTotalPrice.text = "347.20- ج اجمالي الراتب"

        binding.pinChartLayout.txtBasicSalary.text = payroll?.allowences?.get(0)?.cOMPDESCEN ?: ""
        binding.pinChartLayout.txtPriceBasicSalary.text =
            payroll?.allowences?.get(0)?.sALVALUE.toString()

        binding.pinChartLayout.txtNatureWork.text = payroll?.allowences?.get(1)?.cOMPDESCEN ?: ""
        binding.pinChartLayout.txtPriceNatureWork.text =
            payroll?.allowences?.get(1)?.sALVALUE.toString()

        binding.pinChartLayout.txtOtherDeductions.text =
            payroll?.deduction?.get(0)?.cOMPDESCEN ?: ""
        binding.pinChartLayout.txtPriceOtherDeductions.text =
            payroll?.deduction?.get(0)?.sALVALUE.toString()
    }
}
