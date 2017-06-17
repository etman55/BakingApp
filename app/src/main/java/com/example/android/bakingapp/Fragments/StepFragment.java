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
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_DESC = "desc";
    private static final String ARG_VIDEO_URL = "videoUrl";
    private static final String ARG_THUMBNAIL_URL = "thumbnailUrl";
    private String desc;
    private String videoUrl;
    private String thumbnailUrl;

    @BindView(R.id.step_desc)
    TextView stepDesc;
    @BindView(R.id.step_image)
    ImageView stepImage;
    private Realm realm;
    private Step step;

    public StepFragment() {
    }

    public static StepFragment newInstance(String description, String videoURL, String thumbnailURL) {
        StepFragment fragment = new StepFragment();
        Bundle args = new Bundle();
        args.putString(ARG_DESC, description);
        args.putString(ARG_VIDEO_URL, videoURL);
        args.putString(ARG_THUMBNAIL_URL, thumbnailURL);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            desc = getArguments().getString(ARG_DESC);
            videoUrl = getArguments().getString(ARG_VIDEO_URL);
            thumbnailUrl = getArguments().getString(ARG_THUMBNAIL_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        ButterKnife.bind(this, rootView);
        if (desc != null)
            stepDesc.setText(desc);

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
