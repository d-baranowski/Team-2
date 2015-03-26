package com.team.two.lloyds_app.screens.fragments;

import com.team.two.lloyds_app.R;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.*;

import java.util.ArrayList;
import java.util.Calendar;

public class MoneyPlannerFragment extends android.support.v4.app.Fragment {

    public static final String TITLE = "Money Planner";
    private View root;
    private final int POINTS_TO_PLOT = 7;

    public static MoneyPlannerFragment newInstance() {
        return new MoneyPlannerFragment();
    }

    public MoneyPlannerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_money_planner, container, false);

        //Implementation
        generateGraph();
        generateSummary();
        return root;
    }

    private void generateGraph(){
        DataPoint[] points = getPoints();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(points);
        GraphView graph = (GraphView) root.findViewById(R.id.graph);
        graph.addSeries(series);
        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis((long) value);
                if (isValueX) {
                    return calendar.get(Calendar.DAY_OF_MONTH) + "/"
                            + (calendar.get(Calendar.MONTH) + 1);
                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });
        series.setDrawDataPoints(true);
        graph.getGridLabelRenderer().setNumHorizontalLabels(POINTS_TO_PLOT);

        graph.getViewport().setMinX(points[0].getX());
        graph.getViewport().setMaxX(points[POINTS_TO_PLOT - 1].getX());
        graph.getViewport().setXAxisBoundsManual(true);

    }

    private DataPoint[] getPoints(){
        Calendar calendar = Calendar.getInstance();
        ArrayList<DataPoint> sampleDates = new ArrayList<DataPoint>();
        for(int i = 0; i < POINTS_TO_PLOT; i++){
            sampleDates.add(new DataPoint(calendar.getTime(), i));
            calendar.add(Calendar.DATE, 1);
        }
        DataPoint[] arr = new DataPoint[sampleDates.size()];
        return sampleDates.toArray(arr);
    }

    private void generateSummary(){
        TextView average = (TextView) root.findViewById(R.id.spending_average);
        average.setText("20");
    }
}
