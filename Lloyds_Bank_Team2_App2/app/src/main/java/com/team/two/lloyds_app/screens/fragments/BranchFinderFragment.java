package com.team.two.lloyds_app.screens.fragments;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.team.two.lloyds_app.R;



public class BranchFinderFragment extends android.support.v4.app.Fragment {


    public static final String TITLE = "Branch Finder";
    View Root;
    GoogleMap googleMap;

    /**
     * Initialises the mapview
     */
    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                /**
                 * If the map is still null after attempted initialisation,
                 * show an error to the user
                 */
                if(null == googleMap) {
                    Toast.makeText(getActivity(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }
    }

    /**
     * Adds a marker to the map
     */

   private void setZoom(){
       CameraPosition CP = new CameraPosition.Builder()
               .target(new LatLng(54.976479, -1.618589))
               .zoom(9)
               .build();

       googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CP));


   }

    private void addMarker(){

        /** Make sure that the map has been initialised **/
        if(null != googleMap){
            googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(54.976479, -1.618589))
                            .title("TEST Marker NEWCASTLE")
                            .draggable(true));
        }
    }


    public BranchFinderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Root = inflater.inflate(R.layout.fragment_branch_finder, container, false);
        createMapView();
        setZoom();
        addMarker();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        return Root;
    }


}
