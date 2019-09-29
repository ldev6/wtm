package com.montreal.wtm

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import android.util.Log
import com.crashlytics.android.Crashlytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.utils.StorageUtils
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import io.fabric.sdk.android.Fabric
//import timber.log.Timber
//import timber.log.Timber.DebugTree
//import timber.log.Timber.Tree


class WTMApplication : Application() {

  
  override fun onCreate() {
    super.onCreate()
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    val remoteConfig = FirebaseRemoteConfig.getInstance()
    val configSettings = FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(BuildConfig.DEBUG)
        .build()
    remoteConfig.setConfigSettings(configSettings)
    remoteConfig.setDefaults(R.xml.remote_config_defaults)
    if(BuildConfig.DEBUG) {
//      Timber.plant(DebugTree())
    }

    //CONFIGURATION
    val twitterKey = resources.getString(R.string.clientKey)
    val twitterSecret = resources.getString(R.string.clientSecret)


    val config = TwitterConfig.Builder(this)
        .logger(DefaultLogger(Log.DEBUG))
        .twitterAuthConfig(TwitterAuthConfig(twitterKey, twitterSecret))
        .debug(true)
        .build()
    Twitter.initialize(config)

    Fabric.with(this, Crashlytics())

    val cacheDir = StorageUtils.getCacheDirectory(applicationContext)

    val defaultOptions = DisplayImageOptions.Builder()
        .cacheInMemory(true)
        .cacheOnDisk(true)
        .build()

    val configImageLoader = ImageLoaderConfiguration.Builder(applicationContext)
        .defaultDisplayImageOptions(defaultOptions)
        .diskCache(UnlimitedDiskCache(cacheDir))
        .build()
    ImageLoader.getInstance().init(configImageLoader)

  }
}