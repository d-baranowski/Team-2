package com.team.two.lloyds_app.screens.fragments;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.screens.activities.MainActivity;
import android.util.Log;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.*;
import android.widget.TextView;

import com.jjoe64.graphview.*;
import com.jjoe64.graphview.series.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TreeMap;

public class MoneyPlannerFragment extends android.support.v4.app.Fragment {

    private View root;
    public static final String TITLE = "Money Planner";

    private final DateFormat df = new SimpleDateFormat("dd-MM", Locale.ENGLISH);
    private final int KNOWN_BALANCE_DAYS_TO_PLOT = 16;
    private final int NUMBER_OF_DATE_LABELS = 5;
    private Calendar DATE_CONSIDERED_NOW = Calendar.getInstance();

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

        DATE_CONSIDERED_NOW.set(2015,Calendar.FEBRUARY, 15);

        generateBalanceGraph();
       // generateSpendingGraph();
        generateSummary();

        return root;
    }

    private void generateGraph(ArrayList<LineGraphSeries<DataPoint>> seriesList, int graphID, double minX, double maxX){
        //Create graph object
        GraphView graph = (GraphView) root.findViewById(graphID);

        for(LineGraphSeries lgs:seriesList){
            graph.addSeries(lgs);
        }

        formatGridLabelRenderer(graph.getGridLabelRenderer());
        graph.getViewport().setMinX(minX);
        graph.getViewport().setMaxX(maxX);
        graph.getViewport().setXAxisBoundsManual(true);

        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);/**/

    }

    private void generateBalanceGraph(){
        ArrayList<LineGraphSeries<DataPoint>> seriesList = new ArrayList();

        //Calculate initial date to start getting balance from
        Calendar startDate = (Calendar) DATE_CONSIDERED_NOW.clone();
        startDate.add(Calendar.DAY_OF_YEAR, -KNOWN_BALANCE_DAYS_TO_PLOT);

        DataPoint[] knownBalancePoints = getKnownBalancePoints(startDate);

        LineGraphSeries<DataPoint> knownBalanceSeries = new LineGraphSeries<>(knownBalancePoints);
        knownBalanceSeries.setTitle(getString(R.string.actual_balance_title));

        LineGraphSeries<DataPoint> estimateBalanceSeries = new LineGraphSeries<>(getEstimateBalancePoints(knownBalancePoints));
        estimateBalanceSeries.setTitle(getString(R.string.estimated_balance_title));

        seriesList.add(knownBalanceSeries);
        seriesList.add(estimateBalanceSeries);
        seriesList.add(getOverdraftSeries(startDate));

        Calendar endDate = (Calendar) DATE_CONSIDERED_NOW.clone();
        endDate.add(Calendar.DAY_OF_YEAR, KNOWN_BALANCE_DAYS_TO_PLOT);
        generateGraph(seriesList, R.id.balance_graph, startDate.getTimeInMillis(), endDate.getTimeInMillis());
    }

    private DataPoint[] getKnownBalancePoints(Calendar startDate){

        int customerID = ((MainActivity) getActivity()).getCustomer().getId();

        //Get balance/date map
        TreeMap<Calendar, Double> balanceByDate = ((MainActivity) getActivity()).getAdapter().getBalanceDateMap(customerID, startDate, KNOWN_BALANCE_DAYS_TO_PLOT);

        DataPoint[] dataPoints = new DataPoint[KNOWN_BALANCE_DAYS_TO_PLOT];

        int i = 0;
        for(Calendar c: balanceByDate.keySet()){
            dataPoints[i] = new DataPoint(c.getTime(), balanceByDate.get(c));
            i++;
        }

        return dataPoints;
    }

    private DataPoint[] getEstimateBalancePoints(DataPoint[] knownPoints){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis((long) knownPoints[KNOWN_BALANCE_DAYS_TO_PLOT - 1].getX());
        date.add(Calendar.DAY_OF_YEAR, 1);

        DataPoint[] estimateBalancePoints = new DataPoint[KNOWN_BALANCE_DAYS_TO_PLOT];
        for(int i = 0 ;i < KNOWN_BALANCE_DAYS_TO_PLOT; i++){
            estimateBalancePoints[i] = new DataPoint(date.getTime(), 2000);
            date = (Calendar) date.clone();
            date.add(Calendar.DAY_OF_YEAR,1);
        }

        return knownPoints;
    }

    /*private void generateSpendingGraph(){

        ArrayList<LineGraphSeries<DataPoint>> seriesList = new ArrayList();

        DataPoint[] points = getBalancePoints();
        LineGraphSeries<DataPoint> dataSeries = new LineGraphSeries<>(points);
        dataSeries.setTitle(getString(R.string.spending_graph_title));

        seriesList.add(dataSeries);

        generateGraph(seriesList, R.id.spending_graph, points[0].getX(), points[points.length - 1].getX());
    }*/

   /* private DataPoint[] getSpendingPoints(){

        int customerID = ((MainActivity) getActivity()).getCustomer().getId();
        Map<Date, Double> spending = ((MainActivity) getActivity()).getAdapter().getSpendingDateMap(customerID);
        for(Date d: spending.keySet()){
            android.util.Log.i("trans", d + " " + spending.get(d));
        }
       return null;
    }*/

    private LineGraphSeries<DataPoint> getOverdraftSeries(Calendar startDate){
        DataPoint[] overdraftPoints = new DataPoint[KNOWN_BALANCE_DAYS_TO_PLOT*2];
        Calendar date = (Calendar) startDate.clone();

        for(int i = 0; i < KNOWN_BALANCE_DAYS_TO_PLOT*2; i++){
            overdraftPoints[i] = new DataPoint(date.getTime(), 0);
            date.add(Calendar.DAY_OF_YEAR, 1);
        }

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{10.0f, 5.0f}, 0));
        paint.setColor(Color.RED);

        LineGraphSeries<DataPoint> overdraftSeries = new LineGraphSeries<>(overdraftPoints);
        overdraftSeries.setTitle(getString(R.string.overdraft_desc));
        overdraftSeries.setCustomPaint(paint);
        overdraftSeries.setColor(Color.RED);

        return overdraftSeries;
    }

    private String getAverageSpending(){
        return "£" + 20;
    }

    private void generateSummary(){
        TextView average = (TextView) root.findViewById(R.id.summary);
        average.setText(getString(R.string.summary_1) + getAverageSpending() + getString(R.string.summary_2));
        average.append(getString(R.string.overdraft_safe));
    }

    private void formatGridLabelRenderer(GridLabelRenderer gridLabelRenderer){
       gridLabelRenderer.setLabelFormatter(getLabelFormatter());
        gridLabelRenderer.setGridColor(getResources().getColor(R.color.lloyds_green));
        gridLabelRenderer.setHorizontalLabelsColor(Color.BLACK);
        gridLabelRenderer.setVerticalLabelsColor(Color.BLACK);
        gridLabelRenderer.setNumHorizontalLabels(NUMBER_OF_DATE_LABELS);
        gridLabelRenderer.setHighlightZeroLines(false);
    }

    public LabelFormatter getLabelFormatter() {
        return new DefaultLabelFormatter() {
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    return df.format(new java.util.Date((long) value)) + "\n";
                } else {
                    return "£" + super.formatLabel(value, isValueX) + " ";
                }
            }
        };
    }

   }
