package com.example.android.bakingapp.Fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.Models.Step;
import com.example.android.bakingapp.R;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class StepFragment extends Fragment {
    private static final String TAG = StepFragment.class.getSimpleName();
    private String desc;
    private String videoUrl;
    private String thumbnailUrl;
    @Nullable
    @BindView(R.id.step_desc)
    TextView stepDesc;
    @Nullable
    @BindView(R.id.step_image)
    ImageView stepImage;
    @BindView(R.id.step_video)
    SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer simpleExoPlayer;
    private boolean isReady;
    private long playPosition;
    private int currentFrame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            videoUrl = savedInstanceState.getString("url");
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                if (videoUrl != null && videoUrl.equals("")) {
                    Toast.makeText(getContext(), "No Video Available", Toast.LENGTH_SHORT).show();
                } else if (videoUrl != null)
                    hideSystemUi();

        }
        return rootView;
    }

    public void getStepInfo(String description, String videoURL, String thumbnailURL) {
        desc = description;
        videoUrl = videoURL;
        thumbnailUrl = thumbnailURL;
        if (stepDesc != null)
            stepDesc.setText(desc);
        if (stepImage != null) {
            if (!thumbnailUrl.equals(""))
                Picasso.with(getContext()).load(thumbnailUrl).into(stepImage);
            else stepImage.setVisibility(View.GONE);
        }
        if (videoUrl.equals(""))
            simpleExoPlayerView.setVisibility(View.GONE);
        initPlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("url", videoUrl);
    }

    private void initPlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            simpleExoPlayerView.setPlayer(simpleExoPlayer);
            simpleExoPlayer.setPlayWhenReady(isReady);
            simpleExoPlayer.seekTo(currentFrame, playPosition);
        }
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
        simpleExoPlayer.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playPosition = simpleExoPlayer.getCurrentPosition();
            currentFrame = simpleExoPlayer.getCurrentWindowIndex();
            isReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    private void hideSystemUi() {
        simpleExoPlayerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }
}
