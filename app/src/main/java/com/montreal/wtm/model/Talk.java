package com.montreal.wtm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Talk implements Parcelable {

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

    @PropertyName("description")
    public String description;

    protected Talk(Parcel in) {
        type = in.readString();
        room = in.readString();
        speakerId = in.readString();
        time = in.readString();
        title = in.readString();
        description = in.readString();
    }

    public static final Creator<Talk> CREATOR = new Creator<Talk>() {
        @Override
        public Talk createFromParcel(Parcel in) {
            return new Talk(in);
        }

        @Override
        public Talk[] newArray(int size) {
            return new Talk[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(room);
        dest.writeString(speakerId);
        dest.writeString(time);
        dest.writeString(title);
        dest.writeString(description);
    }


    public enum Type {
        Talk, General, Food, Break;
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

    public String getType() {
        return type;
    }

    public String getRoom() {
        return room;
    }

    public String getSpeakerId() {
        return speakerId;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}
