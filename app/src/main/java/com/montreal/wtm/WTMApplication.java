package com.montreal.wtm;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.File;

import io.fabric.sdk.android.Fabric;


public class WTMApplication extends Application {


    public static Context applicationContext;

    public WTMApplication() {
        applicationContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        remoteConfig.setConfigSettings(configSettings);
        remoteConfig.setDefaults(R.xml.remote_config_defaults);

        //CONFIGURATION
        String twitterKey = getResources().getString(R.string.clientKey);
        String twitterSecret = getResources().getString(R.string.clientSecret);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics(), new TweetComposer());

        File cacheDir = StorageUtils.getCacheDirectory(applicationContext);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(applicationContext)
                .defaultDisplayImageOptions(defaultOptions)
                .diskCache(new UnlimitedDiskCache(cacheDir))
                .build();
        ImageLoader.getInstance().init(config);

    }

}
