package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.montreal.wtm.R;
import com.montreal.wtm.ui.adapter.ProgramDayPager;
import com.montreal.wtm.ui.adapter.ProgramFragmentPagerAdapter;

import java.util.ArrayList;


public class ProgramFragment extends Fragment {

    public static ProgramFragment newInstance() {
        ProgramFragment fragment = new ProgramFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.program_fragment, container, false);
        ViewPager viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        TabLayout tabLayout = (TabLayout) v.findViewById(R.id.sliding_tabs);
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setupWithViewPager(viewPager);

        ArrayList<ProgramDayPager> programDayPagers = new ArrayList<>();
        programDayPagers.add(new ProgramDayPager("April 1st", 1));
        programDayPagers.add(new ProgramDayPager("April 2nd", 2));
        viewPager.setAdapter(new ProgramFragmentPagerAdapter(getChildFragmentManager(),
                getActivity(), programDayPagers));
        return v;
    }
}
