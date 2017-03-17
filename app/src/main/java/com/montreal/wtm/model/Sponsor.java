package com.montreal.wtm.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class Sponsor {

    @PropertyName("image")
    protected String imageKey;

    @PropertyName("name")
    protected String name;

    @PropertyName("website")
    protected String urlWebsite;

    public Sponsor() {
    }

    public Sponsor(String imageKey, String name, String urlWebsite) {
        this.imageKey = imageKey;
        this.name = name;
        this.urlWebsite = urlWebsite;
    }

    public String getImageKey() {
        return imageKey;
    }

    public String getName() {
        return name;
    }

    public String getUrlWebsite() {
        return urlWebsite;
    }
}
