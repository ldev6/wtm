package com.montreal.wtm.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import java.util.ArrayList;

@IgnoreExtraProperties
public class Day implements Parcelable {
    
    @PropertyName("date")
    public String date;
    
    
    @PropertyName("dateReadable")
    public String dateReadable;
    
    @PropertyName("timeslots")
    public ArrayList<Timeslot> timeslots;
    
    @PropertyName("tracks")
    public ArrayList<Track> tracks;
    
    
    public ArrayList<Talk> talks;

    public Day() {
    }

    public Day(String date, String dateReadable, ArrayList<Timeslot> timeslots, ArrayList<Track> tracks) {
        this.date = date;
        this.dateReadable = dateReadable;
        this.timeslots = timeslots;
        this.tracks = tracks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.date);
        dest.writeString(this.dateReadable);
        dest.writeTypedList(this.timeslots);
        dest.writeTypedList(this.tracks);
    }

    protected Day(Parcel in) {
        this.date = in.readString();
        this.dateReadable = in.readString();
        this.timeslots = in.createTypedArrayList(Timeslot.CREATOR);
        this.tracks = in.createTypedArrayList(Track.CREATOR);
    }

    public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel source) {
            return new Day(source);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
