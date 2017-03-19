package com.montreal.wtm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.DataManager;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.model.Talk;
import com.squareup.picasso.Picasso;


public class TalkActivity extends AppCompatActivity {

    private static String EXTRA_TALK = "com.montreal.wtm.talk";

    public static Intent newIntent(Context context, Talk talk) {
        Intent intent = new Intent(context, TalkActivity.class);
        intent.putExtra(EXTRA_TALK, talk);
        return intent;
    }

    private Talk mTalk;
    private Toolbar mToolBar;
    private TextView mTitleTextView;
    private TextView mDescriptionTextView;
    private CollapsingToolbarLayout mCollapsingToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.speaker_activity);
        Intent intent = getIntent();
        mTalk = (Talk) intent.getExtras().getParcelable(EXTRA_TALK);

        mToolBar = (Toolbar) findViewById(R.id.toolbar);

        if (mTalk.getType().equals(Talk.Type.General.toString())) {
            findViewById(R.id.bioTextView).setVisibility(View.GONE);
        }
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FirebaseData.getSpeaker(requestListener, mTalk.getSpeakerId());
        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager.getInstance().addLoveTalk(mTalk.getSpeakerId());
                //TODO CHANGE
                Snackbar.make(view, "The talk of this speaker is now love in the schedule ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ImageView avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.speakers_url))
                .append("%2F")
                .append(getResources().getString(R.string.speakers_url_end, mTalk.getSpeakerId()));

        Picasso.with(this).load(stringBuilder.toString())
                .into(avatarImageView);

        findViewById(R.id.talkInformation).setVisibility(View.VISIBLE);
        ((TextView) findViewById(R.id.titleTalkTextView)).setText(mTalk.getTitle() != null ? Html.fromHtml(mTalk.getTitle()) : null);
        ((TextView) findViewById(R.id.descriptionTalkTextView)).setText(mTalk.getDescription() != null ? Html.fromHtml(mTalk.getDescription()) : null);

        mTitleTextView = (TextView) findViewById(R.id.titleTextView);
        mDescriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
    }

    private FirebaseData.RequestListener<Speaker> requestListener = new FirebaseData.RequestListener<Speaker>() {
        @Override
        public void onDataChange(Speaker speaker) {
            mToolBar.setTitle(speaker.getName());
            mCollapsingToolbar.setTitle(speaker.getName());
            mTitleTextView.setText(speaker.getTitle() != null ? Html.fromHtml(speaker.getTitle()) : null);
            mDescriptionTextView.setText(speaker.getDescription() != null ? Html.fromHtml(speaker.getDescription()) : null);
        }

        @Override
        public void onCancelled(DatabaseError error) {

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
