package com.montreal.wtm.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.ImageViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import kotlin.Pair;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TalkActivity extends BaseActivity implements View.OnClickListener {

    private static final String EXTRA_TALK = "com.montreal.wtm.talk";
    private static final String TAG = TalkActivity.class.getSimpleName();

    public static Intent newIntent(Context context, Talk talk) {
        Intent intent = new Intent(context, TalkActivity.class);
        intent.putExtra(EXTRA_TALK, talk);
        return intent;
    }

    private FloatingActionButton fab;
    private View speakerTitleSection;
    private RecyclerView speakers;
    private Talk talk;
    private Toolbar toolBar;
    private TextView talkTitle;
    private TextView talkDescription;
    private ImageView avatarImageView;
    private CollapsingToolbarLayout collapsingToolbar;
    private SpeakersAdapter speakersAdapter;
    private TextView talkLocationTimeLocale;
    private TextView talkTags;

    private TextView speakerSubtitle;
    private ImageView[] stars;

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
        speakerTitleSection = findViewById(R.id.speaker_title_section);
        speakerSubtitle = findViewById(R.id.speaker_title);
        talkLocationTimeLocale = findViewById(R.id.talk_location_time_locale);
        talkTags = findViewById(R.id.talk_tags);

        stars = new ImageView[5];
        stars[0] = findViewById(R.id.one_star);
        stars[1] = findViewById(R.id.two_stars);
        stars[2] = findViewById(R.id.three_stars);
        stars[3] = findViewById(R.id.four_stars);
        stars[4] = findViewById(R.id.five_stars);

        for (int i = 0; i < stars.length; i++) {
            stars[i].setOnClickListener(this);
            stars[i].setTag(i);
        }

        setSupportActionBar(toolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        talkLocationTimeLocale.setText(talk.getLocationTimeLocale());
        talkTags.setText(talk.getSessionTags());

        talkTitle.setText(talk.getSessionTitle());
        talkDescription.setText(talk.getSessionDescription());

        if (talk.hasSpeakers()) {
            speakerTitleSection.setVisibility(View.VISIBLE);
            speakers.setVisibility(View.VISIBLE);
            speakersAdapter = new SpeakersAdapter(this);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            layoutManager.setAutoMeasureEnabled(true);
            speakers.setLayoutManager(layoutManager);
            speakers.setAdapter(speakersAdapter);
            for (int speakerId : talk.getSpeakers()) {
                FirebaseData.INSTANCE.getSpeakerByInnerSpeakerId(this, requestListener, speakerId);
            }
        } else {
            speakerTitleSection.setVisibility(View.GONE);
            speakers.setVisibility(View.GONE);
        }

        fab = findViewById(R.id.fab);
        int drawableId = R.drawable.ic_favorite_white_24px;
        if (talk.getSaved()) {
            drawableId = R.drawable.ic_favorite_black_24px;
        }
        fab.setImageResource(drawableId);
        fab.setOnClickListener(this);

        if(FirebaseWrapper.Companion.isLogged()) {
            FirebaseData.INSTANCE.getMySessionRating(this, requestSessionRatingListener, talk.getSessionId());
        }
    }

    private FirebaseData.RequestListener<Speaker> requestListener = new FirebaseData.RequestListener<Speaker>() {
        @Override
        public void onDataChange(Speaker speaker) {
            speakersAdapter.addSpeaker(speaker);
            speakersAdapter.notifyDataSetChanged();
            speakerSubtitle.setText(
                getResources().getQuantityString(R.plurals.speaker_detail, speakersAdapter.getItemCount()));
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
        switch (v.getId()) {
            case R.id.fab:
                if (FirebaseWrapper.Companion.isLogged()) {
                    talk.setSaved(!talk.getSaved());
                    FirebaseData.INSTANCE.saveSession(talk.getSession().getId(), talk.getSaved());
                    int drawableId, messageId;

                    if (talk.getSaved()) {
                        drawableId = R.drawable.ic_favorite_black_24px;
                        messageId = R.string.talk_added;
                    } else {
                        drawableId = R.drawable.ic_favorite_white_24px;
                        messageId = R.string.talk_removed;
                    }
                    fab.setImageResource(drawableId);
                    Snackbar.make(v, messageId, Snackbar.LENGTH_LONG).show();
                } else {
                    promptLogin();
                }
                break;
            case R.id.one_star:
            case R.id.two_stars:
            case R.id.three_stars:
            case R.id.four_stars:
            case R.id.five_stars:
                if (v.getTag() != null && v.getTag() instanceof Integer) {
                    int rating = (int) v.getTag();
                    highlightStars(rating);
                    saveRating(rating);
                }
        }
    }

    private void highlightStars(long rating) {
        if(rating < 0) {
            return;
        }
        for (int i = 0; i < stars.length; i++) {
            int highlightColor;
            if (i <= rating) {
                highlightColor = R.color.green_wtm;
            } else {
                highlightColor = R.color.black;
            }
            ImageViewCompat.setImageTintList(stars[i], ColorStateList.valueOf(getResources().getColor(highlightColor)));
            ImageViewCompat.setImageTintMode(stars[i], PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void saveRating(int rating) {
        FirebaseData.INSTANCE.saveSessionRating(talk.getSessionId(), rating + 1);
    }

    private FirebaseData.RequestListener<Pair<String, Long>> requestSessionRatingListener =
        new FirebaseData.RequestListener<Pair<String, Long>>() {
            @Override
            public void onDataChange(@Nullable Pair<String, Long> data) {
                Log.d(TAG, "Received session rating " + data.getFirst()+ ", " + data.getSecond());
                try {
                    highlightStars(data.getSecond());
                } catch (NumberFormatException ex) {
                    Log.w(TAG, "Invalid rating", ex);
                }
            }

            @Override
            public void onCancelled(@NotNull FirebaseData.ErrorFirebase errorType) {

            }
        };
}
