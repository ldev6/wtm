package com.montreal.wtm.model

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import com.google.firebase.database.PropertyName
import java.util.ArrayList

@IgnoreExtraProperties
class Session : Parcelable {
  val TECHNICAL_SKILL = "Technical Skill"

  @PropertyName("id")
  var id: Int = 0

  @PropertyName("complexity")
  var complexity: String = ""

  @PropertyName("title")
  lateinit var title: String

  @PropertyName("description")
  lateinit var description: String

  @PropertyName("language")
  var language: String = "English"

  @PropertyName("roomId")
  var roomId: Int = 0

  @PropertyName("type")
  var type: String? = null

  @PropertyName("speakers")
  var speakers: ArrayList<Int> = ArrayList<Int>()

  @PropertyName("tags")
  var tags: ArrayList<String> = ArrayList<String>()

  constructor()

  override fun describeContents(): Int {
    return 0
  }

  override fun writeToParcel(dest: Parcel, flags: Int) {
    dest.writeInt(this.id)
    dest.writeString(this.complexity)
    dest.writeString(this.title)
    dest.writeString(this.description)
    dest.writeString(this.language)
    dest.writeSerializable(if (this.speakers == null) ArrayList<Int>() else this.speakers)
    dest.writeSerializable(this.tags)
  }

  protected constructor(`in`: Parcel) {
    this.id = `in`.readInt()
    this.complexity = `in`.readString() ?: ""
    this.title = `in`.readString() ?: ""
    this.description = `in`.readString() ?: ""
    this.language = `in`.readString() ?: ""
    this.speakers = `in`.readSerializable() as ArrayList<Int>
    this.tags = `in`.readSerializable() as ArrayList<String>
  }

  companion object {

    @JvmField
    val CREATOR: Parcelable.Creator<Session> = object : Parcelable.Creator<Session> {
      override fun createFromParcel(source: Parcel): Session {
        return Session(source)
      }

      override fun newArray(size: Int): Array<Session?> {
        return arrayOfNulls(size)
      }
    }
  }

  fun isTechnicalTalk():Boolean {
    for(tag in tags) {
      if(tag.contains(TECHNICAL_SKILL, true)) {
        return true
      }
    }
    return false
  }

  fun isIncludedIn(savedSessions:HashMap<String, Boolean>? ) :Boolean {
    return savedSessions?.get(id.toString())?:false;
  }
}
