package palladin.lab32;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.List;

/**
 * Created by Palladin on 16.12.2016.
 */
public class AdapterPhotosRecyclerList extends RecyclerView.Adapter<AdapterPhotosRecyclerList.ItemViewHolder>
{
    private Context context;
    private LayoutInflater inflator;
    private List<String> files;

    public AdapterPhotosRecyclerList(Context context, List<String> files)
    {
        this.context = context;
        this.files = files;
        inflator = LayoutInflater.from(context);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        return new ItemViewHolder(inflator.inflate(R.layout.item_photo, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position)
    {
        final String filePath = files.get(position);
        holder.image.setImageURI(Uri.parse(filePath));
        holder.image.setLongClickable(true);
        holder.image.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                File file = new File(filePath);
                file.delete();
                files.remove(filePath);
                notifyDataSetChanged();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {return files.size(); }

    class ItemViewHolder extends RecyclerView.ViewHolder
    {

        ImageView image;
        public ItemViewHolder(View itemView)
        {
            super(itemView);
            this.image = (ImageView) itemView.findViewById(R.id.item_p);
        }
    }
}
