package com.montreal.wtm.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseError;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.ui.adapter.SpeakersAdapter;

import java.util.HashMap;

import static com.montreal.wtm.R.id.recyclerView;


public class SpeakersFragment extends Fragment {

    public static Fragment newIntance() {
        return new SpeakersFragment();
    }

    private RecyclerView mRecyclerView;
    private HashMap<String, Speaker> mSpeakerHashMap;
    private SpeakersAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.speakers_fragment, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                linearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mSpeakerHashMap = new HashMap<String, Speaker>();
        mAdapter = new SpeakersAdapter(getActivity(), mSpeakerHashMap);
        mRecyclerView.setAdapter(mAdapter);
        FirebaseData.getSpeakers(speakersRequestListener);

        return view;
    }


    private FirebaseData.RequestListener speakersRequestListener = new FirebaseData.RequestListener<HashMap<String, Speaker>>() {

        @Override
        public void onDataChange(HashMap<String, Speaker> object) {
            mSpeakerHashMap = object;
            mAdapter.dataChange(object);
        }

        @Override
        public void onCancelled(DatabaseError error) {
            //TODO ERROR
        }
    };

}
