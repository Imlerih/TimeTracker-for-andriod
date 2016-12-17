package palladin.lab32;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ChartActivity extends AppCompatActivity
{
    PieChart pieChart;
    PieDataSet dataSet;
    PieData data;

    public static final int[] COLORS =
            {
            Color.rgb(64, 89, 128),
            Color.rgb(149, 165, 124),
            Color.rgb(217, 184, 162),
            Color.rgb(179, 48, 80),
            Color.rgb(191, 134, 134),
            Color.rgb(255, 102, 0),
            Color.rgb(193, 37, 82),
            Color.rgb(106, 150, 31),
            Color.rgb(245, 199, 0),
            Color.rgb(179, 100, 53)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        setTitle("Chart statistics");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        pieChart = (PieChart)findViewById(R.id.pie_chart);


        List<Long> periods = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();


        Cursor categories = A.db.getCursor(Table.CATEGORIES);
        Cursor records;
        int id;

        String name;
        long period, time = 0;

        while(categories.moveToNext())
        {
            id = categories.getInt(0);
            name = categories.getString(1);
            records = A.db.getCursor(Table.RECORDS, Table.Records.COL_CATEGORY_ID, id);

            period = 0;
            while (records.moveToNext())
                period += records.getLong(4);

            if (period != 0)
            {
                periods.add(period);
                labels.add(name);
                time += period;
            }
        }

        for(int i = 0; i < periods.size(); i++)
            entries.add(new Entry(100f * (float)periods.get(i) / (float)time, i));



        dataSet = new PieDataSet(entries, "%");
        dataSet.setColors(COLORS);

        data = new PieData(labels, dataSet);
        pieChart.setData(data);

        pieChart.setDescription("Percentage time of each category");
        pieChart.animateY(1500);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

}
