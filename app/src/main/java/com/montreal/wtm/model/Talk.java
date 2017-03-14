package com.montreal.wtm.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Talk {

    @PropertyName("type")
    public String type;

    @PropertyName("room")
    public String room;

    @PropertyName("speaker")
    public String speakerId;

    @PropertyName("time")
    public String time;

    @PropertyName("title")
    public String title;

    public String description;


    public enum Type {
        Talk, Food, Break;
    }

    public Talk() {
    }

    public Talk(String type, String room, String speakerId, String time, String title, String description) {
        this.type = type;
        this.room = room;
        this.speakerId = speakerId;
        this.time = time;
        this.title = title;
        this.description = description;
    }
}
