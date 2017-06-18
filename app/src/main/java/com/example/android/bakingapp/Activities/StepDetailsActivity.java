package com.example.android.bakingapp.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.android.bakingapp.Fragments.StepFragment;
import com.example.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsActivity extends AppCompatActivity {
    private static final String TAG = StepDetailsActivity.class.getSimpleName();
    private String stepDesc;
    private String stepVideo;
    private String stepImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        if (getIntent().getExtras() != null) {
            stepDesc = getIntent().getExtras().getString("stepDesc");
            stepVideo = getIntent().getExtras().getString("stepVideo");
            stepImage = getIntent().getExtras().getString("stepThumbnail");
        }
        if (savedInstanceState != null) {
            stepDesc = savedInstanceState.getString("stepDesc");
            stepVideo = savedInstanceState.getString("stepVideo");
            stepImage = savedInstanceState.getString("stepThumbnail");
        }
        StepFragment stepFragment = (StepFragment) getSupportFragmentManager()
                .findFragmentById(R.id.step_fragment);
        stepFragment.getStepInfo(stepDesc, stepVideo, stepImage);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("stepDesc", stepDesc);
        outState.putString("stepVideo", stepVideo);
        outState.putString("stepThumbnail", stepImage);
    }
}
