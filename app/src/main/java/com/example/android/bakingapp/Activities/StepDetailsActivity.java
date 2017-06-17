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
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras() != null) {
            StepFragment stepFragment = StepFragment.newInstance(
                    getIntent().getExtras().getString("stepDesc"),
                    getIntent().getExtras().getString("stepVideo"),
                    getIntent().getExtras().getString("stepThumbnail"));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.step_fragment, stepFragment);
            ft.commit();
        }

    }
}
