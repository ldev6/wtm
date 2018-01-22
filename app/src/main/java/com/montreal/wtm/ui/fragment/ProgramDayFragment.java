package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.model.Timeslot;
import com.montreal.wtm.ui.adapter.ScheduleAdapter;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;

public class ProgramDayFragment extends BaseFragment {

    private static String EXTRA_DAY = "EXTRA_DAY";

    private ScheduleAdapter mAdapter;
    private ArrayList<Talk> talks;
    private int mDay;

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
        talks = new ArrayList<>();
        mAdapter = new ScheduleAdapter(getActivity(), talks);
        recyclerView.setAdapter(mAdapter);
        setMessageViewInterface(this);
        showProgressBar();

        mDay = getArguments().getInt(EXTRA_DAY);
        FirebaseData.INSTANCE.getSchedule(getActivity(), requestListener, mDay);
        return v;
    }

    private FirebaseData.RequestListener<ArrayList<Timeslot>> requestListener = new FirebaseData.RequestListener<ArrayList<Timeslot>>() {
        @Override
        public void onDataChange(ArrayList<Timeslot> object) {
            
            talks.clear();
            //talks.addAll(object);
            //Collections.sort(talks, new Comparator<Talk>() {
            //    public int compare(Talk v1, Talk v2) {
            //        String time1 = v1.getTime().split("-")[0];
            //        String time2 = v2.getTime().split("-")[0];
            //        SimpleDateFormat sdf = new SimpleDateFormat("h:mm aa", Locale.CANADA);
            //        try {
            //            Date date1 = sdf.parse(time1);
            //            Date date2 = sdf.parse(time2);
            //            return date1.compareTo(date2);
            //        } catch (ParseException e) {
            //            e.printStackTrace();
            //        }
            //        return v1.getTime().toUpperCase().compareTo(v2.getTime().toUpperCase());
            //    }
            //});
            mAdapter.notifyDataSetChanged();
            hideMessageView();
        }

        @Override
        public void onCancelled(FirebaseData.ErrorFirebase errorType) {
            //TODO
            //String message = errorType == FirebaseData.INSTANCE.ErrorFirebase.network ? getString(R.string.default_error_message) : getString(R.string.error_message_serveur_prob);
            //setMessageError(message);
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void retryOnProblem() {
        if (!isAdded()) {
            return;
        }
        FirebaseData.INSTANCE.getSchedule(getActivity(), requestListener, mDay);
    }
}
