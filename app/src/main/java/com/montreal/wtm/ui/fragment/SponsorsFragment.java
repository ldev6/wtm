package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseError;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Sponsor;
import com.montreal.wtm.ui.adapter.SponsorsAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class SponsorsFragment extends Fragment {

    public static SponsorsFragment newInstance() {
        return new SponsorsFragment();
    }


    private SponsorsAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.simple_recycler_list, container, false);
        RecyclerView recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new SponsorsAdapter(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(mAdapter);

        FirebaseData.getSponsors(requestListener);
        return v;
    }


    private FirebaseData.RequestListener<HashMap<String, ArrayList<Sponsor>>> requestListener = new FirebaseData.RequestListener<HashMap<String, ArrayList<Sponsor>>>() {

        @Override
        public void onDataChange(HashMap<String, ArrayList<Sponsor>> object) {
            mAdapter.updateList(object);
        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    };

}
