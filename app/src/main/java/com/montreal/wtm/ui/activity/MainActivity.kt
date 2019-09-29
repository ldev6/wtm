package com.montreal.wtm.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.onNavDestinationSelected
import androidx.navigation.ui.NavigationUI.setupActionBarWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseWrapper
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.montreal.wtm.BuildConfig
import com.montreal.wtm.R
import com.montreal.wtm.ui.fragment.InformationFragment
import com.montreal.wtm.ui.fragment.LocationFragment
import com.montreal.wtm.ui.fragment.PartnersFragment
import com.montreal.wtm.ui.fragment.ProgramFragment
import com.montreal.wtm.ui.fragment.SpeakersFragment
import com.montreal.wtm.ui.fragment.TwitterFragment
import com.montreal.wtm.utils.Utils
import com.montreal.wtm.utils.ui.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_main.drawer_layout

class MainActivity : BaseActivity() {

    private lateinit var navigationView: NavigationView
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById<View>(R.id.main_toolbar) as Toolbar
        toolbar.setTitleTextColor(Color.WHITE)


        setSupportActionBar(toolbar)

        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout

//        val toggle = ActionBarDrawerToggle(
//            this, drawer, toolbar, R.string.navigation_drawer_open,
//            R.string.navigation_drawer_close)
//        drawer.addDrawerListener(toggle)
//
//        toggle.syncState()
        navigationView = findViewById<View>(R.id.nav_view) as NavigationView
//        navigationView.setNavigationItemSelectedListener(this)


        setupNavigation()

        setLoginDrawer()
        loginChanged.subscribe({
            setLoginDrawer()
        })

//        Utils.changeFragment(this, R.id.container, ProgramFragment.newInstance())
    }

    private fun setupNavigation() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.mainNavigationFragment) as NavHostFragment? ?: return
        val navController = host.navController

        NavigationUI.setupWithNavController(findViewById<NavigationView>(R.id.nav_view), navController)
        
        //SET the default entry fragment and setup the actionbar 
        val topLevelDestinations = HashSet<Int>()
        topLevelDestinations.add(R.id.nav_program)
        topLevelDestinations.add(R.id.nav_speakers)
        topLevelDestinations.add(R.id.nav_sponsors)
        topLevelDestinations.add(R.id.nav_information)
        topLevelDestinations.add(R.id.nav_map)
        topLevelDestinations.add(R.id.nav_twitter)
        
        appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
            .setDrawerLayout(drawer_layout).build()
        setupActionBarWithNavController(this, navController, appBarConfiguration)

    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        val navController = findNavController(this, R.id.mainNavigationFragment)
//        return onNavDestinationSelected(item, navController) || super.onOptionsItemSelected(item)
//    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        val retValue = super.onCreateOptionsMenu(menu)
//        val navigationView = findViewById<NavigationView>(R.id.nav_view)
//        if (navigationView == null) {
//            //android needs to know what menu I need
//            menuInflater.inflate(R.menu.activity_main_drawer, menu)
//            return true
//        }
//        return retValue
//    }



//    override fun onNavigationItemSelected(item: MenuItem): Boolean {
//        val id = item.itemId
//        var fragment: Fragment
//        when (id) {
//            R.id.nav_program -> {
//                fragment = ProgramFragment.newInstance()
//                setActionBarName(getString(R.string.program))
//            }
//            R.id.nav_speakers -> {
//                fragment = SpeakersFragment.newIntance()
//                setActionBarName(getString(R.string.speakers))
//            }
//            R.id.nav_sponsors -> {
//                fragment = PartnersFragment.newInstance()
//                setActionBarName(getString(R.string.sponsors))
//            }
//            R.id.nav_information -> {
//                fragment = InformationFragment.newInstance()
//                setActionBarName(getString(R.string.information))
//            }
//            R.id.nav_map -> {
//                fragment = LocationFragment.newInstance()
//                setActionBarName(getString(R.string.location))
//            }
//            R.id.nav_twitter -> {
//                fragment = TwitterFragment.newInstance()
//                setActionBarName(getString(R.string.twitter))
//            }
//            R.id.nav_login -> {
//                fragment = ProgramFragment.newInstance()
//                setActionBarName(getString(R.string.program))
//                if (FirebaseWrapper.isLogged()) {
//                    signOut()
//                } else {
//                    signIn()
//                }
//            }
//            else -> {
//                fragment = ProgramFragment.newInstance()
//                setActionBarName(getString(R.string.program))
//            }
//        }
//        val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
//        drawer.closeDrawer(GravityCompat.START)
//
//        return true
//    }

    fun setLoginDrawer() {
        val navLogin = navigationView.menu.findItem(R.id.nav_login)

        if (FirebaseWrapper.isLogged()) {
            navLogin.setTitle(R.string.sign_out)
        } else {
            navLogin.setTitle(R.string.sign_in)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchRemoteConfig()
    }

    private fun setActionBarName(name: CharSequence) {
        supportActionBar!!.title = name
    }

    private fun fetchRemoteConfig() {

        var cacheExpiration: Long = 3600 // 1 hour in seconds.
        if (FirebaseRemoteConfig.getInstance().info.configSettings.isDeveloperModeEnabled) {
            cacheExpiration = 0
        }

        FirebaseRemoteConfig.getInstance().fetch(cacheExpiration)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful()) {
                    // After config data is successfully fetched, it must be activated before newly fetched
                    // values are returned.
                    FirebaseRemoteConfig.getInstance().activateFetched()
                }
                checkNeedUpdate()
            }
    }

    override fun onSupportNavigateUp(): Boolean {
        // Allows NavigationUI to support proper up navigation or the drawer layout
        // drawer menu, depending on the situation
        return NavigationUI.navigateUp(findNavController(this, R.id.mainNavigationFragment), appBarConfiguration)
    }

    private fun checkNeedUpdate() {
        val versionCode = BuildConfig.VERSION_CODE
        val minVersion = FirebaseWrapper.getMinVersion()
        val currentVersion = FirebaseWrapper.getRemoteVersion()

        if (versionCode < minVersion) {
            Utils.updateTheApplication(this, true)
        } else if (versionCode < currentVersion) {
            Utils.updateTheApplication(this, false)
        }
    }
}

