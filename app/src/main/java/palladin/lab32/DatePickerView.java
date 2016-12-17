package palladin.lab32;

import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.Calendar;


/**
 * Created by Palladin on 01.10.2016.
 */
public class DatePickerView extends TextView implements DatePickerDialog.OnDateSetListener, View.OnClickListener
{
    Calendar calendar;
    private Handlers.Handler event;

    public void setHandler(Handlers.Handler event)
    {
        this.event = event;
    }

    public DatePickerView(Context context)
    {
        this(context, null);
        calendar = Calendar.getInstance();
    }

    public DatePickerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        calendar = Calendar.getInstance();
    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        calendar = Calendar.getInstance();
        display();

    }

    private void display()
    {
        int _year = calendar.get(Calendar.YEAR);
        int _month = calendar.get(Calendar.MONTH);
        int _day = calendar.get(Calendar.DAY_OF_MONTH);
        setText(_day + "."+_month+"."+_year);
    }

    public void setDate( long time)
    {
        calendar.setTimeInMillis(time);
        display();
    }

    public long getDate()
    {
        return calendar.getTimeInMillis();
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth)
    {
        calendar.set(year, month, dayOfMonth);
        display();
        if(event != null) event.action();
    }

    @Override
    public void onClick(View v)
    {

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =  new DatePickerDialog(getContext(), this, year, month, day);
        datePickerDialog.show();
    }
}
