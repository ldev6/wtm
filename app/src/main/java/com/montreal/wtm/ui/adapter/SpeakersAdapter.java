package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;
import com.montreal.wtm.R;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.ui.activity.SpeakerActivity;
import com.montreal.wtm.ui.fragment.ProgramFragmentDirections;
import com.montreal.wtm.ui.fragment.SpeakersFragmentDirections;
import com.montreal.wtm.utils.Utils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class SpeakersAdapter extends RecyclerView.Adapter<SpeakersAdapter.SpeakerHolder> {

    private Context context;
    private ArrayList<Speaker> data;

    public SpeakersAdapter(Context context, HashMap<String, Speaker> speakerHashMap) {
        data = new ArrayList<>();
        data.addAll(speakerHashMap.values());
        this.context = context;
    }

    public SpeakersAdapter(Context context) {
        data = new ArrayList<>();
        this.context = context;
    }

    @Override
    public SpeakerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.speaker_row, parent, false);
        return new SpeakerHolder(v);
    }

    @Override
    public void onBindViewHolder(SpeakerHolder holder, int position) {
        final Speaker speaker = data.get(position);
        holder.nameTextView.setText(speaker.getName());
        holder.titleTextView.setText(speaker.getTitle() != null ? Html.fromHtml(speaker.getTitle()) : null);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(context.getResources().getString(R.string.storage_url)).append(speaker.getPhotoUrl());

        Utils.downloadImage(stringBuilder.toString(), holder.avatarImageView);
        holder.itemView.setOnClickListener(v -> {
                SpeakersFragmentDirections.ActionNavSpeakersToSpeakerInfo action = SpeakersFragmentDirections.actionNavSpeakersToSpeakerInfo(speaker);
                Navigation.findNavController(v).navigate(action);
            }
            );
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void dataChange(HashMap<String, Speaker> speakerHashMap) {
        data.clear();
        data.addAll(speakerHashMap.values());

        Collections.sort(data, (speaker1, speaker2) -> {
            String firstName1 = speaker1.getName() != null ? speaker1.getName().toUpperCase() : speaker1.getName();
            String firstName2 = speaker2.getName() != null ? speaker2.getName().toUpperCase() : speaker2.getName();
            if (firstName1 != null && firstName2 != null) {
                return firstName1.compareTo(firstName2);
            } else {
                return 1;
            }
        });

        notifyDataSetChanged();
    }

    public void addSpeaker(Speaker speaker) {
        data.add(speaker);
    }

    public class SpeakerHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        TextView titleTextView;
        ImageView avatarImageView;

        public SpeakerHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            titleTextView = itemView.findViewById(R.id.speaker_position);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
        }
    }
}
