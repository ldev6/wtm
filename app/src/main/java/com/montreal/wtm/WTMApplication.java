package com.montreal.wtm;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;


public class WTMApplication extends Application {


    public static Context applicationContext;

    public WTMApplication() {
        applicationContext = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String twitterKey = getResources().getString(R.string.clientKey);
        String twitterSecret = getResources().getString(R.string.clientSecret);

        TwitterAuthConfig authConfig = new TwitterAuthConfig(twitterKey, twitterSecret);
        Fabric.with(this, new Twitter(authConfig), new Crashlytics(), new TweetComposer());
    }
}
