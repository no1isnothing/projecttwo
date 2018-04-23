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
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.dialogs.ActivityDetailDialog;
import com.thebipolaroptimist.projecttwo.dialogs.BaseDetailDialog;
import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDiscardDialog;
import com.thebipolaroptimist.projecttwo.dialogs.IncidentDetailDialog;
import com.thebipolaroptimist.projecttwo.dialogs.MoodDetailDialog;
import com.thebipolaroptimist.projecttwo.models.DetailsAdapter;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;
import com.thebipolaroptimist.projecttwo.views.CategoryLayout;
import com.thebipolaroptimist.projecttwo.views.DetailRow;
import com.thebipolaroptimist.projecttwo.views.MoodDetailRow;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;


public class EntryCreateActivity extends AppCompatActivity implements ConfirmDiscardDialog.ConfirmDiscardDialogListener, BaseDetailDialog.DetailDialogListener {
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
    LinearLayout mCategoryLayoutList;

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
        if (mId != null) {
            Entry entry = mDataSource.getEntry(mId);
            if (mEntryDTO == null) {
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
        } else if (mDate != null) {
            //fill in date with passed in date
            setTitle(getTitle() + " " + mDate);
        } else {
            //fill in date with current date
            setTitle(getTitle() + " " + format.format(System.currentTimeMillis()));
        }

        mCategoryLayoutList = findViewById(R.id.category_layout_list);

        List<DetailDTO> details = new ArrayList<>();
        if(mEntryDTO != null && mEntryDTO.categoriesToDetails != null) {

            Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(MoodDetailRow.CATEGORY);
            if(detailDTOMap != null) {
                Set<String> keys = detailDTOMap.keySet();
                for (String key : keys) {
                    details.add(detailDTOMap.get(key));
                }
            }
        }
        mCategoryLayoutList.addView(new CategoryLayout(this, MoodDetailRow.CATEGORY, details));
        /*
        //set up speed dial to add different kinds of detail
        FabSpeedDial fabSpeedDi = findViewById(R.id.fab_add_detail);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Log.i(TAG, "Menu item id" + menuItem.getItemId());

                switch (menuItem.getItemId())
                {
                    case R.id.action_activity:
                        mDialogFragment = new ActivityDetailDialog();
                        mDialogFragment.show(getSupportFragmentManager(), "ActivityDetailDialog");
                        break;
                    case R.id.action_incident:
                        mDialogFragment = new IncidentDetailDialog();
                        mDialogFragment.show(getSupportFragmentManager(), "IncidentDetailDialog");
                        break;
                    case R.id.action_mood:
                        mDialogFragment = new MoodDetailDialog();
                        //Send mood data if it exists
                        if(mEntryDTO != null && mEntryDTO.categoriesToDetails != null) {
                            Bundle bundle = new Bundle();
                            Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(MoodDetailRow.CATEGORY);
                            if(detailDTOMap != null) {
                                Set<String> keys = detailDTOMap.keySet();
                                for (String key : keys) {
                                    DetailDTO detailDTO = detailDTOMap.get(key);
                                    bundle.putInt(detailDTO.detailType, Integer.parseInt(detailDTO.detailData));
                                }
                                mDialogFragment.setArguments(bundle);
                            }
                        }
                        mDialogFragment.show(getSupportFragmentManager(), "MoodDetailDialog");
                        break;
                }
                return true;
            }
        });*/

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "On resume");
        if (mDialogFragment != null && mDialogFragment.isVisible()) {
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
        if (item.getItemId() == R.id.nav_item_ec_save) {
            onSave();
        } else if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        mDataSource.close();
        super.onDestroy();
    }

    private void onSave() {
        if (mEntryDTO == null) {
            mEntryDTO = new EntryDTO();
        }
        mEntryDTO.entryNote = mEditNote.getText().toString();


        Long time = System.currentTimeMillis();
        if (mDate != null) {
            SimpleDateFormat format = new SimpleDateFormat(EntryCalendarActivity.DATE_FORMAT_PATTERN);
            try {
                Date date = format.parse(mDate);
                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);
                calendar.setTimeInMillis(date.getTime());
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                time = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        mEntryDTO.lastEditedTime = time.toString();

        if (mEntryDTO.entryTime == null) {
            mEntryDTO.entryTime = mEntryDTO.lastEditedTime;
        }
        mEntryDTO.overallMood = mSeekBarMood.getProgress();

        //get deetail dtos
        int count = mCategoryLayoutList.getChildCount();

        for (int i = 0; i < count; i++) {
            CategoryLayout layout = (CategoryLayout) mCategoryLayoutList.getChildAt(i);


            List<DetailDTO> detailDTOS = layout.onSave();
            if(detailDTOS.size() > 0) {
                String category = detailDTOS.get(0).category;
                Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(category);
                if (detailDTOMap == null) {
                    detailDTOMap = new HashMap<>();
                    mEntryDTO.categoriesToDetails.put(category, detailDTOMap);
                    mEntryDTO.detailCategories.add(category);
                }

                for (DetailDTO dto : detailDTOS) {
                    detailDTOMap.put(dto.detailType, dto);
                }
            }
        }

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

    private void createOrUpdateDetailsView() {
        /*if(mDetailsAdapter == null)
        {
            mDetailsAdapter = new DetailsAdapter(this, mEntryDTO.detailCategories, mEntryDTO.categoriesToDetails);
            ExpandableListView detailsView = findViewById(R.id.list_details);
            detailsView.setAdapter(mDetailsAdapter);
        } else {
            mDetailsAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onDetailDialogPositiveClick(DetailDTO detailDTO, String category) {
        if (mEntryDTO == null) {
            mEntryDTO = new EntryDTO();
        }

        Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(category);
        if (detailDTOMap == null) {
            detailDTOMap = new HashMap<>();
            mEntryDTO.categoriesToDetails.put(category, detailDTOMap);
            mEntryDTO.detailCategories.add(category);
        }

        detailDTOMap.put(detailDTO.detailType, detailDTO);
        createOrUpdateDetailsView();
    }

    @Override
    public void onDetailDialogPositiveClick(List<DetailDTO> detailDTOList, String category) {
        if (mEntryDTO == null) {
            mEntryDTO = new EntryDTO();
        }

        Map<String, DetailDTO> detailDTOMap = mEntryDTO.categoriesToDetails.get(category);

        if (detailDTOMap == null) {
            detailDTOMap = new HashMap<>();
            mEntryDTO.categoriesToDetails.put(category, detailDTOMap);
            mEntryDTO.detailCategories.add(category);
        }

        for (DetailDTO detailDTO : detailDTOList) {
            detailDTOMap.put(detailDTO.detailType, detailDTO);
        }

        createOrUpdateDetailsView();
    }
}


