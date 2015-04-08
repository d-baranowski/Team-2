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
                Branch branch = new Branch(scanner.nextLine(),Double.parseDouble(scanner.nextLine()), Double.parseDouble(scanner.nextLine()));
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

                final RelativeLayout popup = (RelativeLayout) root.findViewById(R.id.branch_popup);

                //Set up initial zoom to Newcastle area
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                        .target(new LatLng(54.976479, -1.618589))
                        .zoom(9)
                        .build()));

                //Set up the marker listener
                googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {
                        Branch branch = branchMap.get(marker.getTitle());
                        /*googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder()
                                .target(new LatLng(branch.getLongitude(), branch.getLongitude()))
                                .zoom(20)
                                .build()));*/

                        TextView branchTitle = (TextView) root.findViewById(R.id.branch_title);
                        TextView addressTitle = (TextView) root.findViewById(R.id.branch_address_title);
                        TextView address = (TextView) root.findViewById(R.id.branch_address);
                        TextView timesTitle = (TextView) root.findViewById(R.id.branch_opening_times_title);
                        TextView times = (TextView) root.findViewById(R.id.branch_opening_times);

                        branchTitle.setText(branch.getName());
                        addressTitle.setText(getResources().getString(R.string.branch_address_title));
                        address.setText("tempAddr");
                        timesTitle.setText(getResources().getString(R.string.branch_opening_times_title));
                        times.setText("tempTimes");

                        popup.setVisibility(View.VISIBLE);
                        return false;
                    }});

                //Set up map listener so the popup will be removed
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng ll) {
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
