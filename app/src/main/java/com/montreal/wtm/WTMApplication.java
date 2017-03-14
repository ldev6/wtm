package com.montreal.wtm;

import android.app.Application;
import android.content.Context;

/**
 * Created by laurencedevillers on 16-07-23.
 */

public class WTMApplication extends Application {

    public static Context applicationContext;
    public WTMApplication() {
        applicationContext = this;
    }
}
