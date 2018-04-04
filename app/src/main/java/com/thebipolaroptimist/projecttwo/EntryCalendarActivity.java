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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class EntryCalendarActivity extends BaseActivity {

    private ProjectTwoDataSource mDataSource;
    public static final String DATE_FIELD = "date_field";
    public static final String DATE_FORMAT_PATTERN = "MMM dd yyyy";
    public static final String CALDROID_SAVE_KEY = "CALDROID_SAVED_STATE";
    CaldroidFragment caldroidFragment;
    Map<String, Object> mAdapterData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_entry_calendar);
        super.onCreate(savedInstanceState);

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();

        caldroidFragment = new CaldroidCustomFragment();
        setupCaldroid(savedInstanceState);

        FloatingActionButton fab = findViewById(R.id.fab_add);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), EntryCreateActivity.class);
                startActivity(intent);
            }
        });
    }

    public void setupCaldroid(Bundle savedInstanceState)
    {
        if(savedInstanceState != null)
        {
            caldroidFragment.restoreStatesFromKey(
                    savedInstanceState, CALDROID_SAVE_KEY
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

        caldroidFragment.setCaldroidListener(new CaldroidListener() {

            @Override
            public void onSelectDate(Date date, View view) {
                openEntryListForDay(date);
            }

            @Override
            public void onChangeMonth(int month, int year) {

            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAdapterData = mDataSource.getEntriesByDay();
        caldroidFragment.setExtraData(mAdapterData);
        caldroidFragment.refreshView();
    }

    public void openEntryListForDay(Date date)
    {
        Intent intent = new Intent(getApplicationContext(), EntryListActivity.class);
        final SimpleDateFormat mFormatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);
        String key = mFormatter.format(date);
        intent.putExtra(EntryCalendarActivity.DATE_FIELD, key);

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
        super.onSaveInstanceState(outState);

        if (caldroidFragment != null) {
            caldroidFragment.saveStatesToKey(outState, CALDROID_SAVE_KEY);
        }
    }
}

