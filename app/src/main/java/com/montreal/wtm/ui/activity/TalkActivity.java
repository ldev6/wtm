package com.montreal.wtm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseWrapper;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.ui.adapter.SpeakersAdapter;
import com.montreal.wtm.utils.ui.activity.BaseActivity;

public class TalkActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_TALK = "com.montreal.wtm.talk";

    public static Intent newIntent(Context context, Talk talk) {
        Intent intent = new Intent(context, TalkActivity.class);
        intent.putExtra(EXTRA_TALK, talk);
        return intent;
    }

    private FloatingActionButton fab;
    private View speakerTitle;
    private RecyclerView speakers;
    private Talk talk;
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
        this.talk = intent.getExtras().getParcelable(EXTRA_TALK);

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
        talkTitle.setText(talk.getSessionTitle());
        talkDescription.setText(talk.getSessionDescription());

        if (talk.hasSpeakers()) {
            speakerTitle.setVisibility(View.VISIBLE);
            speakers.setVisibility(View.VISIBLE);
            speakersAdapter = new SpeakersAdapter(this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setAutoMeasureEnabled(true);
            speakers.setLayoutManager(layoutManager);
            speakers.setAdapter(speakersAdapter);
            for (int speakerId : talk.getSpeakers()) {
                FirebaseData.INSTANCE.getSpeaker(this, requestListener, speakerId);
            }
        } else {
            speakerTitle.setVisibility(View.GONE);
            speakers.setVisibility(View.GONE);
        }

        fab = findViewById(R.id.fab);
        int drawableId = R.drawable.ic_favorite_white_24px;
        if (talk.getSaved()) {
            drawableId = R.drawable.ic_favorite_black_24px;
        }
        fab.setImageResource(drawableId);
        fab.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.fab) {
            if (FirebaseWrapper.Companion.isLogged()) {
                talk.setSaved(!talk.getSaved());
                FirebaseData.INSTANCE.saveSpeaker(talk.getSession().getId(), talk.getSaved());
                int drawableId, messageId;

                if (talk.getSaved()) {
                    drawableId = R.drawable.ic_favorite_black_24px;
                    messageId = R.string.speaker_added;
                } else {
                    drawableId = R.drawable.ic_favorite_white_24px;
                    messageId = R.string.speaker_removed;
                }
                fab.setImageResource(drawableId);
                Snackbar.make(v, messageId, Snackbar.LENGTH_LONG).show();
            } else {
                (new AlertDialog.Builder(this)).setTitle(R.string.login_required)
                    .setCancelable(true)
                    .setNegativeButton(R.string.cancel, (dialog, which) -> dialog.dismiss())
                    .setPositiveButton(R.string.ok, (dialog, which) -> {
                        dialog.dismiss();
                        signIn();
                    })
                    .show();
            }
        }
    }
}
