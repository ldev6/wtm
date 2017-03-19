package com.montreal.wtm.ui.adapter;

import android.support.v4.app.Fragment;

import com.montreal.wtm.ui.fragment.ProgramDayFragment;


public class ProgramDayPager<T> {

    private String title;
    private Fragment fragment;
    private int day;

    public ProgramDayPager(String title, int day) {
        this.title = title;
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public Fragment getFragment() {
        return ProgramDayFragment.newInstance(day);
    }
}
