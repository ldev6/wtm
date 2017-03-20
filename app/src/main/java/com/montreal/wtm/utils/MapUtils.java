package com.montreal.wtm.utils;


import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.montreal.wtm.R;
import com.montreal.wtm.WTMApplication;


public class MapUtils {

    private static String BASE_MAP_URL = "https://maps.googleapis.com/maps/api/staticmap";


    public static String urlForLocation(String place) {
        Uri builtUri = Uri.parse(BASE_MAP_URL)
                .buildUpon()
                .appendQueryParameter("center", place)
                .appendQueryParameter("zome", "14")
                .appendQueryParameter("size", "400x400")
                .appendQueryParameter("markers", "color:blue|label: |" + place)
                .appendQueryParameter("key", WTMApplication.applicationContext.getString(R.string.map_api))
                .build();
        return builtUri.toString();
    }
}
