package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Sponsor;
import com.montreal.wtm.ui.adapter.SponsorsGridViewAdapter;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.HashMap;


public class SponsorsFragment extends BaseFragment {

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
        FirebaseData.getSponsors(getActivity(), requestListener);
        showProgressBar();
        return v;
    }


    private FirebaseData.RequestListener<HashMap<String, ArrayList<Sponsor>>> requestListener = new FirebaseData.RequestListener<HashMap<String, ArrayList<Sponsor>>>() {

        @Override
        public void onDataChange(HashMap<String, ArrayList<Sponsor>> hashMapSponsors) {
            if (!isAdded()) {
                return;
            }
            mSponsorPlatinumGridView.setAdapter(new SponsorsGridViewAdapter(getActivity(), hashMapSponsors.get(getActivity().getString(R.string.platinum)), SponsorsGridViewAdapter.SponsorCategory.PLATINUM));
            mSponsorGoldGridView.setAdapter(new SponsorsGridViewAdapter(getActivity(), hashMapSponsors.get(getActivity().getString(R.string.gold)), SponsorsGridViewAdapter.SponsorCategory.GOLD));
            mSponsorSilverGridView.setAdapter(new SponsorsGridViewAdapter(getActivity(), hashMapSponsors.get(getActivity().getString(R.string.silver)), SponsorsGridViewAdapter.SponsorCategory.SILVER));
            mSponsorBronzeGridView.setAdapter(new SponsorsGridViewAdapter(getActivity(), hashMapSponsors.get(getActivity().getString(R.string.bronze)), SponsorsGridViewAdapter.SponsorCategory.BRONZE));
            hideMessageView();
            getView().invalidate();
        }

        @Override
        public void onCancelled(FirebaseData.ErrorFirebase errorType) {
            String message = errorType == FirebaseData.ErrorFirebase.network ? getString(R.string.default_error_message) : getString(R.string.error_message_serveur_prob);
            setMessageError(message);
        }
    };


    @Override
    public void retryOnProblem() {
        if (!isAdded()) {
            return;
        }
        FirebaseData.getSponsors(getActivity(), requestListener);
    }
}
