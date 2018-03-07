package com.montreal.wtm.model

import com.montreal.wtm.model.Talk.Type.General

class Talk(var session: Session, var time: String, var room: String) {

  var type: Type

  init {

//    //TODO CHANGE
//    if (session.speakersId != null) {
//      type = Type.Talk
//    } else if (session.type == "break") {
//      type = Type.Break
//    } else if (session.type == "food") {
//      type = Type.Food
//    } else {
//      type = Type.General
//    }
    type = General
  }

  enum class Type {
    Talk,
    General,
    Food,
    Break
  }
}
