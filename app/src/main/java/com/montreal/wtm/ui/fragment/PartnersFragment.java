package com.montreal.wtm.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Partner;
import com.montreal.wtm.ui.adapter.SponsorsGridViewAdapter;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;
import java.util.HashMap;

public class PartnersFragment extends BaseFragment {

    public static PartnersFragment newInstance() {
        return new PartnersFragment();
    }

    private LinearLayout layoutContainer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.sponsors_fragment, container, false);
        layoutContainer = v.findViewById(R.id.container);
        FirebaseData.INSTANCE.getPartners(getActivity(), requestListener);
        showProgressBar();
        return v;
    }

    private FirebaseData.RequestListener<HashMap<Integer, Partner>> requestListener =
        new FirebaseData.RequestListener<HashMap<Integer, Partner>>() {

            @Override
            public void onDataChange(HashMap<Integer, Partner> partnerCategoryies) {
                if (!isAdded()) {
                    return;
                }

                for (Partner partner : partnerCategoryies.values()) {

                    View partnerView = getLayoutInflater().inflate(R.layout.partner_row, null);
                    TextView title = partnerView.findViewById(R.id.partnerTitle);
                    title.setText(partner.getTitle());
                    title.setTextColor(getColor(partner.getTitle()));

                    GridView partnerGridview = partnerView.findViewById(R.id.sponsorGridView);
                    partnerGridview.setAdapter(
                        new SponsorsGridViewAdapter(getActivity(), partner, getSize(partner.getTitle())));

                    layoutContainer.addView(partnerView);
                }

                hideMessageView();
                getView().invalidate();
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

    private int getColor(String title) {
        if (title.toLowerCase().contains(getString(R.string.platinum))) {
            return R.color.platinum;
        } else if (title.toLowerCase().contains(getString(R.string.gold))) {
            return R.color.gold;
        } else if (title.toLowerCase().contains(getString(R.string.silver))) {
            return R.color.silver;
        } else if (title.toLowerCase().contains(getString(R.string.bronze))) {
            return R.color.bronze;
        } else {
            return Color.BLACK;
        }
    }
}
