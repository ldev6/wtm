package com.montreal.wtm.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Logo;
import com.montreal.wtm.model.Partner;
import com.montreal.wtm.ui.adapter.SponsorAdapter;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;
import java.util.HashMap;

public class PartnersFragment extends BaseFragment {

    private RecyclerView recyclerView;
    private SponsorAdapter adapter;

    public static PartnersFragment newInstance() {
        return new PartnersFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sponsors_fragment, container, false);
        recyclerView = v.findViewById(R.id.recyclerView);
        adapter = new SponsorAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(v.getContext()));
        FirebaseData.INSTANCE.getPartners(getActivity(), requestListener);
        showProgressBar();
        return v;
    }

    private FirebaseData.RequestListener<HashMap<Integer, Partner>> requestListener =
        new FirebaseData.RequestListener<HashMap<Integer, Partner>>() {

            @Override
            public void onDataChange(HashMap<Integer, Partner> partnerCategories) {
                if (!isAdded()) {
                    return;
                }

                for (Partner partnerCategory : partnerCategories.values()) {
                    adapter.addTitle(partnerCategory.getTitle());
                    int size = getSize(partnerCategory.getTitle());
                    if (getType(partnerCategory.getTitle()) == SponsorAdapter.SMALL) {
                        Pair<Logo, Logo> logos;
                        for (int i = 0; i < ((partnerCategory.getLogos().size()+1) / 2); i += 2) {
                            if (i < (partnerCategory.getLogos().size() - 1)) {
                                logos = new Pair<>(partnerCategory.getLogos().get(i),
                                    partnerCategory.getLogos().get(i + 1));
                                adapter.addLogos(logos, size);
                            } else {
                                adapter.addLogo(partnerCategory.getLogos().get(i), size);
                            }
                        }
                    } else {
                        for (Logo logo : partnerCategory.getLogos()) {
                            adapter.addLogo(logo, size);
                        }
                    }
                }

                hideMessageView();
                adapter.notifyDataSetChanged();
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
        FirebaseData.INSTANCE.getPartners(getActivity(), requestListener);
    }

    private int getType(String title) {
        if (title.toLowerCase().contains(getString(R.string.platinum)) || title.toLowerCase()
            .contains(getString(R.string.gold))) {
            return SponsorAdapter.LARGE;
        } else {
            return SponsorAdapter.SMALL;
        }
    }

    private int getSize(String title) {
        if (title.toLowerCase().contains(getString(R.string.platinum))) {
            return getContext().getResources().getDimensionPixelSize(R.dimen.platinum_size);
        } else if (title.toLowerCase().contains(getString(R.string.gold))) {
            return getContext().getResources().getDimensionPixelSize(R.dimen.gold_size);
        } else if (title.toLowerCase().contains(getString(R.string.silver))) {
            return getContext().getResources().getDimensionPixelSize(R.dimen.silver_size);
        } else if (title.toLowerCase().contains(getString(R.string.bronze))) {
            return getContext().getResources().getDimensionPixelSize(R.dimen.bronze_size);
        } else {
            return getContext().getResources().getDimensionPixelSize(R.dimen.bronze_size);
        }
    }

}
