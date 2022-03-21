package com.example.drink_water_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;


import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    private PieChart pieChart;

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        lineChart = findViewById(R.id.lineChart);
        LineDataSet lineDataSet = new LineDataSet(lineChartDataSet(),"data set");
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);

        LineData lineData = new LineData(iLineDataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();


        //if you want set background color use below method
        //lineChart.setBackgroundColor(Color.RED);

        // set text if data are are not available
        lineChart.setNoDataText("Data not Available");

        //you can modify your line chart graph according to your requirement there are lots of method available in this library

        //now customize line chart

        lineDataSet.setColor(Color.GREEN);
        lineDataSet.setCircleColor(Color.GREEN);
        lineDataSet.setDrawCircles(true);
        lineDataSet.setDrawCircleHole(true);
        lineDataSet.setLineWidth(2);
        lineDataSet.setCircleRadius(5);
        lineDataSet.setCircleHoleRadius(10);
        lineDataSet.setValueTextSize(10);
        lineDataSet.setValueTextColor(Color.BLACK);


        pieChart = findViewById(R.id.pieChart);
        setupPieChart();
        loadPieChartData();

    }



    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);

        Legend l = pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setEnabled(true);
    }

    private void loadPieChartData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(0.1f, "Water"));
        entries.add(new PieEntry(0.9f, "None"));

        ArrayList<Integer> colors = new ArrayList<>();
        for (int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Target");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueTextSize(0.1f);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

        pieChart.animateY(1400, Easing.EaseInOutQuad);
    }




    private ArrayList<Entry> lineChartDataSet(){
        ArrayList<Entry> dataSet = new ArrayList<Entry>();

        dataSet.add(new Entry(0,40));
        dataSet.add(new Entry(1,10));
        dataSet.add(new Entry(2,15));
        dataSet.add(new Entry(3,12));
        dataSet.add(new Entry(4,20));
        dataSet.add(new Entry(5,50));
        dataSet.add(new Entry(6,23));
        dataSet.add(new Entry(7,34));
        dataSet.add(new Entry(8,12));
        return  dataSet;




    }

}



