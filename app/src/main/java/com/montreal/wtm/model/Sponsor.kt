package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName

@IgnoreExtraProperties
class Sponsor : Parcelable {

  @PropertyName("logoUrl")
  var logoUrl: String

  @PropertyName("name")
  var name: String

  @PropertyName("url")
  var urlWebsite: String

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(this.logoUrl)
    dest.writeString(this.name)
    dest.writeString(this.urlWebsite)
  }

  protected constructor(`in`: Parcel) {
    this.logoUrl = `in`.readString()
    this.name = `in`.readString()
    this.urlWebsite = `in`.readString()
  }

  companion object {

    val CREATOR: Parcelable.Creator<Sponsor> = object : Parcelable.Creator<Sponsor> {
      override fun createFromParcel(source: Parcel): Sponsor {
        return Sponsor(source)
      }

      override fun newArray(size: Int): Array<Sponsor?> {
        return arrayOfNulls(size)
      }
    }
  }
}
