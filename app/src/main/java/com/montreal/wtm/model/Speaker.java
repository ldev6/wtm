package com.montreal.wtm.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Speaker implements Parcelable {

    @PropertyName("description")
    public String description;

    @PropertyName("first_name")
    public String firstName;

    @PropertyName("last_name")
    public String lastName;

    @PropertyName("title")
    public String title;

    public Speaker() {
    }

    public Speaker(String description, String firstName, String lastName, String title) {
        this.description = description;
        this.firstName = firstName;
        this.lastName = lastName;
        this.title = title;
    }

    protected Speaker(Parcel in) {
        description = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        title = in.readString();
    }

    public static final Creator<Speaker> CREATOR = new Creator<Speaker>() {
        @Override
        public Speaker createFromParcel(Parcel in) {
            return new Speaker(in);
        }

        @Override
        public Speaker[] newArray(int size) {
            return new Speaker[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(title);
    }

    public String getName() {
        return firstName + " " + lastName;
    }


    public String getDescription() {
        return description;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getTitle() {
        return title;
    }
}
