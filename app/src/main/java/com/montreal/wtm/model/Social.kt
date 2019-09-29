package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.PropertyName
import java.io.Serializable

class Social : Parcelable, Serializable {

  @PropertyName("icon")
  lateinit var icon: String

  @PropertyName("link")
  lateinit var link: String

  @PropertyName("name")
  lateinit var name: String

  constructor() {}

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(this.name)
    dest.writeString(this.link)
    dest.writeString(this.icon)
  }

  protected constructor(`in`: Parcel) {
    this.name = `in`.readString()  ?: ""
    this.link = `in`.readString() ?: ""
    this.icon = `in`.readString() ?: ""
  }

  companion object {

    @JvmField
    val CREATOR: Parcelable.Creator<Social> = object : Parcelable.Creator<Social> {
      override fun createFromParcel(source: Parcel): Social {
        return Social(source)
      }

      override fun newArray(size: Int): Array<Social?> {
        return arrayOfNulls(size)
      }
    }
  }

}