package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

    private static final String EXTRA_DAY = "EXTRA_DAY";
    private static final String EXTRA_SAVED_SESSIONS = "SAVED_SESSIONS";

    private ScheduleAdapter adapter;
    private Day day;

    public static ProgramDayFragment newInstance(Day day, HashMap<String, Boolean> savedSessions) {

        Bundle args = new Bundle();
        args.putSerializable(EXTRA_SAVED_SESSIONS, savedSessions);
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
        return v;
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        RecyclerView recyclerView = v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration =
            new DividerItemDecoration(getActivity(), linearLayoutManager.getOrientation());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(dividerItemDecoration);
        ArrayList<Talk> talks = new ArrayList<>();
        if (getArguments() != null) {
            day = getArguments().getParcelable(EXTRA_DAY);

        }
        talks.addAll(day.getTalks());
        adapter = new ScheduleAdapter(getActivity(), talks);
        if(getArguments() != null) {
            updateSavedSessions((HashMap<String, Boolean>) getArguments().getSerializable(EXTRA_SAVED_SESSIONS));
        }
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void updateSavedSessions(HashMap<String, Boolean> savedSessions) {
        if (day != null && adapter != null) {
            for (Talk talk : day.getTalks()) {
                talk.setSaved(talk.getSession().isIncludedIn(savedSessions));
            }
            adapter.updateTalks(day.getTalks());
            adapter.notifyDataSetChanged();
        }
    }
}
