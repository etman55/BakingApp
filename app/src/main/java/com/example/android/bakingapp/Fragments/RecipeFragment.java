package com.example.android.bakingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.Adapters.IngredientsAdapter;
import com.example.android.bakingapp.Adapters.StepsAdapter;
import com.example.android.bakingapp.Models.Ingredient;
import com.example.android.bakingapp.Models.Model;
import com.example.android.bakingapp.Models.Step;
import com.example.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;


public class RecipeFragment extends Fragment {
    private static final String TAG = RecipeFragment.class.getSimpleName();
    @BindView(R.id.ingredients_list)
    RecyclerView ingredientsRecycler;
    @BindView(R.id.steps_list)
    RecyclerView stepsRecycler;
    @BindView(R.id.scrollView)
    NestedScrollView scrollView;
    private StepCallBack callback;
    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;
    private Realm realm;
    private Model model;

    public void setCallbackHandler(StepCallBack stepCallback) {
        callback = stepCallback;
    }

    public RecipeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getDefaultInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        ButterKnife.bind(this, rootView);
        setIngredientsRecycler();
        setStepsRecycler();
        return rootView;
    }

    private void setIngredientsRecycler() {
        ingredientsRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        ingredientsRecycler.setLayoutManager(mLayoutManager);
        ingredientsAdapter = new IngredientsAdapter(getContext());
        ingredientsRecycler.setAdapter(ingredientsAdapter);
    }

    private void setStepsRecycler() {
        stepsRecycler.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        stepsRecycler.setLayoutManager(mLayoutManager);
        stepsAdapter = new StepsAdapter(getContext());
        stepsRecycler.setAdapter(stepsAdapter);
        stepsAdapter.setOnItemClickListener(new StepsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                if (model != null)
                    callback.onItemSelected(model.getSteps().get(position).getDescription(),
                            model.getSteps().get(position).getVideoURL(),
                            model.getSteps().get(position).getThumbnailURL());
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    public interface StepCallBack {
        void onItemSelected(String description, String videoURL, String thumbnailURL);

    }

    public void getRecipeId(long id) {
        model = realm.where(Model.class)
                .equalTo("id", id)
                .findFirst();

        if (model != null) {
            ingredientsAdapter.updateList(model.getIngredients());
            stepsAdapter.updateList(model.getSteps());
            scrollView.smoothScrollTo(0, 0);
        }
    }

}
