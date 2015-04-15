package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby
Date : 03/04/2015
Purpose : Branch Finder
*/

/*Modified by Michael Edwards on 7/4/2015, 15/4/2015*/
/*Modified by Daniel Smith on 12/4/2015*/

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Address;
import com.team.two.lloyds_app.objects.Branch;
import com.team.two.lloyds_app.screens.activities.MainActivity;

import java.io.IOException;
import java.net.ConnectException;
import java.util.HashMap;
import java.util.Scanner;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class BranchFinderFragment extends Fragment {
    public static final String TITLE = "Branch Finder";
    private View root;
    private GoogleMap googleMap;
    private HashMap<String, Branch> branchMap;

    //Set up objects for the popup details
    RelativeLayout popup;
    TextView branchTitle;
    TextView addressTitle;
    TextView addressView;
    TextView timesTitle;
    TextView timesView;

    public BranchFinderFragment() {
        // Required empty public constructor
    }

    //Create the screen by inflating layout
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_branch_finder, container, false);

        //Get objects for the popup details
        popup = (RelativeLayout) root.findViewById(R.id.branch_popup);
        branchTitle = (TextView) root.findViewById(R.id.branch_title);
        addressTitle = (TextView) root.findViewById(R.id.branch_address_title);
        addressView = (TextView) root.findViewById(R.id.branch_address);
        timesTitle = (TextView) root.findViewById(R.id.branch_opening_times_title);
        timesView = (TextView) root.findViewById(R.id.branch_opening_times);

        //Set the orientation to portrait
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //Set up the button listener
        setSearchButtonListener();

        //Attempt to create the map
        try {
            //Create the map
            createMapView();

            //Add the markers for the branches
            addBranchMarkers();
        }
        //If a network error has been thrown, notify user
        catch (ConnectException ce) {
            Toast.makeText(getActivity(), ce.getMessage(), Toast.LENGTH_LONG).show();
            return root;
        }
        //If the map failed to initiate, notify user
        catch (NullPointerException npe) {
            Toast.makeText(getActivity(), npe.getMessage(), Toast.LENGTH_LONG).show();
        }

        return root;
    }

    //Method to respond to search button press
    private void setSearchButtonListener() {

        //Hide the keyboard (credit: http://stackoverflow.com/questions/3400028/close-virtual-keyboard-on-button-press)

        //Get search button object
        Button button = (Button) root.findViewById(R.id.branch_search_button);

        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        //Set up the button to respond to click
        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //Get the input from the search bar
                String input = ((EditText) root.findViewById(R.id.branch_search_bar_input)).getText().toString();

                //Make sure there is input to process
                if (!input.isEmpty() || input.equals(null)) {

                    try {
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                        //Hide the popup if it is open
                        popup.setVisibility(View.INVISIBLE);

                        //Move the map to the desired location
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(getSearchPosition(input))
                                .zoom(9)
                                .build()));

                    } catch (Exception e) {
                        //If there was a network error, notify user
                        if (e.getClass().equals(ConnectException.class)) {
                            Toast.makeText(getActivity(), getString(R.string.branch_network_search_error), Toast.LENGTH_SHORT).show();
                        }
                        //If there was an error during the search, notify user
                        else {
                            Toast.makeText(getActivity(), getString(R.string.branch_general_search_error), Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    //Tell the user to input something
                    Toast.makeText(getActivity(), getString(R.string.branch_empty_search_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Attempt to generate the map
    private void createMapView() throws ConnectException {

        if (!isNetworkAvailable()) {
            throw new ConnectException(getString(R.string.branch_network_search_error));
        }
        googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView)).getMap();

        if (googleMap == null) {
            throw new NullPointerException(getString(R.string.branch_general_map_error));
        }

        //Enable the My Location layer
        googleMap.setMyLocationEnabled(true);

        //Get the latest location
        Location lastLocation = ((MainActivity) getActivity()).mLastLocation;
        if (lastLocation == null) {
            Toast.makeText(getActivity(), getString(R.string.branch_gps_search_error), Toast.LENGTH_SHORT).show();
        }

        //Set up initial zoom to user's current location
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                .target(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                .zoom(10)
                .build()));

        //Set up the marker listener to manage marker clicks
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //Get the respective branch object and get the address and opening times
                Branch branch = branchMap.get(marker.getTitle());
                String[] address = branch.getAddress().toStringArray();
                String[] times = branch.getOpeningTimes();

                //Focus map on the marker
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 11));

                //Clear any previous popup
                timesView.setText("");
                addressView.setText("");

                //Populate the popup with branch details
                branchTitle.setText(branch.getName());

                addressTitle.setText(getResources().getString(R.string.branch_address_title));
                for (String s : address) {
                    addressView.append(s + "\n");
                }
                addressView.append("\n" + branch.getPhoneNumber());

                timesTitle.setText(getResources().getString(R.string.branch_opening_times_title));
                for (String s : times) {
                    timesView.append(s + "\n");
                }

                //Display the popup
                popup.setVisibility(View.VISIBLE);

                return false;
            }
        });

        //Set up map listener so the popup will be removed once clicked off
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng ll) {
                //Hide the popup
                popup.setVisibility(View.INVISIBLE);
            }
        });
    }

    //Read branch information from the text file to create Branch objects
    private void addBranchMarkers() {

        //Make sure that the map has been initialised
        if (googleMap == null) {
            return;
        }

        branchMap = new HashMap<>();

        try {
            //Open the data file for parsing
            Scanner scanner = new Scanner(getResources().getAssets().open("branches.txt"));

            //Create Branch objects by parsing the data file
            while (scanner.hasNextLine()) {

                Branch branch = new Branch(
                        scanner.nextLine(),
                        Double.parseDouble(scanner.nextLine()),
                        Double.parseDouble(scanner.nextLine()),
                        new Address(scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine()),
                        scanner.nextLine(),
                        new String[]{scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine(), scanner.nextLine()});

                branchMap.put(branch.getName(), branch);

            }

        } catch (IOException ioe) {
            //There was an error parsing the data: notify the user
            Toast.makeText(getActivity(), getString(R.string.branch_data_error), Toast.LENGTH_SHORT).show();
            return;
        }

        //Add a marker for each branch in the branch map
        for (Branch b : branchMap.values()) {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(b.getLatitude(), b.getLongitude()))
                    .title(b.getName())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.finalmarker))
                    .draggable(false));
        }
    }

    //Check whether the user is connected to the internet
    //Credit: http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) (getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    //Attempt to get a usable location from the user's input
    private LatLng getSearchPosition(String input) throws IOException {

        //If there is no network connection then notify user and do not continue
        if (!isNetworkAvailable()) {
            throw new ConnectException();
        }

        Geocoder gc = new Geocoder(getActivity());

        //Get the first result from the Geocoder
        android.location.Address address = gc.getFromLocationName(input, 1).get(0);

        return new LatLng(address.getLatitude(), address.getLongitude());
    }

}
