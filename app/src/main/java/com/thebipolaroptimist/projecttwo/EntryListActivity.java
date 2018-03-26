package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.models.Entry;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryListActivity extends BaseActivity {

    private ProjectTwoDataSource mDataSource;
    public static final String ENTRY_FIELD_ID = "entry_id";
    CaldroidFragment caldroidFragment = new CaldroidCustomFragment();
    Map<String, Object> mAdapterData;
    final SimpleDateFormat mFormatter = new SimpleDateFormat("dd MMM yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_entry_list);
        super.onCreate(savedInstanceState);

        caldroidFragment = new CaldroidCustomFragment();

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();

        caldroidFragment.setExtraData(mAdapterData);

        if(savedInstanceState != null)
        {
            caldroidFragment.restoreStatesFromKey(
                    savedInstanceState, "CALDROID_SAVED_STATE"
            );
        } else
        {
            Bundle args = new Bundle();
            Calendar cal = Calendar.getInstance();
            args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
            args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
            args.putBoolean(CaldroidFragment.ENABLE_SWIPE, true);
            args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, true);
            caldroidFragment.setArguments(args);
        }

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
        t.replace(R.id.calendar1, caldroidFragment);
        t.commit();

        // Setup listener
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                Toast.makeText(getApplicationContext(), mFormatter.format(date),
                        Toast.LENGTH_SHORT).show();
                openEntryForEdit(date);
            }

            @Override
            public void onChangeMonth(int month, int year) {
                String text = "month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClickDate(Date date, View view) {
                Toast.makeText(getApplicationContext(),
                        "Long click " + mFormatter.format(date),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCaldroidViewCreated() {
                if (caldroidFragment.getLeftArrowButton() != null) {
                    Toast.makeText(getApplicationContext(),
                            "Caldroid view is created", Toast.LENGTH_SHORT)
                            .show();
                }
            }

        };

        // Setup Caldroid
        caldroidFragment.setCaldroidListener(listener);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        List<Entry> data = mDataSource.getAllEntries();
        mAdapterData = new HashMap<>();
        for (Entry datum : data) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(datum.getEntryTime()));
            String key = formatter.format(calendar.getTime());
            List<Entry> entries = (List<Entry>) mAdapterData.get(key);
            if(entries == null)
            {
                entries= new ArrayList<>();
                mAdapterData.put(key, entries);
            }
            entries.add(datum);
        }
        caldroidFragment.setExtraData(mAdapterData);
        caldroidFragment.refreshView();
    }

    public void openEntryForEdit(Date date)
    {
        Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
        String key = mFormatter.format(date);
        List<Entry> entries = (List<Entry>) mAdapterData.get(key);
        if(entries.size() > 0) {
            intent.putExtra(EntryListActivity.ENTRY_FIELD_ID, entries.get(0).getId());
        }
        startActivity(intent);
    }

    @Override
    protected void onDestroy()
    {
        mDataSource.close();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, "CALDROID_SAVED_STATE");
        }
    }
}

