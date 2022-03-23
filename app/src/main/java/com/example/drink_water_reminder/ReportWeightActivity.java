package com.example.drink_water_reminder;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class ReportWeightActivity extends AppCompatActivity {

    LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_weight);

        lineChart = findViewById(R.id.lineChart);
        LineDataSet lineDataSet1 = new LineDataSet(lineChartDataSet1(),"weight");
        LineDataSet lineDataSet2 = new LineDataSet(lineChartDataSet2(),"Annual average");
        ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet1);
        iLineDataSets.add(lineDataSet2);

        LineData lineData = new LineData(iLineDataSets);
        lineChart.setData(lineData);
        lineChart.invalidate();


        //if you want set background color use below method
        //lineChart.setBackgroundColor(Color.RED);

        // set text if data are are not available
        lineChart.setNoDataText("Data not Available");

        //you can modify your line chart graph according to your requirement there are lots of method available in this library

        //now customize line chart

        lineDataSet1.setColor(Color.GREEN);
        lineDataSet1.setCircleColor(Color.GREEN);
        lineDataSet1.setDrawCircles(true);
        lineDataSet1.setDrawCircleHole(true);
        lineDataSet1.setLineWidth(2);
        lineDataSet1.setCircleRadius(5);
        lineDataSet1.setCircleHoleRadius(10);
        lineDataSet1.setValueTextSize(10);
        lineDataSet1.setValueTextColor(Color.BLACK);

        lineDataSet2.setColor(Color.RED);
        lineDataSet2.setCircleColor(Color.RED);
        lineDataSet2.setDrawCircles(true);
        lineDataSet2.setDrawCircleHole(true);
        lineDataSet2.setLineWidth(2);
        lineDataSet2.setCircleRadius(5);
        lineDataSet2.setCircleHoleRadius(10);
        lineDataSet2.setValueTextSize(10);
        lineDataSet2.setValueTextColor(Color.BLACK);
    }





    private ArrayList<Entry> lineChartDataSet1(){
        ArrayList<Entry> dataSet1 = new ArrayList<Entry>();

        dataSet1.add(new Entry(0,40));
        dataSet1.add(new Entry(1,10));
        dataSet1.add(new Entry(2,15));
        dataSet1.add(new Entry(3,12));
        dataSet1.add(new Entry(4,20));
        dataSet1.add(new Entry(5,50));
        dataSet1.add(new Entry(6,23));
        dataSet1.add(new Entry(7,34));
        dataSet1.add(new Entry(8,12));
        return  dataSet1;

    }

    private ArrayList<Entry> lineChartDataSet2(){
        ArrayList<Entry> dataSet2 = new ArrayList<Entry>();

        dataSet2.add(new Entry(0,40));
        dataSet2.add(new Entry(1,40));
        dataSet2.add(new Entry(2,40));
        dataSet2.add(new Entry(3,40));
        dataSet2.add(new Entry(4,40));
        dataSet2.add(new Entry(5,40));
        dataSet2.add(new Entry(6,40));
        dataSet2.add(new Entry(7,40));
        dataSet2.add(new Entry(8,40));
        return  dataSet2;


    }
}