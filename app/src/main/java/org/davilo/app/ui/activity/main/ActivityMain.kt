package org.davilo.app.ui.activity.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.ui.setupActionBarWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import org.davilo.app.R
import org.davilo.app.databinding.ActivityMainBinding
import org.davilo.app.di.prefrence.AppPreferences
import org.davilo.app.ui.activity.intro.ActivityIntro
import org.davilo.app.ui.base.BaseActivity
import org.davilo.app.utils.setupWithNavController
import javax.inject.Inject

@AndroidEntryPoint
class ActivityMain : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main

    private var currentNavController: LiveData<NavController>? = null

    @Inject
    lateinit var appPreferences: AppPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val token = appPreferences.getString(AppPreferences.Key.Token)

        if (token == null || token.isEmpty()) {
            finish()
            ActivityIntro.navigate(this)
        }

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        }

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }


    private fun setupBottomNavigationBar() {
        val bottomNavigationView = binding.bottomNav
        val navGraphIds = listOf(
            R.navigation.nav_graph_home,
            R.navigation.nav_graph_info,
//            R.navigation.nav_graph_dictionary,
            R.navigation.nav_graph_setting
        )

        // Setup the bottom navigation view with a list of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment,
            intent = intent
        )

        // Whenever the selected controller changes, setup the action bar.
        controller.observe(this, Observer { navController ->
            if (navController.currentDestination?.id == R.id.dictionary_dest) {
                supportActionBar?.hide()
            } else {
                setupActionBarWithNavController(navController)
                supportActionBar?.show()
            }
        })

        currentNavController = controller

    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    fun showLevelsTab() {
        bottom_nav.selectedItemId = R.id.nav_level
    }

    companion object {

        fun navigate(context: Context?) {
            context?.startActivity(Intent(context, ActivityMain::class.java))
        }
    }
}
