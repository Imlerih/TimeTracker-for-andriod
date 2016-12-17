package palladin.lab32;

import android.app.TimePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Palladin on 18.11.2016.
 */
public class TimePickerView extends TextView implements TimePickerDialog.OnTimeSetListener, View.OnClickListener
{
    Calendar calendar;
    private Handlers.Handler event;

    public void setHandler(Handlers.Handler event)
    {
        this.event = event;
    }

    public TimePickerView(Context context)
    {
        super(context, null);
        calendar = Calendar.getInstance();

    }


    public TimePickerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
        calendar = Calendar.getInstance();
    }


    public TimePickerView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        setOnClickListener(this);
        calendar = Calendar.getInstance();
        display();

    }

    private void display()
    {
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);
        String _devider = ":";
        if(minutes < 10) _devider += "0";
        setText(hours + _devider + minutes);
    }

    public void setTime(long time)
    {
        calendar.setTimeInMillis(time);
        display();
    }

    public long getTime()
    {
        return calendar.getTimeInMillis();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
    {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        display();
        if(event != null) event.action();
    }

    @Override
    public void onClick(View v)
    {
        int hours = calendar.get(Calendar.HOUR_OF_DAY);
        int minutes = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this, hours, minutes, true);
        timePickerDialog.show();
    }
}
