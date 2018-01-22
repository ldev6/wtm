package com.montreal.wtm.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Sponsor implements Parcelable {

    @PropertyName("logoUrl")
    public String logoUrl;

    @PropertyName("name")
    public String name;

    @PropertyName("url")
    public String urlWebsite;

    public Sponsor() {
    }

    public Sponsor(String logoUrl, String name, String urlWebsite) {
        this.logoUrl = logoUrl;
        this.name = name;
        this.urlWebsite = urlWebsite;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getName() {
        return name;
    }

    public String getUrlWebsite() {
        return urlWebsite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.logoUrl);
        dest.writeString(this.name);
        dest.writeString(this.urlWebsite);
    }

    protected Sponsor(Parcel in) {
        this.logoUrl = in.readString();
        this.name = in.readString();
        this.urlWebsite = in.readString();
    }

    public static final Parcelable.Creator<Sponsor> CREATOR = new Parcelable.Creator<Sponsor>() {
        @Override
        public Sponsor createFromParcel(Parcel source) {
            return new Sponsor(source);
        }

        @Override
        public Sponsor[] newArray(int size) {
            return new Sponsor[size];
        }
    };
}
