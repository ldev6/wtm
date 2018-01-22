package com.montreal.wtm.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Timeslot implements Parcelable {

    @PropertyName("startTime")
    public String startTime;

    @PropertyName("endTime")
    public String endTime;

    @PropertyName("sessions")
    public ArrayList<ArrayList<Integer>> sessionsId;

    public Timeslot() {
    }

    public Timeslot(String startTime, String endTime, ArrayList<ArrayList<Integer>> sessionsId) {

        this.startTime = startTime;
        this.endTime = endTime;
        this.sessionsId = sessionsId;
    }

    public String getTime() {
        return startTime + " - " + endTime;
    }

    public ArrayList<ArrayList<Integer>> getSessionsId() {
        return sessionsId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.startTime);
        dest.writeString(this.endTime);
        dest.writeSerializable(this.sessionsId);
    }

    protected Timeslot(Parcel in) {
        this.startTime = in.readString();
        this.endTime = in.readString();
        this.sessionsId = (ArrayList<ArrayList<Integer>>) in.readSerializable();
    }

    public static final Parcelable.Creator<Timeslot> CREATOR = new Parcelable.Creator<Timeslot>() {
        @Override
        public Timeslot createFromParcel(Parcel source) {
            return new Timeslot(source);
        }

        @Override
        public Timeslot[] newArray(int size) {
            return new Timeslot[size];
        }
    };
}
