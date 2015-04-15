
  package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby, Oliver McPheely
Date : 06/04/2015
Purpose : Options screen
 */

        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.graphics.BitmapFactory;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.Spinner;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.ToggleButton;

        import com.facebook.AccessToken;
        import com.facebook.CallbackManager;
        import com.facebook.FacebookCallback;
        import com.facebook.FacebookException;
        import com.facebook.Profile;
        import com.facebook.login.LoginResult;
        import com.facebook.login.widget.LoginButton;
        import com.team.two.lloyds_app.R;
        import com.facebook.FacebookSdk;

        import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


  public class OptionsFragment extends android.support.v4.app.Fragment implements CompoundButton.OnCheckedChangeListener {
    View Root;
    public static final String TITLE = "Options";

    //UI References
    private ToggleButton notificationsToggle;
    private Spinner FontSpinner;
    private String[] Font = {"1", "2", "3"};
    private Button doneButton;


    //facebook SDK References
    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();
        notificationsToggle = (ToggleButton) getActivity().findViewById(R.id.notifications_toggle);

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
        final Spinner FontSpinner = (Spinner) Root.findViewById(R.id.font_size_spinner);
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Font);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FontSpinner.setAdapter(adapter);


        AdapterView.OnItemSelectedListener onSpinner = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String font1 = "1";
                String font2="2";
                String font3="3";
                if (((String) parent.getSelectedItem()) == font1 ) {
                    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Exo2-Bold.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build());
                }
                if ((String)parent.getSelectedItem() == font2) {
                    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/Oxygen-Bold.otf")
                            .setFontAttrId(R.attr.fontPath)
                            .build());
                }
                if ((String)parent.getSelectedItem() == font3) {
                    CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/TitilliumWeb-Bold.ttf")
                            .setFontAttrId(R.attr.fontPath)
                            .build());

                }

            }
                @Override
                public void onNothingSelected (AdapterView < ? > parent){

                }


        };

        FontSpinner.setOnItemSelectedListener(onSpinner);
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
        Profile profile = Profile.getCurrentProfile();

    }

    @Override
    public void onStop() {
        super.onStop();
        TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
        fbMessage.setText(R.string.fb_not_logged_in_message);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    //method used when toggle button is set/unset
    @Override
    public void onCheckedChanged(CompoundButton notificationsToggle, boolean isChecked) {
        notificationsToggle.setOnCheckedChangeListener(this);
        if (isChecked = true) {
            Intent intent = new Intent();
            PendingIntent pIntent = PendingIntent.getActivity(this.getActivity(), 0, intent, 0);
            Notification testNotification = new Notification.Builder(this.getActivity())
                    .setTicker("Notifications On")
                    .setContentTitle("You have switched On Notifications")
                    .setSmallIcon(R.drawable.lloydsbanklogo)
                    .setContentText("Notifications On")
                    .setContentIntent(pIntent)
                    .build();
            testNotification.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
            notificationManager.notify(0, testNotification);
        } else {
            Intent intent = new Intent();
            PendingIntent pIntent = PendingIntent.getActivity(this.getActivity(), 0, intent, 0);
            Notification testNotification = new Notification.Builder(this.getActivity())
                    .setTicker("Notifications Off")
                    .setContentTitle("You have switched off Notifications")
                    .setSmallIcon(R.drawable.lloydsbanklogo)
                    .setContentText("Notifications Off")
                    .setContentIntent(pIntent)
                    .build();
            testNotification.flags = Notification.FLAG_AUTO_CANCEL;
            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
            notificationManager.notify(1, testNotification);
        }
    }



}
