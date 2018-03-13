package com.montreal.wtm.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.montreal.wtm.R;
import com.montreal.wtm.model.Logo;
import com.montreal.wtm.utils.Utils;
import java.util.ArrayList;

public class SponsorAdapter extends RecyclerView.Adapter {

    public static final int LARGE = 1;
    public static final int SMALL = 2;

    public static final int TYPE_TITLE = 1;
    public static final int TYPE_LOGO = 2;
    public static final int TYPE_LOGOS = 3;

    private ArrayList<Object> logosAndTitles;
    private ArrayList<Integer> sizes;

    public SponsorAdapter() {
        logosAndTitles = new ArrayList<>();
        sizes = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_LOGO:
                return LogoViewHolder.create(parent);
            case TYPE_LOGOS:
                return LogosViewHolder.create(parent);
            case TYPE_TITLE:
            default:
                return TitleViewHolder.create(parent);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Context context = holder.itemView.getContext();
        if (holder instanceof LogosViewHolder) {

            LogosViewHolder viewHolder = ((LogosViewHolder) holder);

            StringBuilder stringBuilder =
                new StringBuilder().append(context.getResources().getString(R.string.storage_url))
                    .append(((Pair<Logo, Logo>) logosAndTitles.get(position)).first.getLogoUrl());
            Utils.downloadImage(stringBuilder.toString(), viewHolder.first);

            stringBuilder = new StringBuilder().append(context.getResources().getString(R.string.storage_url))
                .append(((Pair<Logo, Logo>) logosAndTitles.get(position)).second.getLogoUrl());
            Utils.downloadImage(stringBuilder.toString(), viewHolder.second);
            viewHolder.setImageSize(sizes.get(position));
        } else if (holder instanceof LogoViewHolder) {

            LogoViewHolder viewHolder = ((LogoViewHolder) holder);

            StringBuilder stringBuilder =
                new StringBuilder().append(context.getResources().getString(R.string.storage_url))
                    .append(((Logo) logosAndTitles.get(position)).getLogoUrl());
            Utils.downloadImage(stringBuilder.toString(), viewHolder.first);
            viewHolder.setImageSize(sizes.get(position));
        } else if (holder instanceof TitleViewHolder) {
            TitleViewHolder viewHolder = ((TitleViewHolder) holder);
            viewHolder.first.setText((String) logosAndTitles.get(position));
            viewHolder.first.setTextColor(getColor(context, (String) logosAndTitles.get(position)));
        }
    }

    @Override
    public int getItemViewType(int position) {
        Object item = logosAndTitles.get(position);
        if (item instanceof Pair) {
            return TYPE_LOGOS;
        } else if (item instanceof Logo) {
            return TYPE_LOGO;
        }
        return TYPE_TITLE;
    }

    @Override
    public long getItemId(int position) {
        return logosAndTitles.get(position).hashCode();
    }

    @Override
    public int getItemCount() {
        return logosAndTitles.size();
    }

    public void addTitle(String title) {
        logosAndTitles.add(title);
        sizes.add(null);
    }

    public void addLogos(Pair<Logo, Logo> logos, int size) {
        logosAndTitles.add(logos);
        sizes.add(size);
    }

    public void addLogo(Logo logo, int size) {
        logosAndTitles.add(logo);
        sizes.add(size);
    }

    private int getColor(Context context, String title) {
        if (title.toLowerCase().contains(context.getString(R.string.platinum))) {
            return R.color.platinum;
        } else if (title.toLowerCase().contains(context.getString(R.string.gold))) {
            return R.color.gold;
        } else if (title.toLowerCase().contains(context.getString(R.string.silver))) {
            return R.color.silver;
        } else if (title.toLowerCase().contains(context.getString(R.string.bronze))) {
            return R.color.bronze;
        } else {
            return Color.BLACK;
        }
    }

    public static class LogosViewHolder extends RecyclerView.ViewHolder {
        protected ImageView first;
        protected ImageView second;

        public static LogosViewHolder create(ViewGroup parent) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.logos_row, parent, false);
            return new LogosViewHolder(itemView);
        }

        public LogosViewHolder(View itemView) {
            super(itemView);
            first = itemView.findViewById(R.id.first);
            second = itemView.findViewById(R.id.second);
        }

        public void setImageSize(Integer size) {
            if (size != null) {
                first.getLayoutParams().height = size;
                second.getLayoutParams().height = size;
                first.getLayoutParams().width = size;
                second.getLayoutParams().width = size;
            }
        }
    }

    public static class LogoViewHolder extends RecyclerView.ViewHolder {
        protected ImageView first;

        public static LogoViewHolder create(ViewGroup parent) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.logo_row, parent, false);
            return new LogoViewHolder(itemView);
        }

        public LogoViewHolder(View itemView) {
            super(itemView);
            first = itemView.findViewById(R.id.first);
        }

        public void setImageSize(Integer size) {
            if (size != null) {
                first.getLayoutParams().height = size;
                first.getLayoutParams().width = size;
            }
        }
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        protected TextView first;

        public static TitleViewHolder create(ViewGroup parent) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.title_row, parent, false);
            return new TitleViewHolder(itemView);
        }

        public TitleViewHolder(View itemView) {
            super(itemView);
            first = itemView.findViewById(R.id.partnerTitle);
        }
    }
}
