package com.team.two.lloyds_app.screens.fragments;

import com.team.two.lloyds_app.R;

import android.graphics.Color;
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
    private final int BALANCE_POINTS_TO_PLOT = 7;
    private final int SPENDING_POINTS_TO_PLOT = 7;

    public MoneyPlannerFragment() {
        // Required empty public constructor
    }

    @SuppressWarnings("EmptyMethod")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_money_planner, container, false);

        generateBalanceGraph();
        generateSpendingGraph();
        generateSummary();

        return root;
    }

    private void generateBalanceGraph(){

        //Create graph object
        GraphView graph = (GraphView) root.findViewById(R.id.balance_graph);

        //Create balance series and add to graph
        DataPoint[] points = getBalancePoints();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setTitle(getString(R.string.balance_graph_title));
        series.setDrawDataPoints(true);
        graph.addSeries(series);

        //Configure graph
        formatLabelRenderer(graph.getGridLabelRenderer());
        graph.getViewport().setMinX(points[0].getX());
        graph.getViewport().setMaxX(points[BALANCE_POINTS_TO_PLOT - 1].getX());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getLegendRenderer().setVisible(true);

    }

    private void generateSpendingGraph(){

        //Create graph object
        GraphView graph = (GraphView) root.findViewById(R.id.spending_graph);

        //Create balance series and add to graph
        DataPoint[] points = getBalancePoints();
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        series.setTitle(getString(R.string.spending_graph_title));
        series.setDrawDataPoints(true);
        graph.addSeries(series);

        //Configure graph
        formatLabelRenderer(graph.getGridLabelRenderer());
        graph.getViewport().setMinX(points[0].getX());
        graph.getViewport().setMaxX(points[SPENDING_POINTS_TO_PLOT - 1].getX());
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getLegendRenderer().setVisible(true);
    }

    private DataPoint[] getBalancePoints(){
        Calendar calendar = Calendar.getInstance();
        ArrayList<DataPoint> sampleDates = new ArrayList<>();
        for(int i = 0; i < BALANCE_POINTS_TO_PLOT; i++){
            sampleDates.add(new DataPoint(calendar.getTime(), i*100));
            calendar.add(Calendar.DATE, 1);
        }
        DataPoint[] arr = new DataPoint[sampleDates.size()];
        return sampleDates.toArray(arr);
    }

    private DataPoint[] getSpendingPoints(){
        Calendar calendar = Calendar.getInstance();
        ArrayList<DataPoint> sampleDates = new ArrayList<>();
        for(int i = 0; i < SPENDING_POINTS_TO_PLOT; i++){
            sampleDates.add(new DataPoint(calendar.getTime(), i));
            calendar.add(Calendar.DATE, 1);
        }
        DataPoint[] arr = new DataPoint[sampleDates.size()];
        return sampleDates.toArray(arr);
    }

    private String getAverageSpending(){
        return "£" + 20;
    }

    private void generateSummary(){
        TextView average = (TextView) root.findViewById(R.id.summary);
        average.setText(getString(R.string.summary_1) + getAverageSpending() + getString(R.string.summary_2));
        average.append(getString(R.string.overdraft_safe));
    }

    private void formatLabelRenderer(GridLabelRenderer gridLabelRenderer){
        gridLabelRenderer.setLabelFormatter(getLabelFormatter());
        gridLabelRenderer.setNumHorizontalLabels(BALANCE_POINTS_TO_PLOT);
        gridLabelRenderer.setGridColor(getResources().getColor(R.color.lloyds_green));
        gridLabelRenderer.setHorizontalLabelsColor(Color.BLACK);
        gridLabelRenderer.setVerticalLabelsColor(Color.BLACK);
    }

    public LabelFormatter getLabelFormatter() {
        return new DefaultLabelFormatter() {
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis((long) value);
                    return "\n" + calendar.get(Calendar.DAY_OF_MONTH) + "/"
                            + (calendar.get(Calendar.MONTH) + 1);
                } else {
                    return "£" + value+ " ";
                }
            }
        };
    }
}
