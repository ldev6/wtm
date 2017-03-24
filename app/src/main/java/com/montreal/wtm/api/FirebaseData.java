package com.montreal.wtm.api;


import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.montreal.wtm.WTMApplication;
import com.montreal.wtm.model.Location;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.model.Sponsor;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.utils.NetworkUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseData {

    private static String TAG = FirebaseData.class.getSimpleName();

    public enum ErrorFirebase {
        network, firebase
    }

    public interface RequestListener<T> {
        void onDataChange(final T object);

        void onCancelled(final ErrorFirebase errorType);
    }

    public static void getSpeaker(final Activity activity, final RequestListener<Speaker> requestListener, final String speakerId) {
        final String fileName = "Speaker_" + speakerId + ".json";
        firebaseConnected(activity, requestListener, fileName, Speaker.class);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("speakers/" + speakerId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Speaker speaker = dataSnapshot.getValue(Speaker.class);
                if (speaker != null) {
                    requestListener.onDataChange(speaker);
                    saveInFile(fileName, speaker);
                } else {
                    if (NetworkUtils.isNetworkAvailable(WTMApplication.applicationContext)) {
                        requestListener.onCancelled(ErrorFirebase.network);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Speaker failed" + error.getMessage());
                requestListener.onCancelled(ErrorFirebase.firebase);
            }
        });
    }

    public static void getSpeakers(final Activity activity, final RequestListener<HashMap<String, Speaker>> requestListener) {

        final String fileName = "Speakers.json";
        firebaseConnected(activity, requestListener, fileName, new TypeToken<HashMap<String, Speaker>>() {
        }.getType());

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("speakers");

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Speaker> map = new HashMap<String, Speaker>();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Speaker speaker = children.getValue(Speaker.class);
                    map.put(children.getKey(), speaker);
                }
                requestListener.onDataChange(map);
                saveInFile(fileName, map);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Speakers failed =" + error.getMessage());
                requestListener.onCancelled(ErrorFirebase.firebase);
            }
        });

    }

    public static void getSchedule(final Activity activity, final RequestListener<ArrayList<Talk>> requestListener, final int day) {
        final String fileName = "Schedule" + day + ".json";
        firebaseConnected(activity, requestListener, fileName, new TypeToken<ArrayList<Talk>>() {
        }.getType());

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("schedule-day-" + day);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    ArrayList<Talk> talks = new ArrayList<Talk>();
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        talks.add(children.getValue(Talk.class));
                    }
                    saveInFile(fileName, talks);
                    requestListener.onDataChange(talks);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Schedule failed =" + error.getMessage());
                requestListener.onCancelled(ErrorFirebase.firebase);
            }
        });
    }

    public static void getSponsors(final Activity activity, final RequestListener<HashMap<String, ArrayList<Sponsor>>> requestListener) {
        final String fileName = "Sponsors.json";
        firebaseConnected(activity, requestListener, fileName, new TypeToken<HashMap<String, ArrayList<Sponsor>>>() {
        }.getType());
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("sponsor");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, ArrayList<Sponsor>> map = new HashMap<String, ArrayList<Sponsor>>();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    ArrayList<Sponsor> sponsors = new ArrayList<Sponsor>();
                    for (DataSnapshot subChildren : children.getChildren()) {
                        Sponsor sponsor = subChildren.getValue(Sponsor.class);
                        sponsors.add(sponsor);
                    }
                    map.put(children.getKey(), sponsors);
                }
                saveInFile(fileName, map);
                requestListener.onDataChange(map);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Sponsors failed =" + error.getMessage());
                requestListener.onCancelled(ErrorFirebase.firebase);
            }
        });
    }

    public static void getLocation(final Activity activity, final RequestListener<Location> requestListener) {
        final String fileName = "Location.json";
        firebaseConnected(activity, requestListener, fileName, (Type) Location.class);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("location");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Location location = dataSnapshot.getValue(Location.class);
                if (location != null) {
                    requestListener.onDataChange(location);
                    saveInFile(fileName, location);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Crashlytics.log("Get Location failed =" + error.getMessage());
                requestListener.onCancelled(ErrorFirebase.firebase);
            }
        });
    }

    public static void firebaseConnected(final Activity activity, final RequestListener requestListener, final String nameFile, final Type type) {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (!connected) {
                    if (!NetworkUtils.isNetworkAvailable(WTMApplication.applicationContext)) {
                        new ReadFile(activity, requestListener, type).execute(nameFile);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.v(TAG, "Listener was cancelled");
            }
        });
    }

    private static void saveInFile(String nameFile, Object object) {
        Gson gson = new Gson();
        String json = gson.toJson(object);
        new SaveFile().execute(nameFile, json);
    }


    private static class ReadFile extends AsyncTask<String, Void, Void> {

        private RequestListener mRequestListener;
        private Type mType;
        private Activity mActivity;

        ReadFile(Activity activity, RequestListener requestListener, Type classType) {
            mRequestListener = requestListener;
            mType = classType;
            mActivity = activity;
        }

        @Override
        protected Void doInBackground(String... params) {
            String nameFile = params[0];
            try {
                FileInputStream fis = WTMApplication.applicationContext.openFileInput(nameFile);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fis, "UTF-8"));

                int charByte;
                String dataFile = "";
                while ((charByte = reader.read()) != -1) {
                    dataFile = dataFile + Character.toString((char) charByte);
                }
                fis.close();
                final String jsonFile = dataFile;

                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Gson gson = new Gson();
                            Object object = gson.fromJson(jsonFile, mType);
                            mRequestListener.onDataChange(object);
                        } catch (JsonSyntaxException e) {
                            mRequestListener.onCancelled(ErrorFirebase.network);
                        }
                    }
                });
            } catch (IOException e) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mRequestListener.onCancelled(ErrorFirebase.network);
                    }
                });
            }
            return null;
        }
    }

    private static class SaveFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            String nameFile = params[0];
            String file = params[1];
            try {
                FileOutputStream fos = WTMApplication.applicationContext.openFileOutput(nameFile, Context.MODE_PRIVATE);
                fos.write(file.getBytes());
                fos.close();
            } catch (IOException e) {
                Crashlytics.log("Error saving file=" + e.getMessage());
            }
            return null;
        }
    }
}
