package com.team.two.lloyds_app.screens.fragments;

/*
Author : Matthew Selby
Date :
Purpose : Branch Finder
 */

/*Modified by Michael Edwards on 7/4/2015*/

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.objects.Branch;


public class BranchFinderFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Branch Finder";
    private View root;
    private GoogleMap googleMap;
    private HashMap<String, Branch> branchMap;

    public BranchFinderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_branch_finder, container, false);

        initialiseBranches();
        createMapView();
        addMarkers();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        return root;
    }

    //Read branch information from the text file to create Branch objects
    private void initialiseBranches(){
        branchMap = new HashMap<>();
        try {
            Scanner scanner = new Scanner(getResources().getAssets().open("branches.txt"));
            while(scanner.hasNextLine()) {
                Branch branch = new Branch();

                branch.setName(scanner.nextLine());
                branch.setLatitude(Double.parseDouble(scanner.nextLine()));
                branch.setLongitude(Double.parseDouble(scanner.nextLine()));
                branch.setAddress(scanner.nextLine(),scanner.nextLine(),scanner.nextLine(),scanner.nextLine());
                branch.setPhoneNumber(scanner.nextLine());
                branch.setOpeningTimes(scanner.nextLine(),scanner.nextLine(),scanner.nextLine(),scanner.nextLine(),scanner.nextLine(),scanner.nextLine(),scanner.nextLine());

                branchMap.put(branch.getName(), branch);
             }
        } catch(IOException ioe){
            Log.e("branchFinder", ioe.toString());
        }
    }

    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(googleMap == null){
                googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(googleMap == null) {
                    Toast.makeText(getActivity(), "Error creating map", Toast.LENGTH_SHORT).show();
                }

                //Set up initial zoom to Newcastle area
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(54.976479, -1.618589))
                        .zoom(9)
                        .build()));

                final RelativeLayout popup = (RelativeLayout) root.findViewById(R.id.branch_popup);
                final TextView branchTitle = (TextView) root.findViewById(R.id.branch_title);
                final TextView addressTitle = (TextView) root.findViewById(R.id.branch_address_title);
                final TextView addressView = (TextView) root.findViewById(R.id.branch_address);
                final TextView timesTitle = (TextView) root.findViewById(R.id.branch_opening_times_title);
                final TextView timesView = (TextView) root.findViewById(R.id.branch_opening_times);

                //Set up the marker listener
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        //Clear any previous popup
                        timesView.setText("");
                        addressView.setText("");

                        //Get the respective branch object and get the address and opening times
                        Branch branch = branchMap.get(marker.getTitle());
                        String[] address = branch.getAddress();
                        String[] times = branch.getOpeningTimes();

                        //Add the appropriate text to the popup
                        branchTitle.setText(branch.getName());

                        addressTitle.setText(getResources().getString(R.string.branch_address_title));
                        for(String s:address){
                            addressView.append(s + "\n");
                        }
                        addressView.append("\n" + branch.getPhoneNumber());

                        timesTitle.setText(getResources().getString(R.string.branch_opening_times_title));
                        for(String s:times){
                            timesView.append(s + "\n");
                        }

                        //Show the popup
                        popup.setVisibility(View.VISIBLE);

                        return false;
                    }});

                //Set up map listener so the popup will be removed once clicked off
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng ll) {
                        //Hide the popup
                        popup.setVisibility(View.INVISIBLE);
                    }});
            }
        } catch (NullPointerException exception){
            Log.e("branchFinder", exception.toString());
        }
    }

    private void addMarkers(){

        /** Make sure that the map has been initialised **/
        if(googleMap != null){

           for(Branch b: branchMap.values()){
                              googleMap.addMarker(new MarkerOptions()
                                      .position(new LatLng(b.getLatitude(), b.getLongitude()))
                                      .title(b.getName())
                                      .icon(BitmapDescriptorFactory.fromResource(R.drawable.finalmarker))
                                      .draggable(false));
           }


        }
    }

}
