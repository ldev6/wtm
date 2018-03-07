package com.montreal.wtm.model


import android.os.Parcel
import android.os.Parcelable

import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
class Speaker : Parcelable {

  @PropertyName("name")
  lateinit var name: String

  @PropertyName("title")
  lateinit var title: String

  @PropertyName("shortBio")
  lateinit var shortBio: String

  @PropertyName("bio")
  lateinit var bio: String

  @PropertyName("photoUrl")
  lateinit var photoUrl: String


  constructor() {}

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(this.name)
    dest.writeString(this.title)
    dest.writeString(this.shortBio)
    dest.writeString(this.bio)
    dest.writeString(this.photoUrl)
  }

  protected constructor(`in`: Parcel) {
    this.name = `in`.readString()
    this.title = `in`.readString()
    this.shortBio = `in`.readString()
    this.bio = `in`.readString()
    this.photoUrl = `in`.readString()
  }

  companion object {

    val CREATOR: Parcelable.Creator<Speaker> = object : Parcelable.Creator<Speaker> {
      override fun createFromParcel(source: Parcel): Speaker {
        return Speaker(source)
      }

      override fun newArray(size: Int): Array<Speaker?> {
        return arrayOfNulls(size)
      }
    }
  }
}
