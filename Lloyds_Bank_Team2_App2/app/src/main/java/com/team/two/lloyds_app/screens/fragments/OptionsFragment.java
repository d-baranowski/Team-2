
  package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby, Oliver McPheely
Date : 06/04/2015
Purpose : Options screen
 */

        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.Spinner;
        import android.widget.Switch;
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


public class OptionsFragment extends android.support.v4.app.Fragment {
    View Root;
    public static final String TITLE = "Options";

    //UI References
    private Switch notifications_switch;
    private TextView notifications_text;
    private Spinner FontSpinner;
    private String[] Font_size = {"1", "2", "3"};

    //facebook SDK References
    private CallbackManager mCallbackManager;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

    }


    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {
                TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
                fbMessage.setText("Welcome " + profile.getName() + "!");

            }
        }

        @Override
        public void onCancel() {
            TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
            fbMessage.setText(R.string.fb_not_logged_in_message);
        }

        @Override
        public void onError(FacebookException e) {
            TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
            fbMessage.setText(R.string.fb_not_logged_in_message);
        }

    };






    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Root = inflater.inflate(R.layout.fragment_options, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Spinner FontSpinner = (Spinner) Root.findViewById(R.id.font_size_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Font_size);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FontSpinner.setAdapter(adapter);
        return Root;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) getView().findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, mCallBack);
    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile=Profile.getCurrentProfile();

    }

    @Override
    public void onStop() {
        super.onStop();
        TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
        fbMessage.setText(R.string.fb_not_logged_in_message);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


    public void onNotificationsSwitchClicked(View view) {
        boolean on = ((Switch) view.findViewById(R.id.notifications_switch)).isChecked();

        if (on) {
            //enable notifications
        } else {
            //disable notifications
        }


    }


}
