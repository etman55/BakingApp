package com.example.android.bakingapp.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.Models.Model;
import com.example.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmResults;

/**
 * Created by Etman on 6/16/2017.
 */

public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.MyViewHolder> {
    private RealmResults<Model> contentList;
    private static OnItemClickListener listener;
    Context context;

    public RecipesAdapter(RealmResults<Model> contentList, Context context) {
        updateList(contentList);
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.title.setText(contentList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }

    public void updateList(RealmResults<Model> contentList) {
        this.contentList = contentList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.recipe_pic)
        ImageView pic;
        @BindView(R.id.recipe_title)
        TextView title;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v, getAdapterPosition());
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }

}
