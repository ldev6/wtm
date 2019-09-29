package com.montreal.wtm.ui.adapter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import com.montreal.wtm.model.Day;
import com.montreal.wtm.ui.fragment.ProgramDayFragment;
import java.util.ArrayList;
import java.util.HashMap;

public class ProgramFragmentPagerAdapter extends FragmentPagerAdapter {

    private ArrayList<Day> days;
    private ArrayList<ProgramDayFragment> fragments;
    private HashMap<String, Boolean> savedSessions;

    public ProgramFragmentPagerAdapter(FragmentManager fm, ArrayList<Day> days) {
        super(fm);
        this.days = days;
        fragments = new ArrayList<>();
        savedSessions = new HashMap<>();
        for (Day day : days) {
            fragments.add(ProgramDayFragment.newInstance(day, savedSessions));
        }
    }

    public ProgramFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        this.days = new ArrayList<>();
        fragments = new ArrayList<>();
        savedSessions = new HashMap<>();
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
        return days.get(position).getDateReadable();
    }

    public void loadSavedSessions(HashMap<String, Boolean> mySchedule) {
        savedSessions.putAll(mySchedule);
    }

    public void updateDays(ArrayList<Day> days) {
        this.days = days;
        fragments = new ArrayList<>();
        for (Day day : days) {
            fragments.add(ProgramDayFragment.newInstance(day, savedSessions));
        }

    }
}
