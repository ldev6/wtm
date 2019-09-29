package com.montreal.wtm.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Location;
import com.montreal.wtm.utils.MapUtils;
import com.montreal.wtm.utils.Utils;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;

public class LocationFragment extends BaseFragment {

    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    private ImageView mImageView;
    private TextView mPlaceNameTextView;
    private TextView mAddressTextView;
    private TextView mMetroTextView;
    private TextView mParkingTextView;
    private ImageView mParkingImageView;
    private ImageView metroIcon;
    private ImageView parkingIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment, container, false);
        mImageView = view.findViewById(R.id.mapImageView);
        mPlaceNameTextView = view.findViewById(R.id.placeNameTextView);
        mAddressTextView = view.findViewById(R.id.addressTextView);
        mMetroTextView = view.findViewById(R.id.metroInfoTextView);
        mParkingTextView = view.findViewById(R.id.parkingInfoTextView);
        mParkingImageView = view.findViewById(R.id.parkingImageView);
        metroIcon = view.findViewById(R.id.metro_icon);
        parkingIcon = view.findViewById(R.id.parking_icon);

        showProgressBar();

        FirebaseData.INSTANCE.getLocation(getActivity(), requestListener);
        return view;
    }

    private void updateView(final Location location) {
        String staticMapUrl = MapUtils.INSTANCE.urlForLocation(getString(R.string.map_api), location.getAddress());
        Utils.downloadImage(staticMapUrl, mImageView, R.drawable.placeholder_map);

        mPlaceNameTextView.setText(location.getName());
        mAddressTextView.setText(location.getAddress());
        mImageView.setOnClickListener(v -> {
            Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location.getAddress());
            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(mapIntent);
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                startActivity(intent);
            }
        });
        if (location.getMetro() != null) {
            mMetroTextView.setText(location.getMetro());
            metroIcon.setVisibility(View.VISIBLE);
        } else {
            metroIcon.setVisibility(View.INVISIBLE);
            mMetroTextView.setVisibility(View.GONE);
        }
        if (location.getParking() != null) {
            parkingIcon.setVisibility(View.VISIBLE);
            mParkingTextView.setText(location.getParking());
        } else {
            parkingIcon.setVisibility(View.INVISIBLE);
            mPlaceNameTextView.setVisibility(View.GONE);
        }

        if (location.getImageParkingUrl() != null) {
            Utils.downloadImage(location.getImageParkingUrl(), mParkingImageView);
            mParkingImageView.setOnClickListener(v -> {
                String url = location.getImageParkingUrl();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            });
        }
    }

    private FirebaseData.RequestListener<Location> requestListener = new FirebaseData.RequestListener<Location>() {
        @Override
        public void onDataChange(Location location) {
            hideMessageView();
            updateView(location);
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
        FirebaseData.INSTANCE.getLocation(getActivity(), requestListener);
    }
}
