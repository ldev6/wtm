package com.montreal.wtm.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.montreal.wtm.R;
import com.montreal.wtm.model.Session;
import java.util.ArrayList;
import java.util.List;

public class SimpleSessionAdapter extends RecyclerView.Adapter {
    protected static final String LINKEDIN = "linkedin";
    protected static final String GITHUB = "github";
    protected static final String WEBSITE = "website";
    protected static final String TWITTER = "twitter";
    protected static final String FACEBOOK = "facebook";

    protected List<Session> sessions;

    public SimpleSessionAdapter(List<Session> sessions) {
        this.sessions = sessions;
    }

    public SimpleSessionAdapter(Session session) {
        this.sessions = new ArrayList<>();
        this.sessions.add(session);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleSessionViewHolder(
            LayoutInflater.from(parent.getContext()).inflate(R.layout.simple_session_row, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SimpleSessionViewHolder viewHolder = (SimpleSessionViewHolder) holder;
        Session session = sessions.get(position);
        String title = (TextUtils.isEmpty(session.getTitle()) ? "" : session.getTitle()) + " ";
        title += TextUtils.isEmpty(session.getLanguage()) ? ""
            : String.format(viewHolder.title.getContext().getString(R.string.wrapped_content), session.getLanguage());
        viewHolder.title.setText(title);

        viewHolder.description.setText(session.getDescription());
    }

    @Override
    public int getItemCount() {
        return sessions == null ? 0 : sessions.size();
    }

    public class SimpleSessionViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView description;

        public SimpleSessionViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.text_title);
            this.description = itemView.findViewById(R.id.text_description);
        }
    }
}
