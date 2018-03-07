package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.montreal.wtm.R;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.utils.Utils;
import java.util.ArrayList;
import java.util.List;

public class SpeakerAdapter extends BaseAdapter {

    protected List<Speaker> speakerList = new ArrayList<>();

    @Override
    public int getCount() {
        return speakerList.size();
    }

    @Override
    public Speaker getItem(int position) {
        return speakerList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return speakerList.get(position).hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =
            (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SpeakerHolder holder;
        if (convertView == null || convertView.getTag() != null || !(convertView.getTag() instanceof SpeakerHolder)) {
            convertView = inflater.inflate(R.layout.item_speaker, null);
            holder = new SpeakerHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (SpeakerHolder) convertView.getTag();
        }
        Speaker speaker = getItem(position);
        holder.setSpeakerInfo(speaker);
        return convertView;
    }

    public void addSpeaker(Speaker speaker) {
        speakerList.add(speaker);
    }

    protected static class SpeakerHolder {
        protected TextView speakerName;
        protected TextView speakerLocation;
        protected ImageView speakerAvatar;

        public SpeakerHolder(View content) {
            speakerName = content.findViewById(R.id.speaker_name);
            speakerAvatar = content.findViewById(R.id.speaker_image);
            speakerLocation = content.findViewById(R.id.speaker_location);
        }

        public void setSpeakerInfo(Speaker speaker) {
            speakerName.setText(speaker.getName());
            speakerLocation.setText(speaker.getTitle());
            Utils.downloadImage(speaker.getPhotoUrl(), speakerAvatar);
        }
    }
}
