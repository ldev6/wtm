package com.montreal.wtm.ui.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.montreal.wtm.BuildConfig;
import com.montreal.wtm.R;
import com.montreal.wtm.model.DataManager;
import com.montreal.wtm.ui.fragment.InformationFragment;
import com.montreal.wtm.ui.fragment.LocationFragment;
import com.montreal.wtm.ui.fragment.ProgramFragment;
import com.montreal.wtm.ui.fragment.SpeakersFragment;
import com.montreal.wtm.ui.fragment.SponsorsFragment;
import com.montreal.wtm.ui.fragment.TwitterFragment;
import com.montreal.wtm.utils.Utils;
import com.montreal.wtm.utils.ui.fragment.EmptyFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Utils.changeFragment(this, R.id.container, ProgramFragment.newInstance());

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        if (id == R.id.nav_program) {
            fragment = ProgramFragment.newInstance();
            setActionBarName(getString(R.string.program));
        } else if (id == R.id.nav_speakers) {
            fragment = SpeakersFragment.newIntance();
            setActionBarName(getString(R.string.speakers));
        } else if (id == R.id.nav_sponsors) {
            fragment = SponsorsFragment.newInstance();
            setActionBarName(getString(R.string.sponsors));
        } else if (id == R.id.nav_information) {
            fragment = InformationFragment.newInstance();
            setActionBarName(getString(R.string.information));
        } else if (id == R.id.nav_map) {
            fragment = LocationFragment.newInstance();
            setActionBarName(getString(R.string.location));
        } else if (id == R.id.nav_twitter) {
            fragment = TwitterFragment.newInstance();
            setActionBarName(getString(R.string.twitter));
        } else {
            fragment = EmptyFragment.newInstance();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

        Utils.changeFragment(this, R.id.container, fragment);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        DataManager.getInstance().saveToSharePreference();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchRemoteConfig();
    }

    private void setActionBarName(String name) {
        getSupportActionBar().setTitle(name);
    }

    private void fetchRemoteConfig() {

        long cacheExpiration = 3600; // 1 hour in seconds.
        if (FirebaseRemoteConfig.getInstance().getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        FirebaseRemoteConfig.getInstance().fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // After config data is successfully fetched, it must be activated before newly fetched
                            // values are returned.
                            FirebaseRemoteConfig.getInstance().activateFetched();
                        }
                        checkNeedUpdate();
                    }
                });
    }

    private void checkNeedUpdate() {
        int versionCode = BuildConfig.VERSION_CODE;
        int minVersion = Integer.parseInt(FirebaseRemoteConfig.getInstance().getString("min_version"));
        int currentVersion = Integer.parseInt(FirebaseRemoteConfig.getInstance().getString("current_version"));

        if (versionCode < minVersion) {
            Utils.updateTheApplication(this, true);
        } else if (versionCode < currentVersion) {
            Utils.updateTheApplication(this, false);
        }
    }
}
