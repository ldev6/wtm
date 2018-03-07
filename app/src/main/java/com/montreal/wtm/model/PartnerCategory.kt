package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import java.util.ArrayList

@IgnoreExtraProperties
class PartnerCategory : Parcelable {

  @PropertyName("logos")
  lateinit var sponsors: ArrayList<Sponsor>

  @PropertyName("title")
  lateinit var title: String

  constructor()

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeTypedList(this.sponsors)
    dest.writeString(this.title)
  }

  protected constructor(`in`: Parcel) {
    this.sponsors = `in`.createTypedArrayList(Sponsor.CREATOR)
    this.title = `in`.readString()
  }

  companion object {

    val CREATOR: Parcelable.Creator<PartnerCategory> = object : Parcelable.Creator<PartnerCategory> {
      override fun createFromParcel(source: Parcel): PartnerCategory {
        return PartnerCategory(source)
      }

      override fun newArray(size: Int): Array<PartnerCategory?> {
        return arrayOfNulls(size)
      }
    }
  }
}
