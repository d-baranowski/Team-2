
        package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby, Oliver McPheely
Date : 06/04/2015
Purpose : Options
 */

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


    private String[] Font_size = {"1", "2", "3"};
    public OptionsFragment() {
        // Required empty public constructor
    }




    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        Root = inflater.inflate(R.layout.fragment_options, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Spinner FontSpinner = (Spinner) Root.findViewById(R.id.font_size_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.support_simple_spinner_dropdown_item, Font_size);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        FontSpinner.setAdapter(adapter);
        return Root;
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
