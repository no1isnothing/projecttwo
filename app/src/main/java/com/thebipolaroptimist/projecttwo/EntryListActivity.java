package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryAdapter;

import java.util.List;

public class EntryListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ProjectTwoDataSource mDataSource;
    private EntryAdapter mAdapter;
    public static final String ENTRY_FIELD_ID = "entry_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_entry_list);
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
                startActivity(intent);
            }
        });

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();
        Intent intent = getIntent();
        String day = intent.getStringExtra(EntryCalendarActivity.DATE_FIELD);
        List<Entry> entries = mDataSource.getEntriesForDay(day);
        mRecyclerView = findViewById(R.id.entries_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new EntryAdapter(entries.toArray(new Entry[0]), this);

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
    protected void onDestroy()
    {
        mDataSource.close();
        super.onDestroy();
    }
}
