package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.montreal.wtm.R;
import com.montreal.wtm.model.AdapterData;
import com.montreal.wtm.model.Sponsor;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;


public class SponsorsAdapter extends RecyclerView.Adapter {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_SPONSOR = 1;

    private Context mContext;
    private ArrayList<AdapterData> mData;

    public SponsorsAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View viewHeader = LayoutInflater.from(mContext).inflate(R.layout.header_row, parent, false);
                return new HeaderHolder(viewHeader);
            case TYPE_SPONSOR:
                View viewSponsor = LayoutInflater.from(mContext).inflate(R.layout.sponsor_row, parent, false);
                return new SponsorHolder(viewSponsor);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        AdapterData adapterData = mData.get(position);
        if (holder instanceof HeaderHolder) {
            HeaderHolder headerHolder = (HeaderHolder) holder;
            headerHolder.titleCategoryTextView.setText(adapterData.getData().toString());
        } else {
            SponsorHolder sponsorHolder = (SponsorHolder) holder;
            final Sponsor sponsor = (Sponsor) adapterData.getData();

            sponsorHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(sponsor.getUrlWebsite());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(intent);
                }
            });

            sponsorHolder.imageView.setContentDescription(sponsor.getName());

            StringBuilder stringBuilder = new StringBuilder().append(mContext.getResources().getString(R.string.sponsors_url))
                    .append("%2F")
                    .append(mContext.getResources().getString(R.string.sponsors_url_end, sponsor.getImageKey()));

            Picasso.with(mContext).load(stringBuilder.toString())
                    .into(sponsorHolder.imageView);
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    private class SponsorHolder extends RecyclerView.ViewHolder {

        ImageView imageView;


        SponsorHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public void updateList(HashMap<String, ArrayList<Sponsor>> hashMapSponsors) {
        mData = new ArrayList<>();
        mData.add(new AdapterData(TYPE_HEADER, mContext.getString(R.string.platinum).toUpperCase()));
        for (Sponsor sponsor : hashMapSponsors.get(mContext.getString(R.string.platinum))) {
            mData.add(new AdapterData(TYPE_SPONSOR, sponsor));
        }
        mData.add(new AdapterData(TYPE_HEADER, mContext.getString(R.string.gold).toUpperCase()));
        for (Sponsor sponsor : hashMapSponsors.get(mContext.getString(R.string.gold))) {
            mData.add(new AdapterData(TYPE_SPONSOR, sponsor));
        }
        mData.add(new AdapterData(TYPE_HEADER, mContext.getString(R.string.silver).toUpperCase()));
        for (Sponsor sponsor : hashMapSponsors.get(mContext.getString(R.string.silver))) {
            mData.add(new AdapterData(TYPE_SPONSOR, sponsor));
        }
        mData.add(new AdapterData(TYPE_HEADER, mContext.getString(R.string.bronze).toUpperCase()));
        for (Sponsor sponsor : hashMapSponsors.get(mContext.getString(R.string.bronze))) {
            mData.add(new AdapterData(TYPE_SPONSOR, sponsor));
        }
        notifyDataSetChanged();
    }

    private class HeaderHolder extends RecyclerView.ViewHolder {

        TextView titleCategoryTextView;

        HeaderHolder(View itemView) {
            super(itemView);
            titleCategoryTextView = (TextView) itemView.findViewById(R.id.sponsorCategoryTitle);
        }
    }
}
