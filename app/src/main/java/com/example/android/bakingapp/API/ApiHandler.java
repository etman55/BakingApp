package com.example.android.bakingapp.API;

import com.example.android.bakingapp.Models.Model;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

/**
 * Created by Etman on 6/16/2017.
 */

public class ApiHandler {
    private static final String TAG = ApiHandler.class.getSimpleName();
    private static ApiHandler mInstance;
    private Services mServices;
    private Retrofit mRetrofit;
    private static final String BASE_URL = "http://go.udacity.com/";

    private ApiHandler() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(120, TimeUnit.SECONDS)
                .connectTimeout(120, TimeUnit.SECONDS)
                .build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        mServices = mRetrofit.create(Services.class);
    }

    public static ApiHandler getInstance() {
        if (mInstance == null) {
            mInstance = new ApiHandler();
        }

        return mInstance;
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    public Services getServices() {
        return mServices;
    }

    public interface Services {
        @GET("android-baking-app-json")
        Call<List<Model>> getRecipes();
    }
}

