package palladin.lab32;


import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class RecordsFragment extends Fragment
{
    RecyclerView recyclerList;
    AdapterRecordRecycleList listAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Records");
        return inflater.inflate(R.layout.fragment_records, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        recyclerList = (RecyclerView)view.findViewById(R.id.recyclerRecords);
        listAdapter = new AdapterRecordRecycleList(getActivity(), null, onClick, onLongClick);
        recyclerList.setAdapter(listAdapter);
        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));



        Cursor cursor = A.db.getCursor(Table.RECORDS);
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            int categoryId = cursor.getInt(1);
            Cursor categoriesCursor = A.db.getCursor(Table.CATEGORIES, Table.Categories.COL_ID, categoryId);
            categoriesCursor.moveToLast();
            String category = categoriesCursor.getString(1);
            long startTime = cursor.getLong(2);
            long endTime = cursor.getLong(3);
            String desc = cursor.getString(5);

            listAdapter.addItem(new Model.Record(category, id, startTime, endTime, desc));
        }
    }

    void deleteRecord(Model.Record item)
    {
        int res = A.db.deleteData(Table.RECORDS, Table.Records.COL_START_TIME, item.StartTime);
        if(res > 0)
        {
            File photosDir = new File(A.imageDir, item.Id + "");

            File[] files =  photosDir.listFiles();
            if(files != null)
                for(File f: files)
                    f.delete();

            photosDir.delete();

            listAdapter.deleteItem(item);
        }
        else
            Toast.makeText(getActivity(), "ERROR! +(", Toast.LENGTH_LONG).show();
    }

    void setDescription(String description, Model.Record item)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.Records.COL_DESCRIPTION, description);
        if(A.db.updateData(Table.RECORDS, contentValues, Table.Categories.COL_ID, item.Id))
            listAdapter.setDescription(description, item);
        else
            Toast.makeText(getActivity(), "ERROR +(", Toast.LENGTH_LONG).show();
    }

    Handlers.ClickHandler onClick = new Handlers.ClickHandler()
    {
        @Override
        public void onClick(View v, Object data)
        {
            final Model.Record model = (Model.Record)data;
            switch (v.getId())
            {
                case R.id.item_r_add_description:
                    final EditText tb = new EditText(getActivity());
                    callTexViewAlertDialog("Description", tb, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            setDescription(tb.getText().toString(), model);
                        }
                    });
                    break;
                case R.id.item_r_show_photos:
                    Intent intent = new Intent(RecordsFragment.this.getActivity(), PhotosActivity.class);
                    intent.putExtra("id", model.Id);
                    startActivity(intent);
                    break;
                case R.id.item_r_add_photo:

                    File recordDir = new File(A.imageDir, Integer.toString(model.Id));

                    File[] list = recordDir.listFiles();

                    int count = 0;
                    if(list != null)
                        count = list.length;


                    //TODO!!!!
                    String fileName = count + ".jpg";
                    File filePath = new File(recordDir, fileName);

                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(filePath));
                    startActivityForResult(cameraIntent, 42);

            }
        }
    };


    void callTexViewAlertDialog(String title, EditText categoryTb, DialogInterface.OnClickListener onClickListener)
    {
        categoryTb.setHint("Write description here");
        AlertDialog getCategoryName = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(categoryTb)
                .setPositiveButton("Ok", onClickListener)
                .create();
        getCategoryName.show();
    }


    Handlers.LongClickHandler onLongClick = new Handlers.LongClickHandler()
    {
        @Override
        public boolean onLongClick(View v, Object data)
        {
            final Model.Record model = (Model.Record)data;
            AlertDialog dialogChooseAction = new AlertDialog.Builder(getActivity())
                    .setTitle("Delete record?")
                    .setPositiveButton("DELETE", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            deleteRecord(model);
                        }
                    })
                    .create();

            dialogChooseAction.show();

            return true;
        }
    };
}
