package com.montreal.wtm.ui.fragment;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.crashlytics.android.Crashlytics;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseWrapper;
import com.montreal.wtm.R;
import com.montreal.wtm.api.FirebaseData;
import com.montreal.wtm.model.Speaker;
import com.montreal.wtm.model.Talk;
import com.montreal.wtm.ui.adapter.SpeakersAdapter;
import com.montreal.wtm.utils.ui.fragment.BaseFragment;
import kotlin.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
//import timber.log.Timber;

public class TalkFragment extends BaseFragment implements View.OnClickListener {

    public static final String EXTRA_TALK = "com.montreal.wtm.talk";

    public static Intent newIntent(Context context, Talk talk) {
        Intent intent = new Intent(context, TalkFragment.class);
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

    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @androidx.annotation.Nullable ViewGroup container,
        @androidx.annotation.Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.talk_activity, container, false);

        //this.talk = getArguments().getParcelable(EXTRA_TALK);
        this.talk = TalkFragmentArgs.fromBundle(getArguments()).getTalk();

        toolBar = v.findViewById(R.id.toolbar);
        collapsingToolbar = v.findViewById(R.id.toolbar_layout);
        avatarImageView = v.findViewById(R.id.avatarImageView);
        talkTitle = v.findViewById(R.id.talk_title);
        talkDescription = v.findViewById(R.id.talk_description);
        speakers = v.findViewById(R.id.speakers);
        speakerTitleSection = v.findViewById(R.id.speaker_title_section);
        speakerSubtitle = v.findViewById(R.id.speaker_title);
        talkLocationTimeLocale = v.findViewById(R.id.talk_location_time_locale);
        talkTags = v.findViewById(R.id.talk_tags);

        stars = new ImageView[5];
        stars[0] = v.findViewById(R.id.one_star);
        stars[1] = v.findViewById(R.id.two_stars);
        stars[2] = v.findViewById(R.id.three_stars);
        stars[3] = v.findViewById(R.id.four_stars);
        stars[4] = v.findViewById(R.id.five_stars);

        for (int i = 0; i < stars.length; i++) {
            stars[i].setOnClickListener(this);
            stars[i].setTag(i + 1);
        }

        //int screenHeight = getAnimationScreenHeight();
        //v.setTranslationY(screenHeight);
        

        talkLocationTimeLocale.setText(talk.getLocationTimeLocale());
        talkTags.setText(talk.getSessionTags());

        talkTitle.setText(talk.getSessionTitle());
        talkDescription.setText(talk.getSessionDescription());

        if (talk.hasSpeakers()) {
            speakerTitleSection.setVisibility(View.VISIBLE);
            speakers.setVisibility(View.VISIBLE);
            speakersAdapter = new SpeakersAdapter(getContext());
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            layoutManager.setAutoMeasureEnabled(true);
            speakers.setLayoutManager(layoutManager);
            speakers.setAdapter(speakersAdapter);
            for (int speakerId : talk.getSpeakers()) {
                FirebaseData.INSTANCE.getSpeakerByInnerSpeakerId(getActivity(), requestListener, speakerId);
            }
        } else {
            speakerTitleSection.setVisibility(View.GONE);
            speakers.setVisibility(View.GONE);
        }

        fab = v.findViewById(R.id.fab);
        int drawableId = R.drawable.ic_favorite_white_24px;
        if (talk.getSaved()) {
            drawableId = R.drawable.ic_favorite_black_24px;
        }
        fab.setImageResource(drawableId);
        fab.setOnClickListener(this);

        if (FirebaseWrapper.Companion.isLogged()) {
            FirebaseData.INSTANCE.getMySessionRating(getActivity(), requestSessionRatingListener, talk.getSessionId());
        }

        return v;
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

    //@Override
    //public Animator onCreateAnimator(int transit, boolean enter, int nextAnim) {
    //    View view = getView();
    //
    //    if (view == null) {
    //        return null;
    //    }
    //
    //    if (enter) {
    //        view.setElevation(10);
    //
    //        view.animate().translationY(0).setDuration(500).start();
    //    } else if (!enter) {
    //
    //        int screenHeight = getAnimationScreenHeight();
    //
    //        view.animate().translationY(screenHeight).setDuration(500).start();
    //    }
    //  
    //    return ValueAnimator.ofInt(0, 0).setDuration(500);
    //}
   

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
                    //TODO 
                    //                    promptLogin();
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
        if (rating < 0) {
            return;
        }
        for (int i = 0; i < stars.length; i++) {
            int highlightColor;
            if (i < rating) {
                highlightColor = R.color.green_wtm;
            } else {
                highlightColor = R.color.black;
            }
            ImageViewCompat.setImageTintList(stars[i], ColorStateList.valueOf(getResources().getColor(highlightColor)));
            ImageViewCompat.setImageTintMode(stars[i], PorterDuff.Mode.SRC_ATOP);
        }
    }

    private void saveRating(int rating) {
        FirebaseData.INSTANCE.saveSessionRating(talk.getSessionId(), rating);
    }

    private FirebaseData.RequestListener<Pair<String, Long>> requestSessionRatingListener =
        new FirebaseData.RequestListener<Pair<String, Long>>() {
            @Override
            public void onDataChange(@Nullable Pair<String, Long> data) {
                //                Timber.d("Received session rating " + data.getFirst() + ", " + data.getSecond());
                try {
                    highlightStars(data.getSecond());
                } catch (NumberFormatException ex) {
                    //                    Timber.w(ex, "Invalid rating");
                    Crashlytics.logException(ex);
                }
            }

            @Override
            public void onCancelled(@NotNull FirebaseData.ErrorFirebase errorType) {

            }
        };

    @Override
    public void retryOnProblem() {

    }
    
    private int getAnimationScreenHeight() {
        return getContext().getResources().getDisplayMetrics().heightPixels;
    }
}
