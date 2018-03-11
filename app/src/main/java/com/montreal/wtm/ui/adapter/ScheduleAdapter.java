package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.ui.activity.TalkActivity;
import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter {

    private static final int TYPE_BREAK = 0;
    private static final int TYPE_TALK = 1;

    private ArrayList<Talk> talks;
    private Context context;

    public ScheduleAdapter(Context context, ArrayList<Talk> talks) {
        this.talks = talks;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_BREAK:
                View viewHeader = LayoutInflater.from(context).inflate(R.layout.break_row, parent, false);
                return new BreakViewHolder(viewHeader);
            case TYPE_TALK:
                View viewTalk = LayoutInflater.from(context).inflate(R.layout.talk_row, parent, false);
                return new TalkViewHolder(viewTalk);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final Talk talk = talks.get(position);

        if (holder instanceof BreakViewHolder) {
            BreakViewHolder headerHolder = (BreakViewHolder) holder;

            if (talk.getType().equals(Talk.Type.Break)) {
                headerHolder.iconImageView.setImageResource(R.drawable.ic_access_time_black_24dp);
            } else {
                headerHolder.iconImageView.setImageResource(R.drawable.ic_restaurant_menu_black_24dp);
            }

            headerHolder.titleTextView.setText(talk.getTime());
        } else if (holder instanceof TalkViewHolder) {
            final TalkViewHolder talkViewHolder = (TalkViewHolder) holder;

            if (talk.getSaved()) {
                talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_24px);
            } else {
                talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_empty_24px);
            }

            talkViewHolder.loveImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (talk.getSaved()) {
                        talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_empty_24px);
                    } else {
                        talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_24px);
                    }
                    talk.setSaved(!talk.getSaved());
                    FirebaseData.INSTANCE.saveSession(talk.getSessionId(), talk.getSaved());
                }
            });

            talkViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(TalkActivity.newIntent(context, talk));
                }
            });
            talkViewHolder.timeTextView.setText(talk.getTime());
            talkViewHolder.roomTextView.setText(talk.getRoom());
            talkViewHolder.talkTitleTextView.setText(talk.getSession().getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (talks.get(position).getType().equals(Talk.Type.Break) || talks.get(position)
            .getType()
            .equals(Talk.Type.Food)) ? TYPE_BREAK : TYPE_TALK;
    }

    @Override
    public int getItemCount() {
        return talks.size();
    }

    class BreakViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        ImageView iconImageView;

        public BreakViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.speaker_position);
            iconImageView = itemView.findViewById(R.id.iconImageView);
        }
    }

    class TalkViewHolder extends RecyclerView.ViewHolder {

        ImageView loveImageView;
        TextView timeTextView;
        TextView roomTextView;
        TextView talkTitleTextView;

        public TalkViewHolder(View itemView) {
            super(itemView);
            loveImageView = itemView.findViewById(R.id.loveImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            roomTextView = itemView.findViewById(R.id.roomTextView);
            talkTitleTextView = itemView.findViewById(R.id.talkTitleTextView);
        }
    }
}
