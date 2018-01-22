package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.montreal.wtm.R;
import com.montreal.wtm.model.PartnerCategory;
import com.montreal.wtm.model.Sponsor;
import com.montreal.wtm.utils.Utils;
import java.util.ArrayList;

public class SponsorsGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Sponsor> sponsors;
    private int size;

    public SponsorsGridViewAdapter(Context context, PartnerCategory partnerCategory, int size) {
        mContext = context;
        sponsors = partnerCategory.getSponsors();
        this.size = size;
    }

    @Override
    public int getCount() {
        return sponsors.size();
    }

    @Override
    public Sponsor getItem(int position) {
        return sponsors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Sponsor sponsor = getItem(position);
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = inflater.inflate(R.layout.sponsor_row, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            LinearLayout.LayoutParams layoutParamsPlatinum = new LinearLayout.LayoutParams(size, size);
            imageView.setLayoutParams(layoutParamsPlatinum);

            StringBuilder stringBuilder =
                new StringBuilder().append(mContext.getResources().getString(R.string.storage_url))
                    .append(sponsor.getLogoUrl());

            Utils.downloadImage(stringBuilder.toString(), imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(sponsor.getUrlWebsite());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    mContext.startActivity(intent);
                }
            });
            return view;
        } else {
            return convertView;
        }
    }
}
