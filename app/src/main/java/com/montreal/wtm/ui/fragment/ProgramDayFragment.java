package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.montreal.wtm.R;

/**
 * Created by laurencedevillers on 16-07-23.
 */

public class ProgramDayFragment extends Fragment {

    private static String EXTRA_DAY = "EXTRA_DAY";

    private TextView tvProgramDay;

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

        int day = getArguments().getInt(EXTRA_DAY);
        tvProgramDay = (TextView) v.findViewById(R.id.tvProgramDay);
        tvProgramDay.setText(day);
        return v;
    }
}
