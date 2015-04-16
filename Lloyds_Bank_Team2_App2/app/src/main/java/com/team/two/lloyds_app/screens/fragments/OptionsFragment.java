package com.team.two.lloyds_app.screens.fragments;

/**
 * Author : Matthew Selby, Oliver McPheely
 * Date : 06/04/2015
 * Purpose : Options screen
 */


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.team.two.lloyds_app.R;
import com.facebook.FacebookSdk;


import static com.team.two.lloyds_app.R.color.black;
import static com.team.two.lloyds_app.R.color.com_facebook_blue;
import static com.team.two.lloyds_app.R.color.lloyds_green;
import static com.team.two.lloyds_app.R.layout.fragment_options;


public class OptionsFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener{
	View Root;
	public static final String TITLE = "Options";

	//UI References

	private Spinner FontSpinner;
	private Button doneButton;

	public OptionsFragment(){
		// Required empty public constructor
	}

	//facebook SDK References
	private CallbackManager mCallbackManager;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
		mCallbackManager = CallbackManager.Factory.create();


	}


	private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>(){
		@Override
		public void onSuccess(LoginResult loginResult){
			AccessToken accessToken = loginResult.getAccessToken();
			Profile profile = Profile.getCurrentProfile();
			if(profile != null){
				TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
				fbMessage.setText("Welcome " + profile.getName() + "!");

			}
		}

		@Override
		public void onCancel(){
			TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
			fbMessage.setText(R.string.fb_not_logged_in_message);
		}

		@Override
		public void onError(FacebookException e){
			TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
			fbMessage.setText(R.string.fb_not_logged_in_message);
		}
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
		Root = inflater.inflate(fragment_options, container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		return Root;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState){
		super.onViewCreated(view, savedInstanceState);
		LoginButton loginButton = (LoginButton) getView().findViewById(R.id.login_button);
		loginButton.setReadPermissions("user_friends");
		loginButton.setFragment(this);
		loginButton.registerCallback(mCallbackManager, mCallBack);
		FontSpinner = (Spinner) Root.findViewById(R.id.font_size_spinner);
		ArrayAdapter adapter = ArrayAdapter.createFromResource(this.getActivity(), R.array.font_color, android.R.layout.simple_spinner_dropdown_item);
		FontSpinner.setAdapter(adapter);
		FontSpinner.setOnItemSelectedListener(this);
	}

	@Override
	public void onResume(){
		super.onResume();
		Profile profile = Profile.getCurrentProfile();
	}

	@Override
	public void onStop(){
		super.onStop();
		TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
		fbMessage.setText(R.string.fb_not_logged_in_message);
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data){
		super.onActivityResult(requestCode, resultCode, data);
		mCallbackManager.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
		View v;
		ViewGroup viewGroup = (ViewGroup) getActivity().findViewById(R.id.drawer_layout);
		viewGroup.getChildCount();
		if(position == 0){
			for(int i = 0; i < viewGroup.getChildCount(); i++){
				v = viewGroup.getChildAt(i);
				if(view instanceof TextView){
					((TextView) view).setTextColor(lloyds_green);
				}
				if(view instanceof Button){
					((Button) view).setTextColor(lloyds_green);
				}
				FontSpinner.setSelection(position);
			}

			if(position == 1){
				for(int i = 0; i < viewGroup.getChildCount(); i++){
					v = viewGroup.getChildAt(i);
					if(view instanceof TextView){
						((TextView) view).setTextColor(black);
					}
					if(view instanceof Button){
						((Button) view).setTextColor(black);
					}
					FontSpinner.setSelection(position);
				}

				if(position == 2){
					for(int i = 0; i < viewGroup.getChildCount(); i++){
						v = viewGroup.getChildAt(i);
						if(view instanceof TextView){
							((TextView) view).setTextColor(com_facebook_blue);
						}
						if(view instanceof Button){
							((Button) view).setTextColor(com_facebook_blue);
						}
						FontSpinner.setSelection(position);
					}
				}
			}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent){

	}

}
