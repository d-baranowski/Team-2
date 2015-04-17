package com.team.two.lloyds_app.screens.fragments;

/**
 * Author: Matthew Selby
 * Date: 07/04/15
 * Purpose Help Screen
 */

/*Modified by Oliver McPheely*/


import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.team.two.lloyds_app.R;


public class HelpScreenFragment extends android.support.v4.app.Fragment{
	View Root;
	public static final String TITLE = "Help";

	public HelpScreenFragment(){
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		Root = inflater.inflate(R.layout.fragment_help_screen, container, false);

		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		return Root;

	}


}
