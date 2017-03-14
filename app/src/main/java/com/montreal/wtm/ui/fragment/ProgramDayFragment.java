package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.ui.adapter.ScheduleAdapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class ProgramDayFragment extends Fragment {

    private static String EXTRA_DAY = "EXTRA_DAY";

    private ScheduleAdapter mAdapter;
    private ArrayList<Talk> mTalks;


    public static ProgramDayFragment newInstance(int day) {

        Bundle args = new Bundle();
        args.putInt(EXTRA_DAY, day);
        ProgramDayFragment fragment = new ProgramDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.program_day_fragment, container, false);

        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        mTalks = new ArrayList<>();
        mAdapter = new ScheduleAdapter(getActivity(), mTalks);
        recyclerView.setAdapter(mAdapter);

        int day = getArguments().getInt(EXTRA_DAY);
        FirebaseData.getSchedule(requestListener, day);
        return v;
    }


    private FirebaseData.RequestListener<ArrayList<Talk>> requestListener = new FirebaseData.RequestListener<ArrayList<Talk>>() {
        @Override
        public void onDataChange(ArrayList<Talk> object) {
            mTalks.clear();
            mTalks.addAll(object);
            Collections.sort(mTalks, new Comparator<Talk>() {
                public int compare(Talk v1, Talk v2) {
                    String time1 = v1.time.split("-")[0];
                    String time2 = v2.time.split("-")[0];
                    SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa", Locale.CANADA);
                    try {
                        Date date1 = sdf.parse(time1);
                        Date date2 = sdf.parse(time2);
                        return date1.compareTo(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return v1.time.toUpperCase().compareTo(v2.time.toUpperCase());
                }
            });
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    };
}
