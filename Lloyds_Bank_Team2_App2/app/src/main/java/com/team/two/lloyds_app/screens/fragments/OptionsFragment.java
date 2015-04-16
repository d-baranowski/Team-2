
  package com.team.two.lloyds_app.screens.fragments;

/**
 * Author : Matthew Selby, Oliver McPheely
 * Date : 06/04/2015
 * Purpose : Options screen
 */

        import android.app.Notification;
        import android.app.NotificationManager;
        import android.app.PendingIntent;
        import android.content.Context;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.content.res.ColorStateList;
        import android.graphics.BitmapFactory;
        import android.graphics.Color;
        import android.os.Bundle;
        import android.text.Layout;
        import android.text.style.UpdateAppearance;
        import android.util.Size;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.Button;
        import android.widget.CompoundButton;
        import android.widget.RelativeLayout;
        import android.widget.Spinner;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;
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
        import com.team.two.lloyds_app.screens.activities.MainActivity;

        import java.awt.font.TextAttribute;

        import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


  public class OptionsFragment extends android.support.v4.app.Fragment implements AdapterView.OnItemSelectedListener {
      View Root;
      public static final String TITLE = "Options";

      //UI References

      private Spinner FontSpinner;
      private Button doneButton;

      public OptionsFragment() {
          // Required empty public constructor
      }

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
      return Root;
  }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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

      @Override
      public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
          if (position == 0) {
              TextView tv1 = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
              tv1.setTextColor(getResources().getColor(R.color.lloyds_green));
              TextView tv2 = (TextView) getActivity().findViewById(R.id.font_size_text);
              tv2.setTextColor(getResources().getColor(R.color.lloyds_green));
              FontSpinner.setSelection(position);
          }
          if (position ==1) {
              TextView tv1 = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
              tv1.setTextColor(getResources().getColor(R.color.black));
              TextView tv2 = (TextView) getActivity().findViewById(R.id.font_size_text);
              tv2.setTextColor(getResources().getColor(R.color.black));
              FontSpinner.setSelection(position);
          }
          if (position==2) {
              TextView tv1 = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
              TextView tv2 = (TextView) getActivity().findViewById(R.id.font_size_text);
              tv2.setTextColor(getResources().getColor(R.color.com_facebook_blue));
              tv1.setTextColor(getResources().getColor(R.color.com_facebook_blue));
              FontSpinner.setSelection(position);
          }

      }
      @Override
      public void onNothingSelected(AdapterView<?> parent) {

      }

  }
