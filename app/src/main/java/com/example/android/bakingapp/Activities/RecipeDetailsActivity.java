package com.example.android.bakingapp.Activities;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.example.android.bakingapp.Fragments.RecipeFragment;
import com.example.android.bakingapp.Fragments.StepFragment;
import com.example.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailsActivity extends AppCompatActivity implements RecipeFragment.StepCallBack {
    private static final String TAG = RecipeDetailsActivity.class.getSimpleName();
    long recipeId;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras() != null) {
            recipeId = getIntent().getExtras().getLong("recipeId");
            RecipeFragment recipeFragment = RecipeFragment.newInstance(recipeId);
            recipeFragment.setCallbackHandler(this);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.recipe_fragment, recipeFragment);
            ft.commit();
        }

    }

    @Override
    public void onItemSelected(String description, String videoURL, String thumbnailURL) {
        StepFragment stepFragment = (StepFragment) getSupportFragmentManager().findFragmentById(R.id.step_fragment);
        if (stepFragment == null) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra("stepDesc", description);
            intent.putExtra("stepVideo", videoURL);
            intent.putExtra("stepThumbnail", thumbnailURL);
            startActivity(intent);
        } else
            stepFragment.getStepInfo(description, videoURL, thumbnailURL);
    }
}
