package com.montreal.wtm.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;
import java.util.ArrayList;

@IgnoreExtraProperties
public class PartnerCategory implements Parcelable {

    @PropertyName("logos")
    public ArrayList<Sponsor> sponsors;

    @PropertyName("title")
    public String title;

    public PartnerCategory() {
    }

    public PartnerCategory(ArrayList<Sponsor> sponsors, String title) {
        this.sponsors = sponsors;
        this.title = title;
    }

    public ArrayList<Sponsor> getSponsors() {
        return sponsors;
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
        dest.writeTypedList(this.sponsors);
        dest.writeString(this.title);
    }

    protected PartnerCategory(Parcel in) {
        this.sponsors = in.createTypedArrayList(Sponsor.CREATOR);
        this.title = in.readString();
    }

    public static final Creator<PartnerCategory> CREATOR = new Creator<PartnerCategory>() {
        @Override
        public PartnerCategory createFromParcel(Parcel source) {
            return new PartnerCategory(source);
        }

        @Override
        public PartnerCategory[] newArray(int size) {
            return new PartnerCategory[size];
        }
    };
}
