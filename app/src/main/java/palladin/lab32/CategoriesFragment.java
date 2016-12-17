package palladin.lab32;


import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;


public class CategoriesFragment extends Fragment
{
    RecyclerView recyclerList;
    AdapterCategoryRecycleList listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        getActivity().setTitle("Categories");
        return inflater.inflate(R.layout.fragment_categories, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        recyclerList = (RecyclerView)view.findViewById(R.id.recyclerCategories);
        listAdapter = new AdapterCategoryRecycleList(getActivity(), null, clickListener, longClickListener);
        recyclerList.setAdapter(listAdapter);
        recyclerList.setLayoutManager(new LinearLayoutManager(getActivity()));

        Cursor cursor = A.db.getCursor(Table.CATEGORIES);
        while (cursor.moveToNext())
        {
            int id = cursor.getInt(0);
            /////////////////////////
            long startRecordingTime = 0;
            Cursor recordsCursor = A.db.getCursor(Table.RECORDS, Table.Records.COL_CATEGORY_ID, id);
            recordsCursor.moveToLast();
            if(recordsCursor.getCount() != 0) //if record exists
                if(recordsCursor.getLong(3) == 0)//if still recording
                startRecordingTime = recordsCursor.getLong(2);
            ///////////////////////////
            String name = cursor.getString(1);

            listAdapter.addItem(new Model.Category(name, id, startRecordingTime));
        }



        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final EditText categoryTb = new EditText(getActivity());
                callTexViewAlertDialog("Create new category", categoryTb, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        addCategory(categoryTb.getText().toString());
                    }
                });
            }
        });
    }

    void startRecord(long startTime, Model.Category item)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.Records.COL_CATEGORY_ID, item.Id);
        contentValues.put(Table.Records.COL_START_TIME, startTime);
        if(A.db.insertData(Table.RECORDS, contentValues))
        {
            listAdapter.setRecording(startTime, item);
        }
    }

    void stopRecord(long endTime, Model.Category item)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.Records.COL_END_TIME, endTime);
        contentValues.put(Table.Records.COL_PERIOD, endTime - item.StartRecordTime);
        if(A.db.updateData(Table.RECORDS, contentValues, Table.Records.COL_START_TIME, item.StartRecordTime))
        {
            listAdapter.setRecording(0, item);
        }
    }

    void addCategory(String CategoryName)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.Categories.COL_CATEGORY_NAME, CategoryName);
        if(A.db.insertData(Table.CATEGORIES, contentValues))
        {
            //////////////////////////
            Cursor cursor = A.db.getCursor(Table.CATEGORIES);
            cursor.moveToLast();
            ////////////////
            int categoryId = cursor.getInt(0);
            listAdapter.addItem(new Model.Category(CategoryName, categoryId, 0));
        }
        else
            Toast.makeText(getActivity(), "Category with such name already exists!", Toast.LENGTH_LONG).show();
    }

    void renameCategory(String NewName, Model.Category item)
    {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Table.Categories.COL_CATEGORY_NAME, NewName);
        if(A.db.updateData(Table.CATEGORIES, contentValues, Table.Categories.COL_ID, item.Id))
            listAdapter.renameItem(NewName, item);
        else
            Toast.makeText(getActivity(), "Probably category with such name already exists!", Toast.LENGTH_LONG).show();
    }

    void deleteCategory(Model.Category item) {
        int res = A.db.deleteData(Table.CATEGORIES, Table.Categories.COL_ID, item.Id);
        if (res > 0)
        {
            A.db.deleteData(Table.RECORDS, Table.Records.COL_CATEGORY_ID, item.Id);
            listAdapter.deleteItem(item);
        }
        else
            Toast.makeText(getActivity(), "ERROR! +(", Toast.LENGTH_LONG).show();
    }

    void callTexViewAlertDialog(String title, EditText categoryTb, DialogInterface.OnClickListener onClickListener)
    {
        categoryTb.setHint("Enter unique name of a category");
        AlertDialog getCategoryName = new AlertDialog.Builder(getActivity())
                .setTitle(title)
                .setView(categoryTb)
                .setPositiveButton("SUBMIT", onClickListener)
                .create();
        getCategoryName.show();
    }

    void callChooseActionAlertDialog(DialogInterface.OnClickListener renameClickListener,
                                     DialogInterface.OnClickListener deleteClickListener)
    {
        AlertDialog dialogChooseAction = new AlertDialog.Builder(getActivity())
                .setTitle("Choose action")
                .setPositiveButton("Rename", renameClickListener)
                .setNegativeButton("Delete", deleteClickListener)
                .create();

        dialogChooseAction.show();
    }

    Handlers.LongClickHandler longClickListener = new Handlers.LongClickHandler()
    {
        @Override
        public boolean onLongClick(View v, Object data)
        {
            final Model.Category model = (Model.Category)data;
            callChooseActionAlertDialog(new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    final EditText categoryTb = new EditText(getActivity());
                    callTexViewAlertDialog("Rename category", categoryTb, new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            renameCategory(categoryTb.getText().toString(), model);
                        }
                    });
                }
            }, new DialogInterface.OnClickListener()
            {
                @Override
                public void onClick(DialogInterface dialog, int which)
                {
                    deleteCategory(model);
                }
            });
            return true;
        }
    };

    Handlers.ClickHandler clickListener = new Handlers.ClickHandler()
    {
        @Override
        public void onClick(View v, Object data)
        {
            Model.Category model = (Model.Category)data;
            Calendar calendar = Calendar.getInstance();
            if(model.StartRecordTime == 0)
                startRecord(calendar.getTimeInMillis(), model);
            else
                stopRecord(calendar.getTimeInMillis(), model);
        }
    };

}
