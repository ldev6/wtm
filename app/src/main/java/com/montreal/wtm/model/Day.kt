package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import java.util.ArrayList

@IgnoreExtraProperties
class Day : Parcelable {

  @PropertyName("date")
  lateinit var date: String


  @PropertyName("dateReadable")
  lateinit var dateReadable: String

  @PropertyName("timeslots")
  lateinit var timeslots: ArrayList<Timeslot>

  @PropertyName("tracks")
  lateinit var tracks: ArrayList<Track>

  var talks: ArrayList<Talk>? = null

  constructor()

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeString(this.date)
    dest.writeString(this.dateReadable)
    dest.writeTypedList(this.timeslots)
    dest.writeTypedList(this.tracks)
  }

  protected constructor(`in`: Parcel) {
    this.date = `in`.readString()
    this.dateReadable = `in`.readString()
    this.timeslots = `in`.createTypedArrayList(Timeslot.CREATOR)
    this.tracks = `in`.createTypedArrayList(Track.CREATOR)
  }

  companion object {

    @JvmField val CREATOR: Parcelable.Creator<Day> = object : Parcelable.Creator<Day> {
      override fun createFromParcel(source: Parcel): Day {
        return Day(source)
      }

      override fun newArray(size: Int): Array<Day?> {
        return arrayOfNulls(size)
      }
    }
  }
}
