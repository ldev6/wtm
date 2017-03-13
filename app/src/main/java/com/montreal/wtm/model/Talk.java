package com.montreal.wtm.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Talk {

    @PropertyName("room")
    public String room;

    @PropertyName("speaker")
    public String speakerId;

    @PropertyName("time")
    public String time;

    @PropertyName("title")
    public String title;

    public String description;

    public Talk() {
    }

    public Talk(String room, String speakerId, String time, String title, String description) {
        this.room = room;
        this.speakerId = speakerId;
        this.time = time;
        this.title = title;
        this.description = description;
    }
}
