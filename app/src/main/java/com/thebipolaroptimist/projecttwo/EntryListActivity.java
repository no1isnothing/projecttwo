package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryAdapter;

import io.realm.OrderedRealmCollection;

public class EntryListActivity extends BaseActivity {

    private RecyclerView mRecyclerView;
    private ProjectTwoDataSource mDataSource;
    private EntryAdapter mAdapter;
    public static final int CREATE_ENTRY = 1001;
    public static final String ENTRY_FIELD_ID = "entry_id";
    public static final String ENTRY_FIELD_NAME = "entry_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_entry_list);
        super.onCreate(savedInstanceState);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
                startActivityForResult(intent,CREATE_ENTRY);
            }
        });

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();
        mRecyclerView = findViewById(R.id.entries_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EntryAdapter((OrderedRealmCollection<Entry>)mDataSource.getAllEntries(), true, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    //fix this after the create is updated
    public void openEntryForEdit(int entryPosition)
    {
        Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
        String entryId =  mAdapter.getEntryId(entryPosition);
        intent.putExtra(ENTRY_FIELD_ID, entryId);
        intent.putExtra(ENTRY_FIELD_NAME, mDataSource.getEntry(entryId).getEntryName());
        startActivityForResult(intent, CREATE_ENTRY);
    }

    @Override
    protected void onDestroy()
    {
        mDataSource.close();
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CREATE_ENTRY && resultCode == RESULT_OK)
        {
            String name = data.getStringExtra(ENTRY_FIELD_NAME);

            if(data.hasExtra(ENTRY_FIELD_ID))
            {
                //update entry in datasource
                String id = data.getStringExtra(ENTRY_FIELD_ID);
                mDataSource.updateEntry(id, name);
            } else {
                //create new entry
                Entry entry = new Entry();
                entry.setEntryName(name);
                mDataSource.createEntry(entry);
            }
        }
    }
}

