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
import com.montreal.wtm.ui.activity.MainActivity;
import com.montreal.wtm.ui.adapter.SpeakersAdapter;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;
import com.montreal.wtm.utils.view.MessageView;

import java.util.HashMap;


public class SpeakersFragment extends BaseFragment {

    public static Fragment newIntance() {
        return new SpeakersFragment();
    }


    private HashMap<String, Speaker> mSpeakerHashMap;
    private SpeakersAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_recycler_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        mSpeakerHashMap = new HashMap<String, Speaker>();
        mAdapter = new SpeakersAdapter(getActivity(), mSpeakerHashMap);
        recyclerView.setAdapter(mAdapter);
        showProgressBar();
        FirebaseData.getSpeakers(getActivity(), speakersRequestListener);
        return view;
    }


    private FirebaseData.RequestListener speakersRequestListener = new FirebaseData.RequestListener<HashMap<String, Speaker>>() {

        @Override
        public void onDataChange(HashMap<String, Speaker> object) {
            mSpeakerHashMap = object;
            mAdapter.dataChange(object);
            hideMessageView();
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
        FirebaseData.getSpeakers(getActivity(), speakersRequestListener);
    }
}
