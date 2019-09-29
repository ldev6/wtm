package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.auth.FirebaseWrapper;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.ui.fragment.ProgramFragmentDirections;
import java.util.ArrayList;

public class ScheduleAdapter extends RecyclerView.Adapter {

    private static final int TYPE_BREAK = 0;
    private static final int TYPE_TALK = 1;
    private final boolean updatable;

    private ArrayList<Talk> talks;
    private Context context;

    public ScheduleAdapter(Context context, ArrayList<Talk> talks) {
        this.talks = talks;
        this.context = context;
        this.updatable = true;
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
            if (talk.getType() == Talk.Type.General) {
                talkViewHolder.trackView.setVisibility(View.INVISIBLE);
                talkViewHolder.talkLocale.setVisibility(View.GONE);
            } else {
                talkViewHolder.talkLocale.setVisibility(View.VISIBLE);
                talkViewHolder.talkLocale.setText(talk.getSession().getLanguage());
                int trackColor = R.color.soft_skill;
                if (talk.getSession().isTechnicalTalk()) {
                    trackColor = R.color.technical_skill;
                }
                talkViewHolder.trackView.setVisibility(View.VISIBLE);
                talkViewHolder.trackView.setBackgroundColor(
                    talkViewHolder.trackView.getResources().getColor(trackColor));
            }

            if (updatable && FirebaseWrapper.Companion.isLogged()) {
                talkViewHolder.loveImageView.setOnClickListener(v -> {
                    if (talk.getSaved()) {
                        talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_empty_24px);
                    } else {
                        talkViewHolder.loveImageView.setImageResource(R.drawable.ic_favorite_black_24px);
                    }

                    talk.setSaved(!talk.getSaved());
                    FirebaseData.INSTANCE.saveSession(talk.getSessionId(), talk.getSaved());
                });
            }

            talkViewHolder.itemView.setOnClickListener(v -> {
                //Bundle bundle = new Bundle();
                //bundle.putParcelable(EXTRA_TALK, talk);
                Navigation.findNavController(v).navigate(R.id.action_nav_program_to_talkFragment);
                //ProgramFragmentDirections.ActionNavProgramToTalkFragment action =
                //    ProgramFragmentDirections.actionNavProgramToTalkFragment(talk);
                //Navigation.findNavController(v).navigate(action);

                
                //context.startActivity(TalkActivity.newIntent(context, talk)

            });
            talkViewHolder.timeTextView.setText(talk.getTime());
            talkViewHolder.roomTextView.setText(talk.getRoom());
            talkViewHolder.talkTitleTextView.setText(talk.getSession().getTitle());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (talks.get(position).getType() == Talk.Type.Break || talks.get(position).getType() == Talk.Type.Food)
            ? TYPE_BREAK : TYPE_TALK;
    }

    @Override
    public int getItemCount() {
        return talks.size();
    }

    public void updateTalks(ArrayList<Talk> talks) {
        this.talks = talks;
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
        TextView talkLocale;
        View trackView;

        public TalkViewHolder(View itemView) {
            super(itemView);
            loveImageView = itemView.findViewById(R.id.loveImageView);
            timeTextView = itemView.findViewById(R.id.timeTextView);
            roomTextView = itemView.findViewById(R.id.roomTextView);
            talkTitleTextView = itemView.findViewById(R.id.talkTitleTextView);
            trackView = itemView.findViewById(R.id.track);
            talkLocale = itemView.findViewById(R.id.localeTextView);
        }
    }
}
