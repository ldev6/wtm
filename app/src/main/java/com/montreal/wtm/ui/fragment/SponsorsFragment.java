package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.google.firebase.database.DatabaseError;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Sponsor;
import com.montreal.wtm.ui.adapter.SponsorsGridViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class SponsorsFragment extends Fragment {

    public static SponsorsFragment newInstance() {
        return new SponsorsFragment();
    }


    private GridView mSponsorPlatinumGridView;
    private GridView mSponsorGoldGridView;
    private GridView mSponsorSilverGridView;
    private GridView mSponsorBronzeGridView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sponsors_fragment, container, false);

        mSponsorPlatinumGridView = (GridView) v.findViewById(R.id.sponsorPlatinumGridView);
        mSponsorGoldGridView = (GridView) v.findViewById(R.id.sponsorGoldGridView);
        mSponsorSilverGridView = (GridView) v.findViewById(R.id.sponsorSilverGridView);
        mSponsorBronzeGridView = (GridView) v.findViewById(R.id.sponsorBronzeGridView);
        FirebaseData.getSponsors(requestListener);
        return v;
    }


    private FirebaseData.RequestListener<HashMap<String, ArrayList<Sponsor>>> requestListener = new FirebaseData.RequestListener<HashMap<String, ArrayList<Sponsor>>>() {

        @Override
        public void onDataChange(HashMap<String, ArrayList<Sponsor>> hashMapSponsors) {
            mSponsorPlatinumGridView.setAdapter(new SponsorsGridViewAdapter(getActivity(), hashMapSponsors.get(getActivity().getString(R.string.platinum)), SponsorsGridViewAdapter.SponsorCategory.PLATINUM));
            mSponsorGoldGridView.setAdapter(new SponsorsGridViewAdapter(getActivity(), hashMapSponsors.get(getActivity().getString(R.string.gold)), SponsorsGridViewAdapter.SponsorCategory.GOLD));
            mSponsorSilverGridView.setAdapter(new SponsorsGridViewAdapter(getActivity(), hashMapSponsors.get(getActivity().getString(R.string.silver)), SponsorsGridViewAdapter.SponsorCategory.SILVER));
            mSponsorBronzeGridView.setAdapter(new SponsorsGridViewAdapter(getActivity(), hashMapSponsors.get(getActivity().getString(R.string.bronze)), SponsorsGridViewAdapter.SponsorCategory.BRONZE));
            getView().invalidate();
        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    };


}
