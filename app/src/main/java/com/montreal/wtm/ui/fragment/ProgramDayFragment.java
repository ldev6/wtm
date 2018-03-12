package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.montreal.wtm.R;
import com.montreal.wtm.model.Day;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.ui.adapter.ScheduleAdapter;
import java.util.ArrayList;
import java.util.HashMap;

public class ProgramDayFragment extends Fragment {

    private static String EXTRA_DAY = "EXTRA_DAY";

    private ScheduleAdapter adapter;
    private Day day;

    public static ProgramDayFragment newInstance(Day day) {

        Bundle args = new Bundle();
        args.putParcelable(EXTRA_DAY, day);
        ProgramDayFragment fragment = new ProgramDayFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.program_day_fragment, container, false);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration =
            new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ArrayList<Talk> talks = new ArrayList<>();
        day = getArguments().getParcelable(EXTRA_DAY);
        talks.addAll(day.getTalks());
        adapter = new ScheduleAdapter(getActivity(), talks);
        recyclerView.setAdapter(adapter);

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void loadSavedSessions(HashMap<String, Boolean> savedSessions) {
        if (day != null) {
            for (Talk talk : day.getTalks()) {
                talk.setSaved(talk.getSession().isIncludedIn(savedSessions));
            }
            adapter.updateTalks(day.getTalks());
            adapter.notifyDataSetChanged();
        }
    }
}
