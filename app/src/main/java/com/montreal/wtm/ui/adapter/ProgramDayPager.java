package com.montreal.wtm.ui.adapter;

import android.support.v4.app.Fragment;

import com.montreal.wtm.ui.fragment.ProgramDayFragment;
import com.montreal.wtm.ui.fragment.ProgramFragment;

/**
 * Created by laurencedevillers on 16-07-23.
 */

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
        return new ProgramFragment();
    }
}
