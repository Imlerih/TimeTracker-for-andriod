package palladin.lab32;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

/**
 * Created by Palladin on 05.12.2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper
{
    static final String DATABASE_NAME = "TimeLogger.db";
    private SQLiteDatabase db;

    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, 1);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(Table.Categories.CreateQuire);
        db.execSQL(Table.Records.CreateQuire);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

        db.execSQL("DROP TABLE IF EXISTS " + Table.CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + Table.RECORDS);
        onCreate(db);
    }

    public boolean insertData(String TableName, ContentValues TableRaw)
    {
        if(db.insert(TableName, null, TableRaw) == -1)
            return false;
        return true;
    }

    public  boolean updateData(String TableName, ContentValues ColumnsToUpdate, String KeyColumnName, Object KeyValue)
    {
        if(db.update(TableName, ColumnsToUpdate, KeyColumnName + " = ?", new String[] {KeyValue.toString()}) == -1)
            return false;
        return true;
    }

    public int deleteData(String TableName, String KeyColumnName, Object KeyValue)
    {
        return db.delete(TableName, KeyColumnName + " = ?", new String[]{KeyValue.toString()});
    }

    @Nullable
    public Cursor getCursor(String TableName)
    {
        Cursor cursor = db.rawQuery("select * from " + TableName, null);
        return cursor;
    }

    @Nullable
    public Cursor getCursor(String TableName, String KeyColumnName, Object KeyValue)
    {
        Cursor cursor = db.rawQuery("select * from " + TableName
                + " WHERE " + KeyColumnName + " = " + "'" + KeyValue.toString() + "'", null);
        return cursor;
    }

}
