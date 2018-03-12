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
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.ui.adapter.SpeakersAdapter;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;
import java.util.HashMap;

public class SpeakersFragment extends BaseFragment {

    public static Fragment newIntance() {
        return new SpeakersFragment();
    }

    private HashMap<String, Speaker> speakerHashMap;
    private SpeakersAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simple_recycler_list, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        DividerItemDecoration dividerItemDecoration =
            new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setLayoutManager(linearLayoutManager);
        speakerHashMap = new HashMap();
        mAdapter = new SpeakersAdapter(getContext(), speakerHashMap);
        recyclerView.setAdapter(mAdapter);
        showProgressBar();
        FirebaseData.INSTANCE.getSpeakers(getActivity(), speakersRequestListener);
        return view;
    }

    private FirebaseData.RequestListener speakersRequestListener =
        new FirebaseData.RequestListener<HashMap<String, Speaker>>() {

            @Override
            public void onDataChange(HashMap<String, Speaker> object) {
                speakerHashMap = object;
                mAdapter.dataChange(object);
                hideMessageView();
            }

            @Override
            public void onCancelled(FirebaseData.ErrorFirebase errorType) {
            }
        };

    @Override
    public void retryOnProblem() {
        if (!isAdded()) {
            return;
        }
        FirebaseData.INSTANCE.getSpeakers(getActivity(), speakersRequestListener);
    }
}
