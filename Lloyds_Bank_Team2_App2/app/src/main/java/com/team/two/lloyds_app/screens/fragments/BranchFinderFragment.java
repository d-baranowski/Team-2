package com.team.two.lloyds_app.screens.fragments;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import com.team.two.lloyds_app.R;



public class BranchFinderFragment extends android.support.v4.app.Fragment {
    public static final String TITLE = "Branch Finder";
    View Root;
    GoogleMap googleMap;
    Marker Markertest, Markertest2, Markertest3;

    /**
     * CreateMapView()
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
     * setZoom()
     */

   private void setZoom(){
       CameraPosition CP = new CameraPosition.Builder()
               .target(new LatLng(54.976479, -1.618589))
               .zoom(9)
               .build();

       googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(CP));


   }
/*
    addMarker()
 */
    private void addMarker(){


        /** Make sure that the map has been initialised **/
        if(null != googleMap){
           Markertest = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(54.976479, -1.618589))
                    .title("LLOYDS BANKING GROUP")
                    .snippet("Newcastle City Centre Branch")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.finalmarker))
                    .draggable(false));

           Markertest2 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(54.912538, -1.384838))
                    .title("LLOYDS BANKING GROUP")
                    .snippet("Sunderland City Centre Branch")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.finalmarker))
                    .draggable(false));

            Markertest3 = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(54.903310, -1.531391))
                    .title("LLOYDS BANKING GROUP")
                    .snippet("Washington Shopping Centre Branch")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.finalmarker))
                    .draggable(false));


            googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                @Override
                public boolean onMarkerClick(Marker marker) {
                    if (marker.equals(Markertest))
                    {
                        Toast.makeText(getActivity(), "TEST CLICK Marker 1",Toast.LENGTH_SHORT).show();
                    } else if (marker.equals(Markertest2)){
                        Toast.makeText(getActivity(), "TEST CLICK MARKER 2",Toast.LENGTH_SHORT).show();
                    } else if (marker.equals(Markertest3)){
                        Toast.makeText(getActivity(), "TEST CLICK MARKER 3",Toast.LENGTH_SHORT).show();
                    }
                    return false;
            }});
        }
    }
 /*
    openDetails()
  */
    private void openDetails(){
        Toast.makeText(getActivity(),
                "Testing Click Method", Toast.LENGTH_SHORT).show();
    }



    public BranchFinderFragment() {
        // Required empty public constructor
    }

    /*
    onCreateView()
     */
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
