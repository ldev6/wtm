package com.montreal.wtm.model

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
class Location {

  @PropertyName("name")
  lateinit var name: String

  @PropertyName("address")
  lateinit var address: String

  @PropertyName("metro")
  lateinit var metro: String

  @PropertyName("parking")
  lateinit var parking: String

  @PropertyName("parking_url")
  lateinit var imageParkingUrl: String

  constructor(){}
}
