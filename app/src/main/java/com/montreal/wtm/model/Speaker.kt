package com.montreal.wtm.model


import android.os.Parcel
import android.os.Parcelable

import com.google.firebase.database.PropertyName


class Speaker : Parcelable {

  val NO_SESSION = -1

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

  @PropertyName("id")
  var id: Int = 0

  @PropertyName("socials")
  var socials: ArrayList<Social> = ArrayList()


  @PropertyName("sessionId")
  var sessionId: Int = NO_SESSION

  constructor() {}

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeInt(this.id)
    dest.writeString(this.name)
    dest.writeString(this.title)
    dest.writeString(this.shortBio)
    dest.writeString(this.bio)
    dest.writeString(this.photoUrl)
    dest.writeSerializable(this.socials)
    dest.writeInt(this.sessionId)
  }

  protected constructor(`in`: Parcel) {
    this.id = `in`.readInt() ?: 0
    this.name = `in`.readString() ?: ""
    this.title = `in`.readString() ?: ""
    this.shortBio = `in`.readString() ?: ""
    this.bio = `in`.readString() ?: ""
    this.photoUrl = `in`.readString() ?: ""
    this.socials = `in`.readSerializable() as ArrayList<Social>
    this.sessionId = `in`.readInt()
  }

  companion object {

    @JvmField
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
