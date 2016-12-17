package palladin.lab32;

import android.app.Application;
import android.os.Environment;

import java.io.File;


public class A extends Application
{
    static  DatabaseHelper db;
    static File imageDir;

    @Override
    public void onCreate()
    {
        super.onCreate();
        db = new DatabaseHelper(this);
        imageDir = new File(Environment.getExternalStorageDirectory(), "imageDir");
    }
}
