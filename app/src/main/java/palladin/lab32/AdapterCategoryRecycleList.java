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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Palladin on 23.10.2016.
 */
public class AdapterCategoryRecycleList extends RecyclerView.Adapter<AdapterCategoryRecycleList.ItemViewHolder>
{
    private List<Model.Category> categoriesList;
    private LayoutInflater inflater;
    private Handlers.ClickHandler clickHandler;
    private Handlers.LongClickHandler longClickHandler;
    public AdapterCategoryRecycleList(Context context, @Nullable List<Model.Category> categoriesList,
                                      Handlers.ClickHandler clickHandler, Handlers.LongClickHandler longClickHandler)
    {
        if(categoriesList == null)
            this.categoriesList = new ArrayList<>();
        else
            this.categoriesList = categoriesList;
        inflater = LayoutInflater.from(context);
        this.clickHandler = clickHandler;
        this.longClickHandler = longClickHandler;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ItemViewHolder(inflater.inflate(R.layout.item_category, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position)
    {
        final Model.Category model = categoriesList.get(position);
        holder.categoryName.setText(model.Category);
        holder.mView.setTag(model);
        holder.setRecording(model.StartRecordTime);

        holder.mView.setLongClickable(true);
        holder.mView.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                return longClickHandler.onLongClick(v, model);
            }
        });
        holder.mView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                clickHandler.onClick(v, model);
            }
        });

    }

    @Override
    public int getItemCount()
    {
        return categoriesList.size();
    }


    public void addItem(Model.Category item)
    {
        categoriesList.add(item);
        notifyItemInserted(categoriesList.size() -1);
    }

    public void deleteItem(Model.Category item)
    {
        int position = categoriesList.indexOf(item);
        categoriesList.remove(position);
        notifyItemRemoved(position);
    }

    public void renameItem(String NewName, Model.Category item)
    {
        int position = categoriesList.indexOf(item);
        categoriesList.get(position).Category = NewName;
        notifyItemChanged(position);
    }

    public void setRecording(long startTime, Model.Category item)
    {
        int position = categoriesList.indexOf(item);
        categoriesList.get(position).StartRecordTime = startTime;
        notifyItemChanged(position);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder
    {
        View mView;
        TextView categoryName, upperTimeL, downTimeL;
        LinearLayout timeLayout;
        ImageView recordIm;
        public ItemViewHolder(View itemView)
        {
            super(itemView);
            mView = itemView;
            categoryName = (TextView)mView.findViewById(R.id.item_c_category_name);
            timeLayout = (LinearLayout)mView.findViewById(R.id.item_c_time_layout);
            recordIm = (ImageView)mView.findViewById(R.id.item_c_record_im);
            downTimeL = (TextView)mView.findViewById(R.id.item_c_down_time_l);
            upperTimeL = (TextView)mView.findViewById(R.id.item_c_upper_time_l);
        }

        public void setRecording(long startTime)
        {
            if(startTime == 0)
            {
                timeLayout.setVisibility(View.INVISIBLE);
                recordIm.setImageResource(R.mipmap.ic_play_arrow_white_24dp);
            }
            else
            {
                SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat sdfDate = new SimpleDateFormat("dd.MM.yyyy");
                upperTimeL.setText(sdfTime.format(startTime));
                downTimeL.setText(sdfDate.format(startTime));

                timeLayout.setVisibility(View.VISIBLE);
                recordIm.setImageResource(R.mipmap.ic_stop_white_24dp);
            }
        }
    }
}

