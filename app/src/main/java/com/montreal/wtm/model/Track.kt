package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
class Track : Parcelable {

  @PropertyName("title")
  lateinit var title: String

  constructor()

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(this.title)
  }

  protected constructor(`in`: Parcel) {
    this.title = `in`.readString()
  }

  companion object {

    val CREATOR: Parcelable.Creator<Track> = object : Parcelable.Creator<Track> {
      override fun createFromParcel(source: Parcel): Track {
        return Track(source)
      }

      override fun newArray(size: Int): Array<Track?> {
        return arrayOfNulls(size)
      }
    }
  }
}
