package com.montreal.wtm.api;


import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.montreal.wtm.WTMApplication;
import com.montreal.wtm.model.Location;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.model.Sponsor;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseData {

    private static String TAG = FirebaseData.class.getSimpleName();


    public enum ErrorFirebase {
        network, firebase
    }

    public interface RequestListener<T> {
        void onDataChange(T object);

        void onCancelled(ErrorFirebase errorType);
    }


    public static void getSpeaker(final RequestListener<Speaker> requestListener, String speakerId) {
        firebaseConnected(requestListener);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("speakers/" + speakerId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Speaker speaker = dataSnapshot.getValue(Speaker.class);
                if (speaker != null) {
                    requestListener.onDataChange(speaker);
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

    public static void getSpeakers(final RequestListener<HashMap<String, Speaker>> requestListener) {
        firebaseConnected(requestListener);
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
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Speakers failed =" + error.getMessage());
                requestListener.onCancelled(ErrorFirebase.firebase);
            }
        });

    }

    public static void getSchedule(final RequestListener<ArrayList<Talk>> requestListener, int day) {
        firebaseConnected(requestListener);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("schedule-day-" + day);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildren() != null) {
                    ArrayList<Talk> talks = new ArrayList<Talk>();
                    for (DataSnapshot children : dataSnapshot.getChildren()) {
                        talks.add(children.getValue(Talk.class));
                    }
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

    public static void getSponsors(final RequestListener<HashMap<String, ArrayList<Sponsor>>> requestListener) {
        firebaseConnected(requestListener);
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

    public static void getLocation(final RequestListener<Location> requestListener) {
        firebaseConnected(requestListener);
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("location");
        myRef.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Location location = dataSnapshot.getValue(Location.class);
                if (location != null) {
                    requestListener.onDataChange(location);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Crashlytics.log("Get Location failed =" + error.getMessage());
                requestListener.onCancelled(ErrorFirebase.firebase);
            }
        });
    }

    public static void firebaseConnected(final RequestListener requestListener) {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (connected) {
                    Log.v(TAG, "connected");
                } else {
                    Log.v(TAG, "not connected");
                    if (!NetworkUtils.isNetworkAvailable(WTMApplication.applicationContext)) {
                        requestListener.onCancelled(ErrorFirebase.network);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Log.v(TAG, "Listener was cancelled");
            }
        });
    }
}
