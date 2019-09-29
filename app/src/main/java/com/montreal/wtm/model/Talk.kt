package com.montreal.wtm.model


import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.Html
import android.text.Spanned

class Talk(var session: Session, var time: String, var room: String,
    var saved: Boolean) : Parcelable {

  var type: Type

  constructor(parcel: Parcel) : this(
      parcel.readParcelable(Session::class.java.classLoader)!!,
      parcel.readString()?:"",
      parcel.readString()?:"",
      parcel.readByte() != 0.toByte()) {

  }

  init {

    if (session.type == "break") {
      type = Type.Break
    } else if (session.type == "food") {
      type = Type.Food
    } else if (session.type == "general") {
      type = Type.General
    } else {
      type = Type.Talk
    }
  }

  enum class Type {
    Talk,
    General,
    Food,
    Break
  }

  fun getSessionId(): Int {
    return session.id
  }

  fun getSessionTitle(): Spanned? {
    return Html.fromHtml(session.title ?: "")
  }

  fun getSessionDescription(): Spanned? {
    return Html.fromHtml(session.description ?: "")
  }

  fun getSpeakers(): ArrayList<Int> {
    return session.speakers
  }

  fun hasSpeakers(): Boolean {
    return session.speakers.isNotEmpty()
  }

  fun getLocationTimeLocale(): String {
    return room + " / " + time + " / " + session.language
  }

  fun getSessionTags(): String {
    var result = ""
    for (tag in session.tags) {
      if (!result.isBlank()) {
        result += ", "
      }
      result += tag
    }
    return result
  }


  override fun writeToParcel(parcel: Parcel, flags: Int) {
    parcel.writeParcelable(session, flags)
    parcel.writeString(time)
    parcel.writeString(room)
    parcel.writeByte(if (saved) 1 else 0)
  }

  override fun describeContents(): Int {
    return 0
  }

  companion object CREATOR : Creator<Talk> {
    override fun createFromParcel(parcel: Parcel): Talk {
      return Talk(parcel)
    }

    override fun newArray(size: Int): Array<Talk?> {
      return arrayOfNulls(size)
    }
  }
}
