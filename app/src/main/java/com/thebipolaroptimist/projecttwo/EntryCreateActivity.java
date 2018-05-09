package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDeleteDialog;
import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDiscardDialog;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;
import com.thebipolaroptimist.projecttwo.views.CategoryLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class EntryCreateActivity extends AppCompatActivity implements ConfirmDiscardDialog.ConfirmDiscardDialogListener, ConfirmDeleteDialog.ConfirmDeleteDialogListener {
    public static final String TAG = "EntryCreate";
    public static final int SEEKBAR_MIDDLE_VALUE = 50;
    private ProjectTwoDataSource mDataSource;
    private EditText mEditNote;
    private String mId;
    private SeekBar mSeekBarMood;
    private EntryDTO mEntryDTO;
    String mDate;
    private LinearLayout mCategoryLayoutList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        mEditNote = findViewById(R.id.edit_notes);
        mSeekBarMood = findViewById(R.id.seekbar_overall_mood);
        mCategoryLayoutList = findViewById(R.id.category_layout_list);

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();

        Intent intent = getIntent();
        mId = intent.getStringExtra(EntryListActivity.ENTRY_FIELD_ID);
        mDate = intent.getStringExtra(EntryCalendarActivity.DATE_FIELD);

        fillInUI();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);
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
        } else if(item.getItemId() == R.id.nav_item_ec_delete)
        {
            DialogFragment dialogFragment = new ConfirmDeleteDialog();
            dialogFragment.show(getSupportFragmentManager(), "ConfirmDeleteDialog");
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

        mEntryDTO.updateTimes(mDate);

        mEntryDTO.overallMood = mSeekBarMood.getProgress();

        int count = mCategoryLayoutList.getChildCount();

        for (int i = 0; i < count; i++) {
            CategoryLayout layout = (CategoryLayout) mCategoryLayoutList.getChildAt(i);
            mEntryDTO.saveList(layout.onSave(), layout.getCategory());
        }

        mDataSource.updateEntry(mId, mEntryDTO);
        finish();
    }

    private void fillInUI()
    {
        mSeekBarMood.setProgress(SEEKBAR_MIDDLE_VALUE);
        SimpleDateFormat format = new SimpleDateFormat(EntryCalendarActivity.DATE_FORMAT_PATTERN);
        if (mEntryDTO == null) {
            mEntryDTO = new EntryDTO();
        }

        if (mId != null) {
            Entry entry = mDataSource.getEntry(mId);
            EntryDTO.EntryToEntryDTO(entry, mEntryDTO);
            mEditNote.setText(mEntryDTO.entryNote);
            mSeekBarMood.setProgress(mEntryDTO.overallMood);
            //fill in date with entry info
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(mEntryDTO.entryTime));
            setTitle(getTitle() + format.format(calendar.getTime()));
        } else if (mDate != null) {
            //fill in date with passed in date
            setTitle(getTitle() + " " + mDate);
        } else {
            //fill in date with current date
            setTitle(getTitle() + format.format(System.currentTimeMillis()));
        }

        String[] categories = SettingsFragment.CATEGORIES_ARRAY;
        for (String category : categories) {
            mCategoryLayoutList.addView(new CategoryLayout(this, category, mEntryDTO.getDetailList(category)));
        }

    }

    @Override
    public void onBackPressed() {
        DialogFragment dialogFragment = new ConfirmDiscardDialog();
        dialogFragment.show(getSupportFragmentManager(), "ConfirmDiscardDialog");
    }

    @Override
    public void onConfirmDialogPositiveClick(DialogFragment dialog) {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onConfirmDialogNegativeClick(DialogFragment dialog)
    {
        onSave();
    }

    @Override
    public void onConfirmDeletePositiveClick(DialogFragment dialogFragment) {
        if(mId != null) {
            mDataSource.deleteEntry(mId);
        }
        super.onBackPressed();
        finish();
    }
}


