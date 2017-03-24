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
import com.montreal.wtm.model.Sponsor;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.montreal.wtm.ui.adapter.SponsorsGridViewAdapter.SponsorCategory.PLATINUM;


public class SponsorsGridViewAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<Sponsor> mSponsor;
    private SponsorCategory mCategory;

    public enum SponsorCategory {
        PLATINUM, GOLD, SILVER, BRONZE
    }

    public SponsorsGridViewAdapter(Context context, ArrayList<Sponsor> sponsors, SponsorCategory category) {
        mContext = context;
        mSponsor = sponsors;
        mCategory = category;
    }

    @Override
    public int getCount() {
        return mSponsor.size();
    }

    @Override
    public Sponsor getItem(int position) {
        return mSponsor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Sponsor sponsor = getItem(position);
        View view;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            view = inflater.inflate(R.layout.sponsor_row, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            switch (mCategory) {
                case PLATINUM:
                    int sizePlatinum = mContext.getResources().getDimensionPixelSize(R.dimen.platinum_size);
                    LinearLayout.LayoutParams layoutParamsPlatinum = new LinearLayout.LayoutParams(sizePlatinum, sizePlatinum);
                    imageView.setLayoutParams(layoutParamsPlatinum);
                    break;
                case GOLD:
                    int sizeGold= mContext.getResources().getDimensionPixelSize(R.dimen.gold_size);
                    LinearLayout.LayoutParams layoutParamsGold = new LinearLayout.LayoutParams(sizeGold, sizeGold);
                    imageView.setLayoutParams(layoutParamsGold);
                    break;
                case SILVER:
                    int sizeSilver= mContext.getResources().getDimensionPixelSize(R.dimen.silver_size);
                    LinearLayout.LayoutParams layoutParamsSilver = new LinearLayout.LayoutParams(sizeSilver, sizeSilver);
                    imageView.setLayoutParams(layoutParamsSilver);
                    break;
                case BRONZE:
                    int sizeBronze= mContext.getResources().getDimensionPixelSize(R.dimen.bronze_size);
                    LinearLayout.LayoutParams layoutParamsBronze = new LinearLayout.LayoutParams(sizeBronze, sizeBronze);
                    imageView.setLayoutParams(layoutParamsBronze);
                    break;
            }
            StringBuilder stringBuilder = new StringBuilder().append(mContext.getResources().getString(R.string.sponsors_url))
                    .append("%2F")
                    .append(mContext.getResources().getString(R.string.sponsors_url_end, sponsor.getImageKey()));

            Picasso.with(mContext).load(stringBuilder.toString())
                    .networkPolicy(NetworkPolicy.OFFLINE)
                    .into(imageView);

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
