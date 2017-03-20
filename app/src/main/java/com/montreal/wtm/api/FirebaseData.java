package com.montreal.wtm.api;


import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.montreal.wtm.model.Location;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.model.Sponsor;
import com.montreal.wtm.model.Talk;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseData {

    private static String TAG = FirebaseData.class.getSimpleName();


    public interface RequestListener<T> {
        void onDataChange(T object);

        void onCancelled(DatabaseError error);
    }

    public static void getSpeaker(final RequestListener<Speaker> requestListener, String speakerId) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("speakers/" + speakerId);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Speaker speaker = dataSnapshot.getValue(Speaker.class);
                if (speaker != null) {
                    requestListener.onDataChange(speaker);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Speaker failed" + error.getMessage());
                requestListener.onCancelled(error);
            }
        });
    }

    public static void getSpeakers(final RequestListener<HashMap<String, Speaker>> requestListener) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("speakers");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Speaker> map = new HashMap<String, Speaker>();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    Speaker speaker = children.getValue(Speaker.class);
                    map.put(children.getKey(), speaker);
                }
                Log.v(TAG, "receive data");
                requestListener.onDataChange(map);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Speakers failed =" + error.getMessage());
                requestListener.onCancelled(error);
            }
        });
    }

    public static void getSchedule(final RequestListener<ArrayList<Talk>> requestListener, int day) {
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("schedule-day-" + day);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Talk> talks = new ArrayList<Talk>();
                for (DataSnapshot children : dataSnapshot.getChildren()) {
                    talks.add(children.getValue(Talk.class));
                }
                requestListener.onDataChange(talks);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Schedule failed =" + error.getMessage());
                requestListener.onCancelled(error);
            }
        });
    }

    public static void getSponsors(final RequestListener<HashMap<String, ArrayList<Sponsor>>> requestListener) {
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
                Log.v(TAG, "receive data");
                requestListener.onDataChange(map);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Crashlytics.log("Get Sponsors failed =" + error.getMessage());
                requestListener.onCancelled(error);
            }
        });
    }

    public static void getLocation(final RequestListener<Location> requestListener) {
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
                requestListener.onCancelled(error);
            }
        });
    }
}
