package palladin.lab32;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticsFragment extends Fragment
{

    RecyclerView recyclerList;
    AdapterStatisticsRecyclerList listAdapter;
    CheckBox forThisMonthChb;
    TimePickerView fromTime, toTime;
    DatePickerView fromDate, toDate;
    boolean isMostRecent;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Most recent");
        return inflater.inflate(R.layout.fragment_statistics, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        forThisMonthChb = (CheckBox)view.findViewById(R.id.for_month_only_ch);
        forThisMonthChb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                listAdapter.changeList(sortedList(isMostRecent));
            }
        });

        fromTime = (TimePickerView) view.findViewById(R.id.from_time);
        toTime = (TimePickerView) view.findViewById(R.id.to_time);
        fromDate = (DatePickerView) view.findViewById(R.id.from_date);
        fromDate.setDate(fromDate.getDate() - 86400000);
        toDate = (DatePickerView) view.findViewById(R.id.to_date);
        toDate.setDate(fromDate.getDate() + 604800000);

        recyclerList = (RecyclerView)view.findViewById(R.id.recyclerStatistics);
        listAdapter = new AdapterStatisticsRecyclerList(getActivity(), sortedList(true), changedHandler);
        recyclerList.setAdapter(listAdapter);
        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));

        fromTime.setHandler(timeChangeEvent);
        toTime.setHandler(timeChangeEvent);
        fromDate.setHandler(timeChangeEvent);
        toDate.setHandler(timeChangeEvent);

        setHasOptionsMenu(true);
    }

    Handlers.Handler timeChangeEvent = new Handlers.Handler()
    {
        @Override
        public void action()
        {
            listAdapter.changeList(sortedList(isMostRecent));
        }
    };

    Handlers.CheckedChangedHandler changedHandler = new Handlers.CheckedChangedHandler()
    {
        @Override
        public void onCheckedChange(boolean isChecked, Object data)
        {

        }
    };

    @Override
   public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
   {
       super.onCreateOptionsMenu(menu, inflater);
       inflater.inflate(R.menu.statistics, menu);
   }


    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.statistics_longest_time:
                listAdapter.changeList(sortedList(false));
                getActivity().setTitle("Longest time");
                break;
            case R.id.statistics_most_recent:
                listAdapter.changeList(sortedList(true));
                getActivity().setTitle("Most recent");
                break;
            case R.id.statistics_chart:
                startActivity(new Intent(getActivity(), ChartActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    List<Model.Statistics> sortedList(boolean isMostRecent)
    {
        this.isMostRecent = isMostRecent;
        List<Model.Statistics> items = new ArrayList<>();

        Cursor categories = A.db.getCursor(Table.CATEGORIES);
        Cursor records;
        int id, counter, time;

        String name;
        long start, end;
        while(categories.moveToNext())
        {
            id = categories.getInt(0);
            name = categories.getString(1);
            records = A.db.getCursor(Table.RECORDS, Table.Records.COL_CATEGORY_ID, id);
            counter = 0;
            time = 0;

            while (records.moveToNext())
            {
                start = records.getLong(2);
                end = records.getLong(3);

                if (isInTimeRange(start, end))
                {
                    counter++;
                    time += (int)records.getLong(4);
                }
            }

            if (counter != 0)
                items.add(new Model.Statistics(name, id, time, counter));

        }

        if (isMostRecent)
        {
            Collections.sort(items, new Comparator<Model.Statistics>()
            {
                @Override
                public int compare(Model.Statistics o1, Model.Statistics o2)
                {
                    return o2.Count - o1.Count;
                }
            });
        }
        else
        {
            Collections.sort(items, new Comparator<Model.Statistics>()
            {
                @Override
                public int compare(Model.Statistics o1, Model.Statistics o2)
                {
                    return o2.SummaryTime - o1.SummaryTime;
                }
            });
        }

        return items;
    }

    //bad, but easier!
    boolean isInTimeRange(long startTime, long endTime)
    {
        int nowMonth, startMonth, endMonth;
        Calendar calendar = Calendar.getInstance();
        nowMonth = calendar.get(Calendar.MONTH);
        calendar.setTimeInMillis(startTime);
        startMonth = calendar.get(Calendar.MONTH);
        calendar.setTimeInMillis(endTime);
        endMonth = calendar.get(Calendar.MONTH);

        long from, to;
        from = fromDate.getDate();
        from = (from - from % 86400000) + fromTime.getTime() % 86400000;

        to = toDate.getDate();
        to = (to - to % 86400000) + toTime.getTime() % 86400000;


        if(forThisMonthChb.isChecked())
        {
            if (nowMonth == startMonth && nowMonth == endMonth)
                return true;
        }
        else
        if(startTime >= from && startTime <= to)
            if (endTime >= from && endTime <= to)
                return true;
        return false;
    }
}
