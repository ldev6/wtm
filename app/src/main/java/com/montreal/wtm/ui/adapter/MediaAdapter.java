package com.montreal.wtm.ui.adapter;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.net.Uri;
import androidx.annotation.ColorInt;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.montreal.wtm.R;
import com.montreal.wtm.model.Social;
import java.util.List;

public class MediaAdapter extends RecyclerView.Adapter {
    protected static final String LINKEDIN = "linkedin";
    protected static final String GITHUB = "github";
    protected static final String WEBSITE = "website";
    protected static final String TWITTER = "twitter";
    protected static final String FACEBOOK = "facebook";

    protected List<Social> media;
    @ColorInt
    protected int iconColor;

    public MediaAdapter(List<Social> socials) {
        this.media = socials;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        iconColor = ContextCompat.getColor(parent.getContext(), R.color.transGrey);
        return new MediaViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.media_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MediaViewHolder viewHolder = (MediaViewHolder) holder;
        Social social = media.get(position);
        int resource = R.drawable.ic_globe;
        switch (social.icon) {
            case LINKEDIN:
                resource = R.drawable.ic_linkedin;
                break;
            case FACEBOOK:
                resource = R.drawable.ic_fb;
                break;
            case TWITTER:
                resource = R.drawable.ic_twitter;
                break;
            case GITHUB:
                resource = R.drawable.ic_github;
        }
        viewHolder.mediaIcon.setImageResource(resource);
        viewHolder.mediaIcon.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(social.getLink()));
            view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return media == null ? 0 : media.size();
    }

    public class MediaViewHolder extends RecyclerView.ViewHolder {
        public ImageView mediaIcon;

        public MediaViewHolder(View itemView) {
            super(itemView);
            mediaIcon = itemView.findViewById(R.id.media_img);
            ImageViewCompat.setImageTintList(mediaIcon, ColorStateList.valueOf(iconColor));
            ImageViewCompat.setImageTintMode(mediaIcon, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
