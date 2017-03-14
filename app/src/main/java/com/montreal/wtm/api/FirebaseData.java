package com.montreal.wtm.api;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.model.Talk;

import java.util.ArrayList;
import java.util.HashMap;

public class FirebaseData {

    private static String TAG = FirebaseData.class.getSimpleName();


    public interface RequestListener<T> {
        void onDataChange(T object);

        void onCancelled(DatabaseError error);
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
                Log.w(TAG, "Failed to read value.", error.toException());
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
                Log.w(TAG, "Failed to read value.", error.toException());
                requestListener.onCancelled(error);
            }
        });
    }
}
