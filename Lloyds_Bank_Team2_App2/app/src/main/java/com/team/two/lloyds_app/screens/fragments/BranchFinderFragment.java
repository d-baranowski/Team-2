package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby
Date : 03/04/2015
Purpose : Branch Finder
*/

/*Modified by Michael Edwards on 7/4/2015*/
/*Modified by Daniel Smith on 12/4/2015*/

import java.net.ConnectException;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.location.Location;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Branch;
import com.team.two.lloyds_app.objects.Address;
import com.team.two.lloyds_app.screens.activities.MainActivity;


public class BranchFinderFragment extends Fragment {
    public static final String TITLE = "Branch Finder";
    private View root;
    private GoogleMap googleMap;
    private HashMap<String, Branch> branchMap;

    //Create objects for the popup details
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

        //Create objects for the popup details
        popup = (RelativeLayout) root.findViewById(R.id.branch_popup);
        branchTitle = (TextView) root.findViewById(R.id.branch_title);
        addressTitle = (TextView) root.findViewById(R.id.branch_address_title);
        addressView = (TextView) root.findViewById(R.id.branch_address);
        timesTitle = (TextView) root.findViewById(R.id.branch_opening_times_title);
        timesView = (TextView) root.findViewById(R.id.branch_opening_times);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setSearchButtonListener();

        initialiseBranches();

        try {
            createMapView();
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

        addMarkers();

        return root;
    }

    //Method to respond to search button press
    private void setSearchButtonListener() {

        Button button = (Button) root.findViewById(R.id.branch_search_button);

        final InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String input = ((EditText) root.findViewById(R.id.branch_search_bar_input)).getText().toString();

                if (!input.isEmpty() || input.equals(null)) {

                    try{
                        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                        popup.setVisibility(View.INVISIBLE);
                        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                            .target(getSearchPosition(input))
                            .zoom(9)
                            .build()));
                    }

                    catch(Exception e){
                        if(e.getClass().equals(ConnectException.class)){
                            Toast.makeText(getActivity(), getString(R.string.branch_network_search_error), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.branch_general_search_error), Toast.LENGTH_SHORT).show();
                        }
                    }


                } else {
                    Toast.makeText(getActivity(), getString(R.string.branch_empty_search_error), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Read branch information from the text file to create Branch objects
    private void initialiseBranches() {

        branchMap = new HashMap<>();

        try {

            Scanner scanner = new Scanner(getResources().getAssets().open("branches.txt"));

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
            Log.e("branchFinder", ioe.toString());
        }
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


    /*
    addMarkers() - Adds the branch markers to the map.
     */
    private void addMarkers() {

        /** Make sure that the map has been initialised **/
        if (googleMap != null) {

            //Add a marker for each branch in the branch map
            for (Branch b : branchMap.values()) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(b.getLatitude(), b.getLongitude()))
                        .title(b.getName())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.finalmarker))
                        .draggable(false));
            }

        }
    }

    //Check whether the user is connected to the internet
    //Credit: stackoverflow (http://stackoverflow.com/questions/4238921/detect-whether-there-is-an-internet-connection-available-on-android)
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) (getActivity().getSystemService(Context.CONNECTIVITY_SERVICE));
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private LatLng getSearchPosition(String input) throws IOException {

        Geocoder gc = new Geocoder(getActivity());

        if(!isNetworkAvailable()){
            throw new ConnectException();
        }

        android.location.Address address = gc.getFromLocationName(input, 1).get(0);

        return new LatLng(address.getLatitude(), address.getLongitude());

    }


}
