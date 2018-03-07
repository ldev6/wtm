package com.montreal.wtm.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.montreal.wtm.model.Day;
import com.montreal.wtm.ui.fragment.ProgramDayFragment;
import java.util.ArrayList;

public class ProgramFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Day> days;

    public ProgramFragmentPagerAdapter(FragmentManager fm, ArrayList<Day> days) {
        super(fm);
        this.days = days;
    }

    @Override
    public Fragment getItem(int position) {
        Day day = days.get(position);
        return ProgramDayFragment.newInstance(day);
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return days.get(position).getDateReadable();
    }
}
