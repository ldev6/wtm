package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import java.util.ArrayList

@IgnoreExtraProperties
class Partner : Parcelable {

  @PropertyName("logos")
  lateinit var logos: ArrayList<Logo>

  @PropertyName("title")
  lateinit var title: String

  constructor()

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeTypedList(this.logos)
    dest.writeString(this.title)
  }

  protected constructor(`in`: Parcel) {
    this.logos = `in`.createTypedArrayList(Logo.CREATOR)
    this.title = `in`.readString()
  }

  companion object {

    @JvmField val CREATOR: Parcelable.Creator<Partner> = object : Parcelable.Creator<Partner> {
      override fun createFromParcel(source: Parcel): Partner {
        return Partner(source)
      }

      override fun newArray(size: Int): Array<Partner?> {
        return arrayOfNulls(size)
      }
    }
  }
}
