package com.montreal.wtm.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Speaker implements Parcelable {

    @PropertyName("name")
    public String name;

    @PropertyName("title")
    public String title;
    
    @PropertyName("shortBio")
    public String shortBio;
    
    @PropertyName("bio")
    public String bio;
    
    @PropertyName("photoUrl")
    public String photoUrl;

    
    public Speaker() {
    }

    public Speaker(String name, String title, String shortBio, String bio, String photoUrl) {
        this.name = name;
        this.title = title;
        this.shortBio = shortBio;
        this.bio = bio;
        this.photoUrl = photoUrl;
    }

    public String getName() {
        return name;
    }

    public String getShortBio() {
        return shortBio;
    }

    public String getBio() {
        return bio;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.shortBio);
        dest.writeString(this.bio);
        dest.writeString(this.photoUrl);
    }

    protected Speaker(Parcel in) {
        this.name = in.readString();
        this.title = in.readString();
        this.shortBio = in.readString();
        this.bio = in.readString();
        this.photoUrl = in.readString();
    }

    public static final Creator<Speaker> CREATOR = new Creator<Speaker>() {
        @Override
        public Speaker createFromParcel(Parcel source) {
            return new Speaker(source);
        }

        @Override
        public Speaker[] newArray(int size) {
            return new Speaker[size];
        }
    };
}
