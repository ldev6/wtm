package com.montreal.wtm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
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
import com.montreal.wtm.utils.Utils;
import com.montreal.wtm.utils.ui.activity.BaseActivity;

public class TalkActivity extends BaseActivity {

    private static final String EXTRA_TALK = "com.montreal.wtm.talk";

    public static Intent newIntent(Context context, Session session) {
        Intent intent = new Intent(context, TalkActivity.class);
        intent.putExtra(EXTRA_TALK, session);
        return intent;
    }

    private Session session;
    private Toolbar toolBar;
    private TextView titleTextView;
    private TextView descriptionTextView;
    private ImageView avatarImageView;
    private CollapsingToolbarLayout collapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.speaker_activity);
        Intent intent = getIntent();
        this.session = intent.getExtras().getParcelable(EXTRA_TALK);

        toolBar = findViewById(R.id.toolbar);

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TODO CHANGE THE VIEW FOR MORE THAN ONE SPEAKER
        if (session.getSpeakersId() == null) {
            findViewById(R.id.bioTextView).setVisibility(View.GONE);
        } else {
            for (int speakerId : session.getSpeakersId()) {
                FirebaseData.INSTANCE.getSpeaker(this, requestListener, speakerId);
            }
        }
        collapsingToolbar = findViewById(R.id.toolbar_layout);

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

        avatarImageView = findViewById(R.id.avatarImageView);

        findViewById(R.id.talkInformation).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.titleTalkTextView)).setText(
            session.getTitle() != null ? Html.fromHtml(session.getTitle()) : null);
        ((TextView) findViewById(R.id.descriptionTalkTextView)).setText(
            session.getDescription() != null ? Html.fromHtml(session.getDescription()) : null);

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
    }

    private FirebaseData.RequestListener<Speaker> requestListener = new FirebaseData.RequestListener<Speaker>() {
        @Override
        public void onDataChange(Speaker speaker) {
            toolBar.setTitle(speaker.getName());
            collapsingToolbar.setTitle(speaker.getName());
            titleTextView.setText(speaker.getTitle() != null ? Html.fromHtml(speaker.getTitle()) : null);
            descriptionTextView.setText(speaker.getBio() != null ? Html.fromHtml(speaker.getBio()) : null);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(getResources().getString(R.string.storage_url)).append(speaker.getPhotoUrl());

            Utils.downloadImage(stringBuilder.toString(), avatarImageView);
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
