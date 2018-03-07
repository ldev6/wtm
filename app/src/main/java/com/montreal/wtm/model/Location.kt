package com.montreal.wtm.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
class Location(@PropertyName("name") var name: String, address: String, @PropertyName(
    "metro") var metro: String, @PropertyName("parking") var parking: String, @PropertyName(
    "parking_url") var imageParkingUrl: String) {

  @PropertyName("address")
  var address: String = address
    protected set

}
