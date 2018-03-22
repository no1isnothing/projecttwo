package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;
import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.dialogs.ActivityDialog;
import com.thebipolaroptimist.projecttwo.dialogs.MoodDialog;
import com.thebipolaroptimist.projecttwo.models.Detail;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmList;

public class EntryListActivity extends BaseActivity {

    private ProjectTwoDataSource mDataSource;
    public static final String ENTRY_FIELD_ID = "entry_id";
    CaldroidFragment caldroidFragment = new CaldroidCustomFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_entry_list);
        super.onCreate(savedInstanceState);


        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        caldroidFragment = new CaldroidCustomFragment();

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();


        Entry entry1 = new Entry();
        RealmList<Detail> list = new RealmList<>();
        Detail moodDetail = new Detail();
        moodDetail.setColor(Color.RED);
        moodDetail.setCategory(MoodDialog.CATEGORY);
        Detail activiyDetail = new Detail();
        activiyDetail.setColor(Color.BLUE);
        activiyDetail.setCategory(ActivityDialog.CATEGORY);
        list.add(moodDetail);
        list.add(activiyDetail);

        Long time = System.currentTimeMillis(); // be last
        entry1.setEntryTime(time.toString());
        entry1.setDetailList(list);
        mDataSource.createEntry(entry1);

        List<Entry> data = mDataSource.getAllEntries();
        Map<String, Object> adapterData = new HashMap<>();
        for (Entry datum : data) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(entry1.getEntryTime()));
            String key = formatter.format(calendar.getTime());
            Entry entry = (Entry) adapterData.get(key);
            if(entry != null)
            {
                entry.getDetailList().addAll(datum.getDetailList());
            } else
            {
                adapterData.put(key, datum);
            }
        }
        caldroidFragment.setExtraData(adapterData);

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
                Toast.makeText(getApplicationContext(), formatter.format(date),
                        Toast.LENGTH_SHORT).show();
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
                        "Long click " + formatter.format(date),
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
        /**
        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();
        mRecyclerView = findViewById(R.id.entries_view);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new EntryAdapter((OrderedRealmCollection<Entry>) mDataSource.getAllEntries(), true, this);
        mRecyclerView.setAdapter(mAdapter);*/
    }

    public void openEntryForEdit(int entryPosition)
    {
        Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
        //String entryId =  mAdapter.getEntryId(entryPosition);
        //intent.putExtra(ENTRY_FIELD_ID, entryId);
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

