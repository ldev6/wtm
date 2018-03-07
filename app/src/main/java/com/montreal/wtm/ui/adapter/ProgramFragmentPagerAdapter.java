package com.montreal.wtm.ui.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.montreal.wtm.model.Day;
import com.montreal.wtm.ui.fragment.ProgramDayFragment;
import java.util.ArrayList;

public class ProgramFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Day> days;
    private ArrayList<ProgramDayFragment> fragments;

    public ProgramFragmentPagerAdapter(FragmentManager fm, ArrayList<Day> days) {
        super(fm);
        this.days = days;
        fragments = new ArrayList<ProgramDayFragment>();

        for (Day day : days) {
            fragments.add(ProgramDayFragment.newInstance(day));
        }
    }

    @Override
    public ProgramDayFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return days.get(position).dateReadable;
    }
}
