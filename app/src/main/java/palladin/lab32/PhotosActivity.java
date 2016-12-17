package palladin.lab32;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotosActivity extends AppCompatActivity
{

    RecyclerView recyclerView;
    File photosDir;
    List<Integer> toDelete = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.photos_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        photosDir = new File(A.imageDir, getIntent().getIntExtra("id", 0) + "");

        List<String> uris = new ArrayList<>();

            File[] files =  photosDir.listFiles();
            if(files != null)
            for(File f: files)
                uris.add(f.getAbsolutePath());

        recyclerView.setAdapter(new AdapterPhotosRecyclerList(this, uris));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
