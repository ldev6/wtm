package com.montreal.wtm.ui.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Location;
import com.montreal.wtm.utils.MapUtils;
import com.squareup.picasso.Picasso;

public class LocationFragment extends Fragment {

    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    private ImageView mImageView;
    private TextView mPlaceNameTextView;
    private TextView mAddressTextView;
    private TextView mMetroTextView;
    private TextView mParkingTextView;
    private ImageView mParkingImageView;
    private FloatingActionButton mFloatingAction;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.location_fragment, container, false);

        mImageView = (ImageView) view.findViewById(R.id.mapImageView);
        mPlaceNameTextView = (TextView) view.findViewById(R.id.placeNameTextView);
        mAddressTextView = (TextView) view.findViewById(R.id.addressTextView);
        mMetroTextView = (TextView) view.findViewById(R.id.metroInfoTextView);
        mParkingTextView = (TextView) view.findViewById(R.id.parkingInfoTextView);
        mParkingImageView = (ImageView) view.findViewById(R.id.parkingImageView);

        mFloatingAction = (FloatingActionButton) view.findViewById(R.id.fab);

        FirebaseData.getLocation(requestListener);
        return view;
    }

    private void updateView(final Location location) {
        String staticMapUrl = MapUtils.urlForLocation(location.getAddress());
        Picasso.with(getActivity()).load(staticMapUrl)
                .placeholder(R.drawable.placeholder_map)
                .into(mImageView);

        mPlaceNameTextView.setText(location.getName());
        mAddressTextView.setText(location.getAddress());
        mFloatingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + location.getAddress());
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Intent intent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    startActivity(intent);
                }
            }
        });
        if (location.getMetro() != null) {
            mMetroTextView.setText(location.getMetro());
        } else {
            mMetroTextView.setVisibility(View.GONE);
        }
        if (location.getParking() != null) {
            mParkingTextView.setText(location.getParking());
        } else {
            mPlaceNameTextView.setVisibility(View.GONE);
        }

        if (location.getImageParkingUrl() != null) {
            Picasso.with(getActivity()).load(location.getImageParkingUrl())
                    .into(mParkingImageView);
        }

    }

    private FirebaseData.RequestListener<Location> requestListener = new FirebaseData.RequestListener<Location>() {
        @Override
        public void onDataChange(Location location) {
            updateView(location);
        }

        @Override
        public void onCancelled(DatabaseError error) {

        }
    };
}
