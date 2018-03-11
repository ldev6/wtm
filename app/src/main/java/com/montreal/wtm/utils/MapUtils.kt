package com.montreal.wtm.utils


import android.net.Uri

import com.montreal.wtm.R
import com.montreal.wtm.WTMApplication


object MapUtils {

  private val BASE_MAP_URL = "https://maps.googleapis.com/maps/api/staticmap"


  fun urlForLocation(mapApi:String, place: String): String {
    val builtUri = Uri.parse(BASE_MAP_URL)
        .buildUpon()
        .appendQueryParameter("center", place)
        .appendQueryParameter("zome", "14")
        .appendQueryParameter("size", "400x400")
        .appendQueryParameter("markers", "color:blue|label: |" + place)
        .appendQueryParameter("key", mapApi)
        .build()
    return builtUri.toString()
  }
}
