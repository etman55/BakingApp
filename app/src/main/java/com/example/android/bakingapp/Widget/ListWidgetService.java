package com.example.android.bakingapp.Widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingapp.API.ApiHandler;
import com.example.android.bakingapp.Models.Model;
import com.example.android.bakingapp.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Etman on 6/21/2017.
 */

public class ListWidgetService extends RemoteViewsService {


    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListViewFactory(this.getApplicationContext());
    }

    class ListViewFactory implements RemoteViewsService.RemoteViewsFactory {
        private final String TAG = ListWidgetService.class.getSimpleName();
        Context context;
        List<Model> models = new ArrayList<>();
        private AppWidgetManager appWidgetManager;
        private int[] appWidgetId;

        public ListViewFactory(Context context) {
            this.context = context;
            appWidgetManager = AppWidgetManager.getInstance(context);
            ComponentName component = new ComponentName(context, RecipeWidget.class);
            appWidgetId = appWidgetManager.getAppWidgetIds(component);
        }

        @Override
        public void onCreate() {
            requestData();
        }

        @Override
        public void onDataSetChanged() {
        }

        @Override
        public void onDestroy() {
            models.clear();
        }

        @Override
        public int getCount() {
            return models.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (models == null || models.size() == 0) return null;
            Model model = models.get(position);
            long id = model.getId();
            String recipe = model.getName();
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_list_item);
            views.setTextViewText(R.id.recipe_name_widget, recipe);
            // Fill in the onClick PendingIntent Template using the specific plant Id for each item individually
            Bundle extras = new Bundle();
            extras.putLong("recipeId", id);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.recipe_name_widget, fillInIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        private void requestData() {
            ApiHandler mApiHandler = ApiHandler.getInstance();
            Call<List<Model>> getRecipes = mApiHandler.getServices().getRecipes();
            getRecipes.enqueue(new Callback<List<Model>>() {
                @Override
                public void onResponse(Call<List<Model>> call, Response<List<Model>> response) {
                    models = response.body();
                    appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipes_list_widget);
                }

                @Override
                public void onFailure(Call<List<Model>> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getMessage());
                }
            });

        }
    }

}
