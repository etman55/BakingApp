package com.example.android.bakingapp.API;

import android.app.IntentService;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.android.bakingapp.Models.Model;
import com.example.android.bakingapp.MyApplication;
import com.example.android.bakingapp.Utils.ConnectivityReceiver;

import java.util.List;

import io.realm.Realm;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Etman on 6/16/2017.
 */

public class RecipesService extends IntentService implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String TAG = RecipesService.class.getSimpleName();
    public static final int STATUS_SUCCESS = 1;
    public static final int STATUS_FAIL = 2;
    private ResultReceiver receiver;

    public RecipesService() {
        super(RecipesService.class.getName());
        MyApplication.getInstance().setConnectivityListener(this);

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "Service Started!");
        receiver = intent.getParcelableExtra("receiver");
        requestData();
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        Log.d(TAG, "internet connection: " + isConnected);
        if (isConnected)
            requestData();

    }

    private void requestData() {
        ApiHandler mApiHandler = ApiHandler.getInstance();
        Call<List<Model>> getRecipes = mApiHandler.getServices().getRecipes();
        getRecipes.enqueue(new Callback<List<Model>>() {
            @Override
            public void onResponse(Call<List<Model>> call, final Response<List<Model>> response) {
                Log.d(TAG, "onResponse: " + response.body().size());
                Realm realm = Realm.getDefaultInstance();
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        if (response.body().size() > 0) {
                            for (int i = 0; i < response.body().size(); i++) {
                                Model model = response.body().get(i);
                                realm.copyToRealmOrUpdate(model);
                            }
                        }
                    }
                });
                receiver.send(STATUS_SUCCESS, Bundle.EMPTY);
                realm.close();
                stopSelf();
            }

            @Override
            public void onFailure(Call<List<Model>> call, Throwable t) {
                Bundle bundle = new Bundle();
                bundle.putString("errorMsg", t.getMessage());
                receiver.send(STATUS_FAIL, bundle);
                Log.d(TAG, "onFailure: " + t.getMessage());
                stopSelf();
            }
        });

    }


}
