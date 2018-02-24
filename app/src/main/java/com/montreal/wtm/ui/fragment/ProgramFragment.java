package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Day;
import com.montreal.wtm.model.Session;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.model.Timeslot;
import com.montreal.wtm.model.Track;
import com.montreal.wtm.ui.adapter.ProgramFragmentPagerAdapter;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;
import java.util.ArrayList;
import java.util.HashMap;
import org.jetbrains.annotations.NotNull;

public class ProgramFragment extends BaseFragment {

    public static ProgramFragment newInstance() {
        ProgramFragment fragment = new ProgramFragment();
        return fragment;
    }

    private ViewPager viewPager;
    private HashMap<String, Session> sessionHashMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.program_fragment, container, false);
        viewPager =  v.findViewById(R.id.viewpager);
        TabLayout tabLayout = v.findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

        FirebaseData.INSTANCE.getSessions(getActivity(), requestListenerSession);
        setMessageViewInterface(this);
        showProgressBar();
        return v;
    }

    private FirebaseData.RequestListener<ArrayList<Day>> requestListenerDays =
        new FirebaseData.RequestListener<ArrayList<Day>>() {
            @Override
            public void onDataChange(ArrayList<Day> days) {

                //TODO REFACTOR IN RX
                for (Day day : days) {
                    ArrayList<Track> tracks = day.tracks;
                    ArrayList<Talk> talks = new ArrayList<>();
                    for (Timeslot timeslot : day.timeslots) {
                        for (ArrayList<Integer> sessionId : timeslot.sessionsId) {
                            Session session = sessionHashMap.get("" + sessionId.get(0));
                            talks.add(new Talk(session, timeslot.getTime(), tracks.get(session.getRoomId()).title));
                        }
                    }

                    day.talks = talks;
                }

                viewPager.setAdapter(new ProgramFragmentPagerAdapter(getChildFragmentManager(), days));
                hideMessageView();
                //mAdapter.notifyDataSetChanged();
                //hideMessageView();
            }

            @Override
            public void onCancelled(FirebaseData.ErrorFirebase errorType) {
                //TODO
                //String message = errorType == FirebaseData.INSTANCE.ErrorFirebase.network ? getString(R.string
                // .default_error_message) : getString(R.string.error_message_serveur_prob);
                //setMessageError(message);
            }
        };

    @Override
    public void retryOnProblem() {
        if (!isAdded()) {
            return;
        }
        FirebaseData.INSTANCE.getSessions(getActivity(), requestListenerSession);
    }

    private FirebaseData.RequestListener<HashMap<String, Session>> requestListenerSession =
        new FirebaseData.RequestListener<HashMap<String, Session>>() {
            @Override
            public void onDataChange(HashMap<String, Session> sessionMap) {
                sessionHashMap = sessionMap;
                FirebaseData.INSTANCE.getSchedule(getActivity(), requestListenerDays);
            }

            @Override
            public void onCancelled(@NotNull FirebaseData.ErrorFirebase errorType) {

            }
        };
}
