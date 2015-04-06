package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby
Date : 06/04/2015
Purpose : Options
 */

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.two.lloyds_app.R;


public class OptionsFragment extends android.support.v4.app.Fragment {
    View Root;
    public static final String TITLE = "Options";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Root = inflater.inflate(R.layout.fragment_options, container, false);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return Root;
    }

}
