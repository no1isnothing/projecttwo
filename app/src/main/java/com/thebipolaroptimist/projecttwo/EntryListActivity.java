package com.thebipolaroptimist.projecttwo;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryAdapter;

public class EntryListActivity extends BaseActivity {
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_entry_list);
        super.onCreate(savedInstanceState);

        mRecyclerView = findViewById(R.id.entries_view);

        Entry[] entries = new Entry[10];

        for (int i = 0; i < entries.length; i++) {
            entries[i] = new Entry("Entry Number " +  i);
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new EntryAdapter(entries));
    }
}
