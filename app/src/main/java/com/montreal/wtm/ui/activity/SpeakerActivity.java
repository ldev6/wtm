package com.montreal.wtm.ui.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;


import com.montreal.wtm.R;
import com.montreal.wtm.model.DataManager;
import com.montreal.wtm.model.Speaker;
import com.squareup.picasso.Picasso;

public class SpeakerActivity extends AppCompatActivity {

    private static String EXTRA_SPEAKER = "com.montreal.wtm.speaker";
    private static String EXTRA_SPEAKER_KEY = "com.montreal.wtm.speaker_key";

    public static Intent newIntent(Context context, Speaker speaker, String key) {
        Intent intent = new Intent(context, SpeakerActivity.class);
        intent.putExtra(EXTRA_SPEAKER, speaker);
        intent.putExtra(EXTRA_SPEAKER_KEY, key);
        return intent;
    }

    private String mSpeakerKey;
    private Speaker mSpeaker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().requestFeature(Window.FEATURE_ACTION_MODE_OVERLAY);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.speaker_activity);

        Intent intent = getIntent();
        mSpeakerKey = intent.getExtras().getString(EXTRA_SPEAKER_KEY);
        mSpeaker = intent.getExtras().getParcelable(EXTRA_SPEAKER);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(mSpeaker.getName());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataManager.getInstance().addLoveTalk(mSpeakerKey);
                Snackbar.make(view, "The talk of this speaker is now love in the schedule ", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        ImageView avatarImageView = (ImageView) findViewById(R.id.avatarImageView);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getResources().getString(R.string.speakers_url))
                .append("%2F")
                .append(getResources().getString(R.string.speakers_url_end, mSpeakerKey));

        Picasso.with(this).load(stringBuilder.toString())
                .into(avatarImageView);

        ((TextView) findViewById(R.id.titleTextView)).setText(mSpeaker.title != null ? Html.fromHtml(mSpeaker.title) : null);
        ((TextView) findViewById(R.id.descriptionTextView)).setText(mSpeaker.description != null ? Html.fromHtml(mSpeaker.description) : null);
    }

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
