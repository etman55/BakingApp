package com.example.android.bakingapp;

import android.app.Application;

import com.example.android.bakingapp.Utils.ConnectivityReceiver;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by Etman on 6/16/2017.
 */

public class MyApplication extends Application {
    private static MyApplication instance;

    public MyApplication() {
        instance = this;
    }

    public static synchronized MyApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm.getInstance(realmConfiguration);
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
