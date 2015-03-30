package com.team.two.lloyds_app.screens.fragments;

import com.team.two.lloyds_app.R;

import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
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
    private final int BALANCE_DAYS_TO_PLOT = 31;
    private final int SPENDING_DAYS_TO_PLOT = 7;
    private final int NUMBER_OF_DATE_LABELS = 5;

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

        generateBalanceGraph();
        generateSpendingGraph();
        generateSummary();

        return root;
    }

    private void generateGraph(ArrayList<LineGraphSeries<DataPoint>> seriesList, int graphID, double minX, double maxX){
        //Create graph object
        GraphView graph = (GraphView) root.findViewById(graphID);

        for(LineGraphSeries lgs:seriesList){
            graph.addSeries(lgs);
        }

        formatLabelRenderer(graph.getGridLabelRenderer());
        graph.getViewport().setMinX(minX);
        graph.getViewport().setMaxX(maxX);
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getLegendRenderer().setVisible(true);
        graph.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);

    }
    private void generateBalanceGraph(){
        ArrayList<LineGraphSeries<DataPoint>> seriesList = new ArrayList();

        DataPoint[] points = getBalancePoints();
        LineGraphSeries<DataPoint> dataSeries = new LineGraphSeries<>(points);
        dataSeries.setTitle(getString(R.string.balance_graph_title));

        seriesList.add(dataSeries);
        seriesList.add(getOverdraftSeries(points));

        generateGraph(seriesList, R.id.balance_graph, points[0].getX(), points[BALANCE_DAYS_TO_PLOT - 1].getX());
    }

    private void generateSpendingGraph(){

        ArrayList<LineGraphSeries<DataPoint>> seriesList = new ArrayList();

        DataPoint[] points = getSpendingPoints();
        LineGraphSeries<DataPoint> dataSeries = new LineGraphSeries<>(points);
        dataSeries.setTitle(getString(R.string.spending_graph_title));

        seriesList.add(dataSeries);

        generateGraph(seriesList, R.id.spending_graph, points[0].getX(), points[SPENDING_DAYS_TO_PLOT - 1].getX());
    }

    private DataPoint[] getBalancePoints(){
        Calendar calendar = Calendar.getInstance();
        ArrayList<DataPoint> sampleDates = new ArrayList<>();
        for(int i = -2; i < BALANCE_DAYS_TO_PLOT; i++){
            sampleDates.add(new DataPoint(calendar.getTime(), i*100));
            calendar.add(Calendar.DATE, 1);
        }
        DataPoint[] arr = new DataPoint[sampleDates.size()];
        return sampleDates.toArray(arr);
    }

    private DataPoint[] getSpendingPoints(){
        Calendar calendar = Calendar.getInstance();
        ArrayList<DataPoint> sampleDates = new ArrayList<>();
        for(int i = 0; i < SPENDING_DAYS_TO_PLOT; i++){
            sampleDates.add(new DataPoint(calendar.getTime(), i));
            calendar.add(Calendar.DATE, 1);
        }
        DataPoint[] arr = new DataPoint[sampleDates.size()];
        return sampleDates.toArray(arr);
    }

    private LineGraphSeries<DataPoint> getOverdraftSeries(DataPoint[] points){
        DataPoint[] overdraftPoints = new DataPoint[points.length];
        for(int i = 0; i < points.length; i++){
            overdraftPoints[i] = new DataPoint(points[i].getX(), 0);
        }
        LineGraphSeries<DataPoint> overdraftSeries = new LineGraphSeries<>(overdraftPoints);
        overdraftSeries.setTitle(getString(R.string.overdraft_desc));
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setPathEffect(new DashPathEffect(new float[]{10.0f, 5.0f}, 0));
        paint.setColor(Color.RED);

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

    private void formatLabelRenderer(GridLabelRenderer gridLabelRenderer){
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
