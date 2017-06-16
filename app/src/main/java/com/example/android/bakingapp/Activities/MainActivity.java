package com.example.android.bakingapp.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.bakingapp.API.DownloadResultReceiver;
import com.example.android.bakingapp.API.RecipesService;
import com.example.android.bakingapp.Adapters.RecipesAdapter;
import com.example.android.bakingapp.Models.Model;
import com.example.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity implements DownloadResultReceiver.Receiver {
    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipes_list)
    RecyclerView recipesRecycler;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.error_msg)
    TextView errorMsg;
    @BindView(R.id.progress_bar)
    ProgressBar progressBar;
    private Realm realm;
    private RecipesAdapter recipesAdapter;
    private DownloadResultReceiver downloadResultReceiver;
    private RecyclerView.LayoutManager mLayoutManager;
    private RealmResults<Model> models;
    private RealmChangeListener changeListener = new RealmChangeListener() {
        @Override
        public void onChange(Object o) {
            recipesAdapter.updateList(models);
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        models.addChangeListener(changeListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        realm = Realm.getDefaultInstance();
        setSupportActionBar(toolbar);
        downloadResultReceiver = new DownloadResultReceiver(new Handler());
        downloadResultReceiver.setReceiver(this);
        showLoading();
        models = realm.where(Model.class).findAll();
        setRecipesRecycler();
        if (models.size() > 0)
            showList();
        else {
            Intent intent = new Intent(Intent.ACTION_SYNC, null, MainActivity.this, RecipesService.class);
            intent.putExtra("receiver", downloadResultReceiver);
            startService(intent);
        }
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == RecipesService.STATUS_SUCCESS) {
            showList();
        } else
            showError(resultData.getString("errorMsg"));
    }

    private void setRecipesRecycler() {
        recipesRecycler.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recipesRecycler.setLayoutManager(mLayoutManager);
        recipesAdapter = new RecipesAdapter(models, this);
        recipesRecycler.setAdapter(recipesAdapter);
        recipesAdapter.setOnItemClickListener(new RecipesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        });
    }

    private void showList() {
        progressBar.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
        recipesRecycler.setVisibility(View.VISIBLE);
    }

    private void showError(String error) {
        progressBar.setVisibility(View.INVISIBLE);
        recipesRecycler.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.VISIBLE);
        errorMsg.setText(error);
    }

    private void showLoading() {
        recipesRecycler.setVisibility(View.INVISIBLE);
        errorLayout.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        models.removeChangeListener(changeListener);
        realm.close();
    }
}


