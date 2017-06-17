package com.example.android.bakingapp.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.Models.Step;
import com.example.android.bakingapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;

public class StepFragment extends Fragment {
    private static final String TAG = StepFragment.class.getSimpleName();
    private String desc;
    private String videoUrl;
    private String thumbnailUrl;

    @BindView(R.id.step_desc)
    TextView stepDesc;
    @BindView(R.id.step_image)
    ImageView stepImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public void getStepInfo(String description, String videoURL, String thumbnailURL) {
        desc = description;
        videoUrl = videoURL;
        thumbnailUrl = thumbnailURL;
        if (desc != null)
            stepDesc.setText(desc);
    }
}
