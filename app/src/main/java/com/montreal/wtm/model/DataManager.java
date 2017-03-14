package com.montreal.wtm.model;


import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.montreal.wtm.WTMApplication;


import java.util.ArrayList;
import java.util.Arrays;


public class DataManager {

    private static final String LOVED_TALK = "LOVED_TALK";
    private static final String SHARED_PREFS_FILE = "com.montreal.wtm.preference";
    private static DataManager instance;

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    private ArrayList<String> mLoveTalks;

    private DataManager() {
        SharedPreferences prefs = WTMApplication.applicationContext.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        String json = prefs.getString(LOVED_TALK, null);
        if (json == null) {
            mLoveTalks = new ArrayList<>();
        } else {
            Gson gson = new Gson();
            String[] loveTalksArray = gson.fromJson(json, String[].class);
            mLoveTalks = new ArrayList<String>(Arrays.asList(loveTalksArray));
        }
    }


    public void addLoveTalk(String speakerId) {
        if (!mLoveTalks.contains(speakerId)) {
            mLoveTalks.add(speakerId);
        }
    }

    public ArrayList<String> getLoveTalks() {
        return mLoveTalks;
    }

    public void removeLoveTalks(String speakerId) {
        mLoveTalks.remove(speakerId);
    }

    public boolean loveTalkContainSpeaker(String speakerId) {
        return mLoveTalks.contains(speakerId);
    }

    public void saveToSharePreference() {
        SharedPreferences prefs = WTMApplication.applicationContext.getSharedPreferences(SHARED_PREFS_FILE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        Gson gson = new Gson();
        String jsonText = gson.toJson(mLoveTalks);
        editor.putString(LOVED_TALK, jsonText).apply();
    }
}
