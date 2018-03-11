package com.montreal.wtm.model


import android.os.Parcel
import android.os.Parcelable
import android.os.Parcelable.Creator
import android.text.Html
import android.text.Spanned
import com.montreal.wtm.model.Talk.Type.General
import java.io.Serializable

class Talk(var session: Session, var time: String, var room: String, var saved: Boolean): Parcelable {

  var type: Type

  constructor(parcel: Parcel) : this(
      parcel.readParcelable(Session::class.java.classLoader),
      parcel.readString(),
      parcel.readString(),
      parcel.readByte() != 0.toByte()) {

  }

  init {

    if (session.speakers.isEmpty()) {
      type = Type.Talk
    } else if (session.type == "break") {
      type = Type.Break
    } else if (session.type == "food") {
      type = Type.Food
    } else {
      type = Type.General
    }
    type = General
  }

  enum class Type {
    Talk,
    General,
    Food,
    Break
  }

  fun getSessionId() : Int {
    return session.id
  }

  fun getSessionTitle(): Spanned? {
      return Html.fromHtml(session.title?:"")
  }

  fun getSessionDescription(): Spanned? {
      return Html.fromHtml(session.description?:"")
  }

  fun getSpeakers():ArrayList<Int> {
    return session.speakers
  }

  fun hasSpeakers():Boolean {
    return session.speakers.isNotEmpty()
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
