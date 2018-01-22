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

public class TalkActivity extends AppCompatActivity {

    private static String EXTRA_TALK = "com.montreal.wtm.talk";

    public static Intent newIntent(Context context, Session session) {
        Intent intent = new Intent(context, TalkActivity.class);
        intent.putExtra(EXTRA_TALK, session);
        return intent;
    }

    private Session session;
    private Toolbar mToolBar;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.speaker_activity);
        Intent intent = getIntent();
        this.session = (Session) intent.getExtras().getParcelable(EXTRA_TALK);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);

        if (session.getSpeakersId() == null) {
            findViewById(R.id.bioTextView).setVisibility(View.GONE);
        }
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //TODO CHANGE THE VIEW FOR MORE THAN ONE SPEAKER
        for (int speakerId : session.getSpeakersId().keySet() ) {
            FirebaseData.INSTANCE.getSpeaker(this, requestListener, speakerId);
        }
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

        ImageView avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        //StringBuilder stringBuilder = new StringBuilder();
        //stringBuilder.append(getResources().getString(R.string.speakers_url))
        //        .append("%2F")
        //        .append(getResources().getString(R.string.speakers_url_end, mTimeslot.getSpeakerId()));

        //Utils.downloadImage(stringBuilder.toString(), avatarImageView);

        findViewById(R.id.talkInformation).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.titleTalkTextView)).setText(
            session.getTitle() != null ? Html.fromHtml(session.getTitle()) : null);
        ((TextView) findViewById(R.id.descriptionTalkTextView)).setText(
            session.getDescription() != null ? Html.fromHtml(session.getDescription()) : null);

        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
    }

    private FirebaseData.RequestListener<Speaker> requestListener = new FirebaseData.RequestListener<Speaker>() {
        @Override
        public void onDataChange(Speaker speaker) {
            mToolBar.setTitle(speaker.getName());
            mCollapsingToolbar.setTitle(speaker.getName());
            mTitleTextView.setText(speaker.getTitle() != null ? Html.fromHtml(speaker.getTitle()) : null);
            mDescriptionTextView.setText(speaker.getBio() != null ? Html.fromHtml(speaker.getBio()) : null);
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
