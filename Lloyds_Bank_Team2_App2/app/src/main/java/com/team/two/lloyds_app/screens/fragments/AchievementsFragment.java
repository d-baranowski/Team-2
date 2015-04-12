package com.team.two.lloyds_app.screens.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.two.lloyds_app.R;

public class AchievementsFragment extends android.support.v4.app.Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    String mGreeting = "You are not signed in.";
    Listener mListener = null;

    // test comment added - to be removed
    public static final String TITLE = "Achievements";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types and number of parameters
    public static AchievementsFragment newInstance(String param1, String param2) {
        AchievementsFragment fragment = new AchievementsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AchievementsFragment() {
        // Required empty public constructor
    }

    public interface Listener {
        public void onShowAchievementsRequested();
    }

    public void setListener(Listener l) {
        mListener = l;
    }

    public void setGreeting(String greeting) {
        mGreeting = greeting;
        //updateUi();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_achievements:
                mListener.onShowAchievementsRequested();
                break;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_achievements, container, false);
    }
}
