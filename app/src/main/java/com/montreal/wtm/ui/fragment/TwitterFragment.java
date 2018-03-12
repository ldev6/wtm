package com.montreal.wtm.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.montreal.wtm.R;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;


public class TwitterFragment extends ListFragment {

    private static String TAG = TwitterFragment.class.getSimpleName();

    public static TwitterFragment newInstance() {
        return new TwitterFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().findViewById(R.id.messageView).setVisibility(View.GONE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.twitter_fragment, container, false);

        final SearchTimeline searchTimeline = new SearchTimeline.Builder()
                .query(getString(R.string.tweet_search_query))
                .build();
        final TweetTimelineListAdapter adapter = new TweetTimelineListAdapter.Builder(getActivity())
                .setTimeline(searchTimeline)
                .build();

        setListAdapter(adapter);

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(clicked -> showTweetComposer());

        return view;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        showTweetComposer();
        return true;

    }

    private void showTweetComposer() {
        TweetComposer.Builder builder = new TweetComposer.Builder(getActivity())
                .text(getString(R.string.tweet_composer_text));
        builder.show();
    }
}
