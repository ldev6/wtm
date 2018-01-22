package com.montreal.wtm.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Session implements Parcelable {

    @PropertyName("id")
    public int id;

    @PropertyName("complexity")
    public String complexity;

    @PropertyName("title")
    public String title;

    @PropertyName("description")
    public String description;

    @PropertyName("language")
    public String language;

    @PropertyName("speakers")
    public ArrayList<Integer> speakersId;

    @PropertyName("tags")
    public ArrayList<String> tags;

    public Session(int id, String complexity, String title, String description, String language,
        ArrayList<Integer> speakersId, ArrayList<String> tags) {
        this.id = id;
        this.complexity = complexity;
        this.title = title;
        this.description = description;
        this.language = language;
        this.speakersId = speakersId;
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public String getComplexity() {
        return complexity;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public ArrayList<Integer> getSpeakersId() {
        return speakersId;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.complexity);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.language);
        dest.writeSerializable(this.speakersId);
        dest.writeSerializable(this.tags);
    }

    public Session() {
    }

    protected Session(Parcel in) {
        this.id = in.readInt();
        this.complexity = in.readString();
        this.title = in.readString();
        this.description = in.readString();
        this.language = in.readString();
        this.speakersId = (ArrayList<Integer>) in.readSerializable();
        this.tags = (ArrayList<String>) in.readSerializable();
    }

    public static final Creator<Session> CREATOR = new Creator<Session>() {
        @Override
        public Session createFromParcel(Parcel source) {
            return new Session(source);
        }

        @Override
        public Session[] newArray(int size) {
            return new Session[size];
        }
    };
}
