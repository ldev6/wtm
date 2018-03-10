package com.montreal.wtm.ui.activity

import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.app.Fragment
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.montreal.wtm.BuildConfig
import com.montreal.wtm.R
import com.montreal.wtm.R.id
import com.montreal.wtm.R.layout
import com.montreal.wtm.R.string
import com.montreal.wtm.ui.fragment.InformationFragment
import com.montreal.wtm.ui.fragment.LocationFragment
import com.montreal.wtm.ui.fragment.ProgramFragment
import com.montreal.wtm.ui.fragment.SpeakersFragment
import com.montreal.wtm.ui.fragment.SponsorsFragment
import com.montreal.wtm.ui.fragment.TwitterFragment
import com.montreal.wtm.utils.Utils
import com.montreal.wtm.utils.Utils.changeFragment
import com.montreal.wtm.utils.ui.activity.BaseActivity


class MainActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener {

  lateinit var navigationView: NavigationView

  override fun onCreate(savedInstanceState: Bundle?) {

    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)

    val toolbar = findViewById<View>(id.main_toolbar) as Toolbar
    toolbar.setTitleTextColor(Color.WHITE)
    setSupportActionBar(toolbar)

    val drawer = findViewById<View>(id.drawer_layout) as DrawerLayout

    val toggle = ActionBarDrawerToggle(
        this, drawer, toolbar, string.navigation_drawer_open, string.navigation_drawer_close)
    drawer.setDrawerListener(toggle)

    toggle.syncState()
    navigationView = findViewById<View>(id.nav_view) as NavigationView
    navigationView?.setNavigationItemSelectedListener(this)

    setLoginDrawer()
    changeLogin.subscribe({
      setLoginDrawer()
    })

    Utils.changeFragment(this, R.id.container, ProgramFragment.newInstance())
  }


  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    val id = item.itemId
    var fragment: Fragment? = null
    when(id) {
      R.id.nav_program -> {
        fragment = ProgramFragment.newInstance()
        setActionBarName(getString(R.string.program))
      }
      R.id.nav_speakers -> {
        fragment = SpeakersFragment.newIntance()
        setActionBarName(getString(R.string.speakers))
      }
      R.id.nav_sponsors -> {
        fragment = SponsorsFragment.newInstance()
        setActionBarName(getString(R.string.sponsors))
      }
      R.id.nav_information -> {
        fragment = InformationFragment.newInstance()
        setActionBarName(getString(R.string.information))
      }
      R.id.nav_map -> {
        fragment = LocationFragment.newInstance()
        setActionBarName(getString(R.string.location))
      }
      R.id.nav_twitter -> {
        fragment = TwitterFragment.newInstance()
        setActionBarName(getString(R.string.twitter))
      }R.id.nav_login -> {
        fragment = ProgramFragment.newInstance()
        setActionBarName(getString(R.string.program))
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
          signOut()
        } else {
          signIn()
        }
      }
      else -> {
        fragment = ProgramFragment.newInstance()
        setActionBarName(getString(R.string.program))
      }
    }
    val drawer = findViewById<View>(R.id.drawer_layout) as DrawerLayout
    drawer.closeDrawer(GravityCompat.START)

    Utils.changeFragment(this, R.id.container, fragment)
    return true
  }

  fun setLoginDrawer() {
    val navLogin = navigationView.menu.findItem(R.id.nav_login)

    if (FirebaseAuth.getInstance().currentUser == null) {
      navLogin.setTitle(R.string.sign_in)
    } else {
      navLogin.setTitle(R.string.sign_out)
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

  private fun checkNeedUpdate() {
    val versionCode = BuildConfig.VERSION_CODE
    val minVersion = Integer.parseInt(FirebaseRemoteConfig.getInstance().getString("min_version"))
    val currentVersion = Integer.parseInt(
        FirebaseRemoteConfig.getInstance().getString("current_version"))

    if (versionCode < minVersion) {
      Utils.updateTheApplication(this, true)
    } else if (versionCode < currentVersion) {
      Utils.updateTheApplication(this, false)
    }
  }
}

