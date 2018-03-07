package com.montreal.wtm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Session;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.ui.adapter.SpeakersAdapter;
import com.montreal.wtm.utils.ui.activity.BaseActivity;
import java.util.HashMap;

public class TalkActivity extends BaseActivity {

    private static final String EXTRA_TALK = "com.montreal.wtm.talk";

    public static Intent newIntent(Context context, Session session) {
        Intent intent = new Intent(context, TalkActivity.class);
        intent.putExtra(EXTRA_TALK, session);
        return intent;
    }

    private View speakerTitle;
    private RecyclerView speakers;
    private Session session;
    private Toolbar toolBar;
    private TextView talkTitle;
    private TextView talkDescription;
    private ImageView avatarImageView;
    private CollapsingToolbarLayout collapsingToolbar;
    private SpeakersAdapter speakersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.talk_activity);
        Intent intent = getIntent();
        this.session = intent.getExtras().getParcelable(EXTRA_TALK);

        toolBar = findViewById(R.id.toolbar);
        collapsingToolbar = findViewById(R.id.toolbar_layout);
        avatarImageView = findViewById(R.id.avatarImageView);
        talkTitle = findViewById(R.id.talk_title);
        talkDescription = findViewById(R.id.talk_description);
        speakers = findViewById(R.id.speakers);
        speakerTitle = findViewById(R.id.speaker_title);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        talkTitle.setVisibility(View.VISIBLE);
        talkTitle.setText(session.getTitle() != null ? Html.fromHtml(session.getTitle()) : null);
        talkDescription.setText(session.getDescription() != null ? Html.fromHtml(session.getDescription()) : null);

        if (session.getSpeakers() != null && !session.getSpeakers().isEmpty()){
            speakerTitle.setVisibility(View.VISIBLE);
            speakers.setVisibility(View.VISIBLE);
            speakersAdapter = new SpeakersAdapter(this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setAutoMeasureEnabled(true);
            speakers.setLayoutManager(layoutManager);
            speakers.setAdapter(speakersAdapter);
            for (int speakerId : session.getSpeakers()) {
                FirebaseData.INSTANCE.getSpeaker(this, requestListener, speakerId);
            }
        } else {
            speakerTitle.setVisibility(View.GONE);
            speakers.setVisibility(View.GONE);
        }

        final FloatingActionButton fab = findViewById(R.id.fab);
        //TODO do this logic with firebase
        //if (DataManager.Companion.getInstance().loveTalkContainSpeaker(mTimeslot.getSpeakerId())) {
        //    fab.setImageResource(R.drawable.ic_favorite_black_24px);
        //} else {
        //    fab.setImageResource(R.drawable.ic_favorite_white_24px);
        //}
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO do this logic with firebase

                //if (DataManager.Companion.getInstance().loveTalkContainSpeaker(mTimeslot.getSpeakerId())) {
                //    DataManager.Companion.getInstance().removeLoveTalks(mTimeslot.getSpeakerId());
                //    fab.setImageResource(R.drawable.ic_favorite_white_24px);
                //    Snackbar.make(view, R.string.talk_removed, Snackbar.LENGTH_LONG)
                //            .setAction("Action", null).show();
                //} else {
                //    DataManager.Companion.getInstance().addLoveTalk(mTimeslot.getSpeakerId());
                //    fab.setImageResource(R.drawable.ic_favorite_black_24px);
                //    Snackbar.make(view, R.string.talk_added, Snackbar.LENGTH_LONG)
                //            .setAction("Action", null).show();
                //}
            }
        });
    }

    private FirebaseData.RequestListener<Speaker> requestListener = new FirebaseData.RequestListener<Speaker>() {
        @Override
        public void onDataChange(Speaker speaker) {
            speakersAdapter.addSpeaker(speaker);
            speakersAdapter.notifyDataSetChanged();
        }

        @Override
        public void onCancelled(FirebaseData.ErrorFirebase errorType) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
