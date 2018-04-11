package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.SeekBar;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.dialogs.ActivityDialog;
import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDiscardDialog;
import com.thebipolaroptimist.projecttwo.dialogs.IncidentDialog;
import com.thebipolaroptimist.projecttwo.dialogs.MoodDialog;
import com.thebipolaroptimist.projecttwo.models.DetailsAdapter;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;


public class EntryCreateActivity extends AppCompatActivity implements ConfirmDiscardDialog.ConfirmDiscardDialogListener, MoodDialog.MoodDialogListener, ActivityDialog.ActivityDialogListener, IncidentDialog.IncidentDialogListener
{
    public static final String TAG = "EntryCreate";
    public static final int SEEKBAR_MIDDLE_VALUE = 50;
    private ProjectTwoDataSource mDataSource;
    private EditText mEditNote;
    private String mId;
    private SeekBar mSeekBarMood;
    private EntryDTO mEntryDTO;
    private DetailsAdapter mDetailsAdapter;
    String mDate;
    DialogFragment mDialogFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        mEditNote = findViewById(R.id.edit_notes);
        mSeekBarMood = findViewById(R.id.seekbar_overall_mood);

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();
        SimpleDateFormat format = new SimpleDateFormat(EntryCalendarActivity.DATE_FORMAT_PATTERN);

        //fill out data if entry already exists
        Intent intent = getIntent();
        mId = intent.getStringExtra(EntryListActivity.ENTRY_FIELD_ID);
        mDate = intent.getStringExtra(EntryCalendarActivity.DATE_FIELD);
        mSeekBarMood.setProgress(SEEKBAR_MIDDLE_VALUE);
        if(mId != null)
        {
            Entry entry = mDataSource.getEntry(mId);
            if(mEntryDTO == null)
            {
                mEntryDTO = new EntryDTO();
            }
            EntryDTO.EntryToEntryDTO(entry, mEntryDTO);
            mEditNote.setText(mEntryDTO.entryNote);
            mSeekBarMood.setProgress(mEntryDTO.overallMood);
            //fill in date with entry info
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(mEntryDTO.entryTime));
            setTitle(getTitle() + " " + format.format(calendar.getTime()));
            createOrUpdateDetailsView();
        } else if(mDate != null)
        {
            //fill in date with passed in date
            setTitle(getTitle() + " " + mDate);
        } else
        {
            //fill in date with current date
            setTitle(getTitle() + " " + format.format(System.currentTimeMillis()));
        }

        //set up speed dial to add different kinds of detail
        FabSpeedDial fabSpeedDial = findViewById(R.id.fab_add_detail);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Log.i(TAG, "Menu item id" + menuItem.getItemId());

                switch (menuItem.getItemId())
                {
                    case R.id.action_activity:
                        mDialogFragment = new ActivityDialog();
                        mDialogFragment.show(getSupportFragmentManager(), "ActivityDialog");
                        break;
                    case R.id.action_incident:
                        mDialogFragment = new IncidentDialog();
                        mDialogFragment.show(getSupportFragmentManager(), "IncidentDialog");
                        break;
                    case R.id.action_mood:
                        mDialogFragment = new MoodDialog();
                        //Send mood data if it exists
                        if(mEntryDTO != null && mEntryDTO.categoriesToDetails != null) {
                            Bundle bundle = new Bundle();
                            Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(MoodDialog.CATEGORY);
                            if(detailDTOMap != null) {
                                Set<String> keys = detailDTOMap.keySet();
                                for (String key : keys) {
                                    DetailDTO detailDTO = detailDTOMap.get(key);
                                    bundle.putInt(detailDTO.detailType, Integer.parseInt(detailDTO.detailData));
                                }
                                mDialogFragment.setArguments(bundle);
                            }
                        }
                        mDialogFragment.show(getSupportFragmentManager(), "MoodDialog");
                        break;
                }
                return true;
            }
        });

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "On resume");
        if(mDialogFragment != null && mDialogFragment.isVisible())
        {
            mDialogFragment.onResume();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.nav_item_entry_create, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.nav_item_ec_save)
        {
            onSave();
        } else if(item.getItemId() == android.R.id.home)
        {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        mDataSource.close();
        super.onDestroy();
    }

    private void onSave()
    {
        if(mEntryDTO == null)
        {
            mEntryDTO = new EntryDTO();
        }
        mEntryDTO.entryNote=mEditNote.getText().toString();


        Long time = System.currentTimeMillis();
        if(mDate != null)
        {
            SimpleDateFormat format = new SimpleDateFormat(EntryCalendarActivity.DATE_FORMAT_PATTERN);
            try {
                Date date =  format.parse(mDate);
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                calendar.setTimeInMillis(date.getTime());
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE,minute);
                time = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        mEntryDTO.lastEditedTime=time.toString();

        if(mEntryDTO.entryTime == null)
        {
            mEntryDTO.entryTime = mEntryDTO.lastEditedTime;
        }
        mEntryDTO.overallMood=mSeekBarMood.getProgress();

        mDataSource.updateEntry(mId, mEntryDTO);

        finish();
    }

    @Override
    public void onBackPressed() {
        DialogFragment dialogFragment = new ConfirmDiscardDialog();
        dialogFragment.show(getSupportFragmentManager(), "ConfirmDiscardDialog");
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        onSave();
    }

    @Override
    public void onMoodDialogPositiveClick(List<DetailDTO> moodList) {
        if(mEntryDTO == null)
        {
            mEntryDTO = new EntryDTO();
        }

        Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(MoodDialog.CATEGORY);

        if(detailDTOMap ==null)
        {
            detailDTOMap = new HashMap<>();
            mEntryDTO.categoriesToDetails.put(MoodDialog.CATEGORY, detailDTOMap);
            mEntryDTO.detailCategories.add(MoodDialog.CATEGORY);
        }

        for (DetailDTO detailDTO : moodList) {
            detailDTOMap.put(detailDTO.detailType, detailDTO);
        }

        for (DetailDTO moodDetailDTO : moodList) {
            Log.i(TAG, "Mood " + moodDetailDTO.detailType + " Intensity " + moodDetailDTO.detailData);
        }
        createOrUpdateDetailsView();
    }

    @Override
    public void onActivityDialogPositiveClick(DetailDTO activityDetailDTO) {
        if(mEntryDTO == null)
        {
            mEntryDTO = new EntryDTO();
        }

        Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(ActivityDialog.CATEGORY);
        if(detailDTOMap == null)
        {
            detailDTOMap = new HashMap<>();
            mEntryDTO.categoriesToDetails.put(ActivityDialog.CATEGORY, detailDTOMap);
            mEntryDTO.detailCategories.add(ActivityDialog.CATEGORY);
        }

        detailDTOMap.put(activityDetailDTO.detailType, activityDetailDTO);
        createOrUpdateDetailsView();
    }

    @Override
    public void onIncidentDialogPositiveClick(DetailDTO incidentDetailDTO) {
        if(mEntryDTO == null)
        {
            mEntryDTO = new EntryDTO();
        }

        Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(IncidentDialog.CATEGORY);
        if(detailDTOMap == null)
        {
            detailDTOMap = new HashMap<>();
            mEntryDTO.categoriesToDetails.put(IncidentDialog.CATEGORY, detailDTOMap);
            mEntryDTO.detailCategories.add(IncidentDialog.CATEGORY);
        }

        detailDTOMap.put(incidentDetailDTO.detailType, incidentDetailDTO);
        createOrUpdateDetailsView();
    }

    private void createOrUpdateDetailsView()
    {
        if(mDetailsAdapter == null)
        {
            mDetailsAdapter = new DetailsAdapter(this, mEntryDTO.detailCategories, mEntryDTO.categoriesToDetails);
            ExpandableListView detailsView = findViewById(R.id.list_details);
            detailsView.setAdapter(mDetailsAdapter);
        } else {
            mDetailsAdapter.notifyDataSetChanged();
        }
    }


}


