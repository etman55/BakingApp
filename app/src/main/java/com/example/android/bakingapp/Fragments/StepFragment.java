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

public class StepFragment extends Fragment implements ExoPlayer.EventListener {
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
    private MediaSessionCompat mediaSessionCompat;
    private PlaybackStateCompat.Builder builder;
    private boolean isReady;
    private long playPosition;
    private int currentFrame;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            playPosition = savedInstanceState.getLong("position");
            currentFrame = savedInstanceState.getInt("frame");
            isReady = savedInstanceState.getBoolean("isReady");
            videoUrl = savedInstanceState.getString("url");
            if (videoUrl != null && videoUrl.equals(""))
                if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                    Toast.makeText(getContext(), "No Video Available", Toast.LENGTH_SHORT).show();
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
        simpleExoPlayer.addListener(this);
        initMediaSession();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("frame", currentFrame);
        outState.putLong("position", playPosition);
        outState.putBoolean("isReady", isReady);
        outState.putString("url", videoUrl);
    }

    private void initPlayer() {
        simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());
        simpleExoPlayerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setPlayWhenReady(isReady);
        simpleExoPlayer.seekTo(currentFrame, playPosition);
        Uri uri = Uri.parse(videoUrl);
        MediaSource mediaSource = new ExtractorMediaSource(uri,
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
        simpleExoPlayer.prepare(mediaSource, true, false);
    }

    private void initMediaSession() {
        mediaSessionCompat = new MediaSessionCompat(getContext(), TAG);
        mediaSessionCompat.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mediaSessionCompat.setMediaButtonReceiver(null);
        builder = new PlaybackStateCompat.Builder().setActions(PlaybackStateCompat.ACTION_PLAY |
                PlaybackStateCompat.ACTION_PAUSE | PlaybackStateCompat.ACTION_PLAY_PAUSE | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS);
        mediaSessionCompat.setPlaybackState(builder.build());
        mediaSessionCompat.setCallback(new MyMediaSessionCallback());
        mediaSessionCompat.setActive(true);
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady)
            builder.setState(PlaybackStateCompat.STATE_PLAYING, simpleExoPlayer.getCurrentPosition(), 1f);
        else if (playbackState == ExoPlayer.STATE_READY)
            builder.setState(PlaybackStateCompat.STATE_PAUSED, simpleExoPlayer.getCurrentPosition(), 1f);
        mediaSessionCompat.setPlaybackState(builder.build());

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    private class MyMediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            simpleExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            simpleExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            simpleExoPlayer.seekTo(0);
        }
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        if (mediaSessionCompat != null) {
            mediaSessionCompat.setActive(false);
        }
    }
}
