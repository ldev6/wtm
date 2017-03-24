package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.montreal.wtm.R;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.ui.activity.SpeakerActivity;
import com.montreal.wtm.utils.Utils;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class SpeakersAdapter extends RecyclerView.Adapter<SpeakersAdapter.SpeakerHolder> {

    private Context mContext;
    private ArrayList<Map.Entry> mData;

    public SpeakersAdapter(Context context, HashMap<String, Speaker> speakerHashMap) {
        mData = new ArrayList<Map.Entry>();
        mData.addAll(speakerHashMap.entrySet());
        mContext = context;
    }

    @Override
    public SpeakerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.speaker_row, parent, false);
        return new SpeakerHolder(v);
    }

    @Override
    public void onBindViewHolder(SpeakerHolder holder, int position) {
        final Map.Entry<String, Speaker> speakerHasmap = mData.get(position);
        final Speaker speaker = speakerHasmap.getValue();
        holder.nameTextView.setText(speaker.getName());
        holder.titleTextView.setText(speaker.getTitle() != null ? Html.fromHtml(speaker.getTitle()) : null);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(mContext.getResources().getString(R.string.speakers_url))
                .append("%2F")
                .append(mContext.getResources().getString(R.string.speakers_url_end, speakerHasmap.getKey()));

        Utils.downloadImage(stringBuilder.toString(), holder.avatarImageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.startActivity(SpeakerActivity.newIntent(mContext, speaker, speakerHasmap.getKey()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void dataChange(HashMap<String, Speaker> speakerHashMap) {
        mData.clear();
        mData.addAll(speakerHashMap.entrySet());


        Collections.sort(mData, new Comparator<Map.Entry>() {
            @Override
            public int compare(Map.Entry o1, Map.Entry o2) {
                Speaker speaker1 = (Speaker) o1.getValue();
                Speaker speaker2 = (Speaker) o2.getValue();
                String firstName1 = speaker1.getFirstName() != null ? speaker1.getFirstName().toUpperCase() : speaker1.getFirstName();
                String firstName2 = speaker2.getFirstName() != null ? speaker2.getFirstName().toUpperCase() : speaker2.getFirstName();
                if (firstName1 != null && firstName2 != null) {
                    return firstName1.compareTo(firstName2);
                } else {
                    return 1;
                }
            }
        });


        notifyDataSetChanged();
    }

    public class SpeakerHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView titleTextView;
        ImageView avatarImageView;

        public SpeakerHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            avatarImageView = (ImageView) itemView.findViewById(R.id.avatarImageView);
        }
    }
}
