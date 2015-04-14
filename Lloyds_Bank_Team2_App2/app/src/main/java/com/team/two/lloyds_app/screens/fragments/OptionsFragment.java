
        package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby, Oliver McPheely
Date : 06/04/2015
Purpose : Options
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
    private Button FB_share_button;
    private TextView options_title;
    private TextView notifications_text;
    private TextView link_facebook_text;
    private Spinner FontSpinner;

    //facebook SDK References
    private TextView fbWelcomeMessage;
    private CallbackManager mCallbackManager;
    private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null){
                fbWelcomeMessage.setText("Welcome " + profile.getName());
            }

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }

    };

    private String[] Font_size = {"1", "2", "3"};
    public OptionsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager=CallbackManager.Factory.create();
        }


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
