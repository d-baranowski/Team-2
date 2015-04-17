
  package com.team.two.lloyds_app.screens.fragments;

/**
 * Author : Oliver McPheely
 * Date : 06/04/2015
 * Purpose : Options screen
 */

        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.os.Bundle;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
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

        import static com.team.two.lloyds_app.R.layout.fragment_options;


  public class OptionsFragment extends android.support.v4.app.Fragment {
      View Root;
     public static final String TITLE = "Options and About";
     public OptionsFragment() {
          // Required empty public constructor
      }
      // CallBackManager used to route calls back to the Facebook SDK and your registered callbacks.
      private CallbackManager mCallbackManager;

      @Override
      public void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
          mCallbackManager = CallbackManager.Factory.create();

      }




    //handles the result of the attempted facebook login
      private FacebookCallback<LoginResult> mCallBack = new FacebookCallback<LoginResult>() {
        // on a successful login, set welcome message. loginResult parameter now has the new access token,
        // and the currently granted/declined permissions.
        @Override
          public void onSuccess(LoginResult loginResult) {
              AccessToken accessToken = loginResult.getAccessToken();
              Profile profile = Profile.getCurrentProfile();
              if (profile != null) {
                  TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
                  fbMessage.setText("Welcome " + profile.getName() + "!");

              }
          }
          //cancel, reset message.
          @Override
          public void onCancel() {
              TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
              fbMessage.setText(R.string.fb_not_logged_in_message);
          }
          //error throws an exception and resets message.
          @Override
          public void onError(FacebookException e) {
              TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
              fbMessage.setText(R.string.fb_not_logged_in_message);
          }
      };
      //Create the screen by inflating layout
      public View onCreateView(LayoutInflater inflater, ViewGroup container,
                               Bundle savedInstanceState) {
          Root = inflater.inflate(fragment_options, container, false);
          getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
          return Root;
      }

     //Creates the login with facebook button. Requests permission to access the users friends. Callback manager handles login responses.
      @Override
      public void onViewCreated(View view, Bundle savedInstanceState) {
          super.onViewCreated(view, savedInstanceState);
          LoginButton loginButton = (LoginButton) getView().findViewById(R.id.login_button);
          loginButton.setReadPermissions("user_friends");
          loginButton.setFragment(this);
          loginButton.registerCallback(mCallbackManager, mCallBack);

          }

      //keeps the current facebook profile logged in.
      @Override
      public void onResume() {
          super.onResume();
          Profile profile = Profile.getCurrentProfile();
      }

      //Resets the facebook welcome message.
      @Override
      public void onStop() {
          super.onStop();
          TextView fbMessage = (TextView) getActivity().findViewById(R.id.fbLoginMessage);
          fbMessage.setText(R.string.fb_not_logged_in_message);
      }

      //onActivityResult forwards login results to callbackmanager, created in onCreate.
      public void onActivityResult(int requestCode, int resultCode, Intent data) {
          super.onActivityResult(requestCode, resultCode, data);
          mCallbackManager.onActivityResult(requestCode, resultCode, data);
      }

  }