package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryAdapter;

import java.util.List;

public class EntryListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ProjectTwoDataSource mDataSource;
    private EntryAdapter mAdapter;
    public static final String ENTRY_FIELD_ID = "entry_id";
    private String mDay;
    List<Entry> mEntries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_entry_list);
        super.onCreate(savedInstanceState);

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();
        Intent intent = getIntent();
        mDay = intent.getStringExtra(EntryCalendarActivity.DATE_FIELD);
        setTitle(getTitle() + " " + mDay);

        Button fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
                intent.putExtra(EntryCalendarActivity.DATE_FIELD, mDay);
                startActivity(intent);
            }
        });

        mRecyclerView = findViewById(R.id.entries_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mEntries = mDataSource.getEntriesForDay(mDay);

        mAdapter = new EntryAdapter(mEntries.toArray(new Entry[0]), this);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void openEntryForEdit(int entryPosition)
    {
        Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
        String entryId =  mAdapter.getEntryId(entryPosition);
        intent.putExtra(ENTRY_FIELD_ID, entryId);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle your other action bar items...
        if(item.getItemId() == R.id.action_settings)
        {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy()
    {
        mDataSource.close();
        super.onDestroy();
    }
}
