package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby
Date : 03/04/2015
Purpose : Branch Finder
*/

/*Modified by Michael Edwards on 7/4/2015*/
/*Modified by Daniel Smith on 12/4/2015*/

import android.content.pm.ActivityInfo;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

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

    public BranchFinderFragment() {
        // Required empty public constructor
    }

    /*
    onCreateView() - Creates the screen by inflating layout.
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_branch_finder, container, false);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        initialiseBranches();
        createMapView();
        addMarkers();

        return root;
    }

    /*
initialiseBranches()-Read branch information from the text file to create Branch objects
 */
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

         /*
    createMapView()- Initialises the Map
     */

    private void createMapView() {
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {

            googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(
                    R.id.mapView)).getMap();

            /**
             * If the map is still null after attempted initialisation,
             * show an error to the user
             */
            if (googleMap == null) {
                Toast.makeText(getActivity(), "Error creating map", Toast.LENGTH_SHORT).show();
            }

            //Enable the My Location layer
            googleMap.setMyLocationEnabled(true);

            //Get the latest location
            Location lastLocation = ((MainActivity) getActivity()).mLastLocation;

            //Set up initial zoom to user's current location
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                    .target(new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude()))
                    .zoom(10)
                    .build()));

            //Create objects for the popup details
            final RelativeLayout popup = (RelativeLayout) root.findViewById(R.id.branch_popup);
            final TextView branchTitle = (TextView) root.findViewById(R.id.branch_title);
            final TextView addressTitle = (TextView) root.findViewById(R.id.branch_address_title);
            final TextView addressView = (TextView) root.findViewById(R.id.branch_address);
            final TextView timesTitle = (TextView) root.findViewById(R.id.branch_opening_times_title);
            final TextView timesView = (TextView) root.findViewById(R.id.branch_opening_times);

            //Set up the marker listener to manage marker clicks
            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {

                    //Get the respective branch object and get the address and opening times
                    Branch branch = branchMap.get(marker.getTitle());
                    String[] address = branch.getAddress().toStringArray();
                    String[] times = branch.getOpeningTimes();

                    //Zoom on the marker
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 15));


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

        } catch (NullPointerException exception) {
            Log.e("branchFinder", exception.toString());
        }
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


}
