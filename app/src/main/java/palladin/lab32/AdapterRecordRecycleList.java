package palladin.lab32;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Palladin on 03.12.2016.
 */
public class AdapterRecordRecycleList extends RecyclerView.Adapter<AdapterRecordRecycleList.ItemViewHolder>
{
    private List<Model.Record> recordsList;
    private LayoutInflater inflater;
    private Handlers.LongClickHandler longClickHandler;
    private Handlers.ClickHandler clickHandler;

    public AdapterRecordRecycleList(Context context, @Nullable List<Model.Record> recordsList,
                                    Handlers.ClickHandler clickHandler, Handlers.LongClickHandler longClickHandler)
    {
        if(recordsList == null)
            this.recordsList = new ArrayList<>();
        else
            this.recordsList = recordsList;
        inflater = LayoutInflater.from(context);
        this.longClickHandler = longClickHandler;
        this.clickHandler = clickHandler;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ItemViewHolder(inflater.inflate(R.layout.item_record, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position)
    {
        final Model.Record model = recordsList.get(position);
        holder.categoryName.setText(model.Category);
        holder.setTime(model.StartTime, model.EndTime);
        holder.mView.setLongClickable(true);
        holder.description.setText(model.Description);
        holder.setClickHandlers(model);
    }

    @Override
    public int getItemCount() {return recordsList.size();}

    public void addItem(Model.Record item)
    {
        recordsList.add(item);
        notifyItemInserted(recordsList.size() -1);
    }

    public void setDescription(String description, Model.Record item)
    {
        int position = recordsList.indexOf(item);
        recordsList.get(position).Description = description;
        notifyItemChanged(position);
    }

    public void deleteItem(Model.Record item)
    {
        int position = recordsList.indexOf(item);
        recordsList.remove(position);
        notifyItemRemoved(position);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        TextView categoryName, description, periodL;
        ImageButton addDesc, addPhoto, showPhotos;
        TimePickerView startTimeV, endTimeV;
        DatePickerView startDateV, endDateV;
        public ItemViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            categoryName = (TextView)mView.findViewById(R.id.item_r_category_name);
            description = (TextView)mView.findViewById(R.id.item_r_description);
            addDesc = (ImageButton)mView.findViewById(R.id.item_r_add_description);
            addPhoto = (ImageButton)mView.findViewById(R.id.item_r_add_photo);
            showPhotos = (ImageButton)mView.findViewById(R.id.item_r_show_photos);
            startTimeV = (TimePickerView)mView.findViewById(R.id.item_r_start_time);
            startDateV = (DatePickerView)mView.findViewById(R.id.item_r_start_date);
            endTimeV = (TimePickerView)mView.findViewById(R.id.item_r_end_time);
            endDateV = (DatePickerView)mView.findViewById(R.id.item_r_end_date);
            periodL = (TextView)mView.findViewById(R.id.item_r_period);
        }

        public void setClickHandlers(final Object model)
        {

            View.OnClickListener onClickListener = new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    clickHandler.onClick(v, model);
                }
            };

            addDesc.setOnClickListener(onClickListener);
            addPhoto.setOnClickListener(onClickListener);
            showPhotos.setOnClickListener(onClickListener);

            mView.setLongClickable(true);
            mView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    return longClickHandler.onLongClick(v, model);
                }
            });
        }

        void setTime(long startTime, long endTime)
        {
            startTimeV.setTime(startTime);
            startDateV.setDate(startTime);

            long period;
            if(endTime == 0)
            {
                endTimeV.setVisibility(View.INVISIBLE);
                endDateV.setVisibility(View.INVISIBLE);
                periodL.setTextColor(Color.RED);
                Calendar calendar = Calendar.getInstance();
                period = calendar.getTimeInMillis() - startTime;
            }
            else
            {
                endTimeV.setVisibility(View.VISIBLE);
                endDateV.setVisibility(View.VISIBLE);
                endTimeV.setTime(startTime);
                endDateV.setDate(startTime);
                periodL.setTextColor(Color.BLACK);
                period = endTime - startTime;
            }
            int minutes, hours, days;
            days = (int)(period / (60000 * 60 * 24));
            period %= (60000 * 60 * 24);

            hours = (int)(period / (60000 * 60));
            period %= (60000 * 60);

            minutes = (int)(period / (60000));

            periodL.setText(days + "d " + hours + "h " + minutes + "m");
        }
    }
}
