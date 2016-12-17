package palladin.lab32;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Palladin on 13.12.2016.
 */
public class AdapterStatisticsRecyclerList extends RecyclerView.Adapter<AdapterStatisticsRecyclerList.ItemViewHolder>
{

    private List<Model.Statistics> categoriesList;
    private LayoutInflater inflater;
    private Handlers.CheckedChangedHandler checkedChangedHandler;

    public AdapterStatisticsRecyclerList(Context context, @Nullable List<Model.Statistics> categoriesList,
                                         Handlers.CheckedChangedHandler checkedChangedHandler)
    {
        if(categoriesList == null)
            this.categoriesList = new ArrayList<>();
        else
            this.categoriesList = categoriesList;
        inflater = LayoutInflater.from(context);
        this.checkedChangedHandler = checkedChangedHandler;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ItemViewHolder(inflater.inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position)
    {
        final Model.Statistics model = categoriesList.get(position);
        holder.categoryName.setText(model.Category);
        holder.mView.setLongClickable(true);
        holder.mView.setTag(model);
        holder.forStatistics(model.SummaryTime, model.Count);
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                checkedChangedHandler.onCheckedChange(isChecked, model);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return categoriesList.size();
    }

    public void changeList(List<Model.Statistics> categoriesList)
    {
        this.categoriesList.clear();
        this.categoriesList = categoriesList;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        TextView categoryName, upperTimeL, downTimeL;
        LinearLayout timeLayout;
        ImageView recordIm;
        CheckBox checkBox;
        public ItemViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            categoryName = (TextView)mView.findViewById(R.id.item_c_category_name);
            timeLayout = (LinearLayout)mView.findViewById(R.id.item_c_time_layout);
            recordIm = (ImageView)mView.findViewById(R.id.item_c_record_im);
            checkBox = (CheckBox) mView.findViewById(R.id.item_c_chb);
            downTimeL = (TextView)mView.findViewById(R.id.item_c_down_time_l);
            upperTimeL = (TextView)mView.findViewById(R.id.item_c_upper_time_l);

        }


        public void forStatistics(int time, int num)
        {
            timeLayout.setVisibility(View.VISIBLE);
            recordIm.setVisibility(View.INVISIBLE);

            int period = time;
            int minutes, hours, days;
            days = period / (60000 * 60 * 24);
            period %= (60000 * 60 * 24);

            hours = period / (60000 * 60);
            period %= (60000 * 60);

            minutes = period / (60000);

            upperTimeL.setText(days + "d " + hours + "h " + minutes + "m");
            downTimeL.setText(num + " records");

            checkBox.setVisibility(View.VISIBLE);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    if(checkedChangedHandler != null)
                        checkedChangedHandler.onCheckedChange(isChecked, 42);
                }
            });
        }
    }
}
