package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by laurencedevillers on 16-07-23.
 */

public class ProgramFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private ArrayList<ProgramDayPager> programDayPagers;


    public ProgramFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<ProgramDayPager> programDayPagers) {
        super(fm);
        this.context = context;
        this.programDayPagers = programDayPagers;
    }

    @Override
    public Fragment getItem(int position) {
        return programDayPagers.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return programDayPagers.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return programDayPagers.get(position).getTitle();
    }
}
