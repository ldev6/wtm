package com.montreal.wtm.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.montreal.wtm.R;


public class InformationFragment extends Fragment {

    public static InformationFragment newInstance() {
        return new InformationFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.information_fragment, container, false);
        ((TextView) view.findViewById(R.id.descriptionTextView)).setText(Html.fromHtml(getString(R.string.wtm_description)));
        ((TextView)view.findViewById(R.id.montrealWebsiteTextView)).setText(Html.fromHtml(getString(R.string.wtm_montreal_website)));
        ((TextView)view.findViewById(R.id.globalWebsiteTextView)).setText(Html.fromHtml(getString(R.string.wtm_global_website)));
        return view;
    }
}
