package com.khabeer.payrollapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.khabeer.payrollapp.databinding.ActivityMainBinding
import com.khabeer.payrollapp.utils.ViewsManager

class MainActivity : AppCompatActivity(), ViewsManager {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var navHostFragment: NavHostFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimary)
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // initialize navigation bottom
        initNavigationComponent()
    }

    private fun initNavigationComponent() {
        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main) as NavHostFragment
        navController = navHostFragment.navController

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.PayrollFragment,
            )
        )

        navController.setGraph(R.navigation.nav_graph, intent.extras)
        setupActionBarWithNavController(navController, appBarConfiguration)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun hideToolbar() {
        binding.toolbar.visibility = View.GONE
    }

    override fun showToolbar() {
        binding.toolbar.visibility = View.VISIBLE
        binding.toolbar.background = ContextCompat.getDrawable(this, R.color.colorPrimary)
    }

    override fun setToolbarTitle(title: String) {
        binding.toolbar.title = title
    }

    override fun showProgressBar() {
        binding.loadingView.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        binding.loadingView.visibility = View.GONE
    }
}