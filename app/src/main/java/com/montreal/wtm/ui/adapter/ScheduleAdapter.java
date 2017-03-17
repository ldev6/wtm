package com.montreal.wtm.ui.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.montreal.wtm.R;

import com.montreal.wtm.model.DataManager;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.ui.activity.TalkActivity;

import java.util.ArrayList;


public class ScheduleAdapter extends RecyclerView.Adapter {

    private static final int TYPE_BREAK = 0;
    private static final int TYPE_TALK = 1;

    private ArrayList<Talk> mTalks;
    private Context mContext;


    public ScheduleAdapter(Context context, ArrayList<Talk> talks) {
        mTalks = talks;
        mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BREAK:
                View viewHeader = LayoutInflater.from(mContext).inflate(R.layout.break_row, parent, false);
                return new BreakViewHolder(viewHeader);
            case TYPE_TALK:
                View viewTalk = LayoutInflater.from(mContext).inflate(R.layout.talk_row, parent, false);
                return new TalkViewHolder(viewTalk);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Talk talk = mTalks.get(position);

        if (holder instanceof BreakViewHolder) {
            BreakViewHolder headerHolder = (BreakViewHolder) holder;
            if (talk.getType().equals(Talk.Type.Break.toString())) {
                headerHolder.iconImageView.setImageResource(R.drawable.ic_access_time_black_24dp);
            } else {
                headerHolder.iconImageView.setImageResource(R.drawable.ic_restaurant_menu_black_24dp);
            }
            headerHolder.titleTextView.setText(talk.getTime());
        } else if (holder instanceof TalkViewHolder) {
            final TalkViewHolder talkViewHolder = (TalkViewHolder) holder;
            if (talk.getSpeakerId() != null) {
                if (DataManager.getInstance().loveTalkContainSpeaker(talk.getSpeakerId())) {
                    talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_24px);
                } else {
                    talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_empty_24px);
                }
                talkViewHolder.loveImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (DataManager.getInstance().loveTalkContainSpeaker(talk.getSpeakerId())) {
                            DataManager.getInstance().removeLoveTalks(talk.getSpeakerId());
                            talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_empty_24px);
                        } else {
                            DataManager.getInstance().addLoveTalk(talk.getSpeakerId());
                            talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_24px);
                        }
                    }
                });
            }
            talkViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mContext.startActivity(TalkActivity.newIntent(mContext, talk));
                }
            });
            talkViewHolder.timeTextView.setText(talk.getTime());
            talkViewHolder.roomTextView.setText(talk.getRoom());
            talkViewHolder.talkTitleTextView.setText(talk.getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (mTalks.get(position).getType().equals(Talk.Type.Break.toString()) ||
                mTalks.get(position).getType().equals(Talk.Type.Food.toString())) ? TYPE_BREAK : TYPE_TALK;
    }

    @Override
    public int getItemCount() {
        return mTalks.size();
    }

    class BreakViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView iconImageView;

        public BreakViewHolder(View itemView) {
            super(itemView);
            titleTextView = (TextView) itemView.findViewById(R.id.titleTextView);
            iconImageView = (ImageView) itemView.findViewById(R.id.iconImageView);
        }
    }

    class TalkViewHolder extends RecyclerView.ViewHolder {

        ImageView loveImageView;
        TextView timeTextView;
        TextView roomTextView;
        TextView talkTitleTextView;

        public TalkViewHolder(View itemView) {
            super(itemView);
            loveImageView = (ImageView) itemView.findViewById(R.id.loveImageView);
            timeTextView = (TextView) itemView.findViewById(R.id.timeTextView);
            roomTextView = (TextView) itemView.findViewById(R.id.roomTextView);
            talkTitleTextView = (TextView) itemView.findViewById(R.id.talkTitleTextView);
        }
    }
}
