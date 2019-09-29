package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
class Logo : Parcelable {

  @PropertyName("logoUrl")
  lateinit var logoUrl: String

  @PropertyName("name")
  lateinit var name: String

  @PropertyName("url")
  lateinit var urlWebsite: String

  override fun describeContents(): Int {
    return 0
  }

  constructor()

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(this.logoUrl)
    dest.writeString(this.name)
    dest.writeString(this.urlWebsite)
  }

  protected constructor(`in`: Parcel) {
    this.logoUrl = `in`.readString() ?: ""
    this.name = `in`.readString() ?: ""
    this.urlWebsite = `in`.readString() ?: ""
  }

  companion object {

    @JvmField val CREATOR: Parcelable.Creator<Logo> = object : Parcelable.Creator<Logo> {
      override fun createFromParcel(source: Parcel): Logo {
        return Logo(source)
      }

      override fun newArray(size: Int): Array<Logo?> {
        return arrayOfNulls(size)
      }
    }
  }
}
