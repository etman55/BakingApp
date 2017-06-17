package com.example.android.bakingapp.Activities;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.example.android.bakingapp.Fragments.RecipeFragment;
import com.example.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeDetailsActivity extends AppCompatActivity {
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
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.recipe_fragment, recipeFragment);
            ft.commit();
        }

    }


}
