package com.team.two.lloyds_app.screens.fragments;

import com.team.two.lloyds_app.R;
import com.team.two.lloyds_app.screens.activities.MainActivity;
import android.util.Log;

import android.content.pm.ActivityInfo;
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
    private int customerID;

    private final DateFormat df = new SimpleDateFormat("dd-MM", Locale.ENGLISH);
    private final int KNOWN_BALANCE_DAYS_TO_PLOT = 14;
    private final int NUMBER_OF_DATE_LABELS = 5;
    private Calendar DATE_CONSIDERED_NOW = Calendar.getInstance();
    private double AVERAGE_SPENDING = 0;
    private double LATEST_KNOWN_BALANCE = 0;
    private final double OVERDRAFT_SAFE_DAYS = 30;
    private final double OVERDRAFT_DANGER_DAYS = 10;

    public MoneyPlannerFragment() {
        // Required empty public constructor
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getActivity().setTitle("Money Planner");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_money_planner, container, false);

        //Set the date that is considered the present date
        DATE_CONSIDERED_NOW.set(2015,Calendar.FEBRUARY, 15);
        customerID  = ((MainActivity) getActivity()).getCustomer().getId();

        generateSpendingGraph();
        generateBalanceGraph();
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

        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);
        graph.getLegendRenderer().setPadding(0);
        graph.getLegendRenderer().setTextColor(getResources().getColor(R.color.black));
        graph.getLegendRenderer().setBackgroundColor(getResources().getColor(R.color.legend_background));
        graph.getLegendRenderer().setVisible(true);

    }

    private void generateBalanceGraph(){
        ArrayList<LineGraphSeries<DataPoint>> seriesList = new ArrayList<>();

        //Calculate initial date to start getting balance from
        Calendar startDate = (Calendar) DATE_CONSIDERED_NOW.clone();
        startDate.add(Calendar.DAY_OF_YEAR, -KNOWN_BALANCE_DAYS_TO_PLOT);

        DataPoint[] knownBalancePoints = getKnownBalancePoints(startDate);

        LineGraphSeries<DataPoint> knownBalanceSeries = new LineGraphSeries<>(knownBalancePoints);
        knownBalanceSeries.setTitle(getString(R.string.actual_balance_title));
        knownBalanceSeries.setColor(Color.BLUE);

        LineGraphSeries<DataPoint> estimateBalanceSeries = new LineGraphSeries<>(getEstimateBalancePoints(knownBalancePoints));
        estimateBalanceSeries.setTitle(getString(R.string.estimated_balance_title));
        estimateBalanceSeries.setColor(getResources().getColor(R.color.money_planner_estimated));

        seriesList.add(knownBalanceSeries);
        seriesList.add(estimateBalanceSeries);
        seriesList.add(getOverdraftSeries(startDate));

        Calendar endDate = (Calendar) DATE_CONSIDERED_NOW.clone();
        endDate.add(Calendar.DAY_OF_YEAR, KNOWN_BALANCE_DAYS_TO_PLOT);
        generateGraph(seriesList, R.id.balance_graph, startDate.getTimeInMillis(), endDate.getTimeInMillis());
    }

    private DataPoint[] getKnownBalancePoints(Calendar startDate){

        //Get balance/date map
        TreeMap<Calendar, Double> balanceByDate = ((MainActivity) getActivity()).getAdapter().getBalanceDateMap(customerID, startDate, KNOWN_BALANCE_DAYS_TO_PLOT);

        DataPoint[] knownBalancePoints = new DataPoint[KNOWN_BALANCE_DAYS_TO_PLOT];

        int i = 0;
        for(Calendar c: balanceByDate.keySet()){
            knownBalancePoints[i] = new DataPoint(c.getTime(), balanceByDate.get(c));
            i++;
        }

        LATEST_KNOWN_BALANCE = knownBalancePoints[KNOWN_BALANCE_DAYS_TO_PLOT-1].getY();
        Calendar c = Calendar.getInstance();
        for(DataPoint dp : knownBalancePoints){
            c.setTimeInMillis((long)dp.getX());
            Log.d("test", df.format(c.getTime()) + " " + dp.getY());
        }
        return knownBalancePoints;
    }

    private DataPoint[] getEstimateBalancePoints(DataPoint[] knownPoints){
        Calendar date = Calendar.getInstance();
        date.setTimeInMillis((long) knownPoints[KNOWN_BALANCE_DAYS_TO_PLOT - 1].getX());
        double balance = knownPoints[KNOWN_BALANCE_DAYS_TO_PLOT - 1].getY();

        DataPoint[] estimateBalancePoints = new DataPoint[KNOWN_BALANCE_DAYS_TO_PLOT + 1];

        for(int i = 0 ; i < KNOWN_BALANCE_DAYS_TO_PLOT + 1; i++){
            balance = balance - AVERAGE_SPENDING;
            estimateBalancePoints[i] = new DataPoint(date.getTime(), balance);
            date = (Calendar) date.clone();
            date.add(Calendar.DAY_OF_YEAR,1);
        }

        return estimateBalancePoints;
    }

    private void generateSpendingGraph(){

        ArrayList<LineGraphSeries<DataPoint>> seriesList = new ArrayList<>();

        DataPoint[] points = getSpendingPoints();
        LineGraphSeries<DataPoint> dataSeries = new LineGraphSeries<>(points);
        dataSeries.setTitle(getString(R.string.spending_graph_title));

        seriesList.add(dataSeries);

        generateGraph(seriesList, R.id.spending_graph, points[0].getX(), points[points.length - 1].getX());
    }

    private DataPoint[] getSpendingPoints(){

        //Get balance/date map
        TreeMap<Calendar, Double> spendingByDate = ((MainActivity) getActivity()).getAdapter().getSpendingDateMap(customerID, DATE_CONSIDERED_NOW, KNOWN_BALANCE_DAYS_TO_PLOT);

        DataPoint[] dataPoints = new DataPoint[KNOWN_BALANCE_DAYS_TO_PLOT];
        int i = 0;
        for(Calendar c: spendingByDate.keySet()){
            dataPoints[i] = new DataPoint(c.getTime(),spendingByDate.get(c));
            AVERAGE_SPENDING = AVERAGE_SPENDING + spendingByDate.get(c);
            i++;
        }
        AVERAGE_SPENDING = AVERAGE_SPENDING / KNOWN_BALANCE_DAYS_TO_PLOT;

        return dataPoints;
    }

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

    private double getSpendingAverage(TreeMap<Calendar, Double> map){
        double average = 0;
        for(Calendar c: map.keySet()){
            average = average + map.get(c);
        }
        return average/map.size();
    }

    private void generateSummary(){
        TextView summary = (TextView) root.findViewById(R.id.summary);
        String averageSpending = getString(R.string.pound_sign) + String.format("%.4g", AVERAGE_SPENDING);
        double daysUntilZero = LATEST_KNOWN_BALANCE / AVERAGE_SPENDING;
        Calendar dateTwoPeriodsAgo = (Calendar) DATE_CONSIDERED_NOW.clone();
        dateTwoPeriodsAgo.add(Calendar.DATE, -KNOWN_BALANCE_DAYS_TO_PLOT);
        double previousAvg = getSpendingAverage(((MainActivity) getActivity()).getAdapter().getSpendingDateMap(customerID, dateTwoPeriodsAgo, KNOWN_BALANCE_DAYS_TO_PLOT));
        String averageChange;
        if(previousAvg > AVERAGE_SPENDING){
            averageChange = getString(R.string.average_decrease) + getString(R.string.summary_3) + String.format("%.2g",100*(previousAvg-AVERAGE_SPENDING)/previousAvg) + getString(R.string.summary_4) + KNOWN_BALANCE_DAYS_TO_PLOT + getString(R.string.days);
        } else if(previousAvg < AVERAGE_SPENDING){
            averageChange = getString(R.string.average_increase) + getString(R.string.summary_3) + String.format("%.2g",100*(AVERAGE_SPENDING-previousAvg)/previousAvg) + getString(R.string.summary_4) + KNOWN_BALANCE_DAYS_TO_PLOT + getString(R.string.days);
        } else {
            averageChange = getResources().getString(R.string.average_no_change) + getString(R.string.summary_4) + KNOWN_BALANCE_DAYS_TO_PLOT + getString(R.string.days);
        }

        if(LATEST_KNOWN_BALANCE < 0){
            //In overdraft
            summary.setText(getString(R.string.in_overdraft) + averageSpending + getString(R.string.on_average));
            summary.setTextColor(Color.RED);
        } else {
            summary.setText(getString(R.string.summary_1) + averageSpending + getString(R.string.summary_2) + averageChange + getString(R.string.summary_5) + String.format("%.2g",daysUntilZero) + getString(R.string.days));
            if (daysUntilZero > OVERDRAFT_SAFE_DAYS) {
                summary.append(getString(R.string.overdraft_safe));
            } else if (daysUntilZero > OVERDRAFT_DANGER_DAYS) {
                summary.append(getString(R.string.overdraft_caution));
            } else {
                summary.append(getString(R.string.overdraft_danger));
            }
        }
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
                    return "£" + super.formatLabel(value, false) + " ";
                }
            }
        };
    }

}
