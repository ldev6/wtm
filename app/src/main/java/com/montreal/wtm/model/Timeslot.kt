package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import java.util.ArrayList

@IgnoreExtraProperties
class Timeslot : Parcelable {

  @PropertyName("startTime")
  lateinit var startTime: String

  @PropertyName("endTime")
  lateinit var endTime: String

  @PropertyName("sessions")
  lateinit var sessionsId: ArrayList<ArrayList<Int>>

  constructor()

  val time: String
    get() = startTime + " - " + endTime

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(this.startTime)
    dest.writeString(this.endTime)
    dest.writeSerializable(this.sessionsId)
  }

  protected constructor(`in`: Parcel) {
    this.startTime = `in`.readString()
    this.endTime = `in`.readString()
    this.sessionsId = `in`.readSerializable() as ArrayList<ArrayList<Int>>
  }

  companion object {

    @JvmField val CREATOR: Parcelable.Creator<Timeslot> = object : Parcelable.Creator<Timeslot> {
      override fun createFromParcel(source: Parcel): Timeslot {
        return Timeslot(source)
      }

      override fun newArray(size: Int): Array<Timeslot?> {
        return arrayOfNulls(size)
      }
    }
  }
}
