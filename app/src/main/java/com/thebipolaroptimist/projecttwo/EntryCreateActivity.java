package com.thebipolaroptimist.projecttwo;

import android.arch.lifecycle.ViewModelProviders;
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

import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDeleteDialog;
import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDiscardDialog;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;
import com.thebipolaroptimist.projecttwo.views.CategoryLayout;

import java.util.Map;


public class EntryCreateActivity extends AppCompatActivity implements ConfirmDiscardDialog.ConfirmDiscardDialogListener, ConfirmDeleteDialog.ConfirmDeleteDialogListener {
    private static final String TAG = "EntryCreate";

    private EditText mEditNote;
    private SeekBar mSeekBarMood;
    private LinearLayout mCategoryLayoutList;
    private EntryCreateViewModel mViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        mEditNote = findViewById(R.id.edit_notes);
        mSeekBarMood = findViewById(R.id.seekbar_overall_mood);
        mCategoryLayoutList = findViewById(R.id.category_layout_list);

        mViewModel = ViewModelProviders.of(this).get(EntryCreateViewModel.class);

        Intent intent = getIntent();
        mViewModel.setId(intent.getStringExtra(EntryListActivity.ENTRY_FIELD_ID));
        mViewModel.setDate(intent.getStringExtra(EntryCalendarActivity.DATE_FIELD));

        fillInUI();

        if(getSupportActionBar() != null) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);
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
        switch (item.getItemId()) {
            case R.id.nav_item_ec_save:
                onSave();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.nav_item_ec_delete:
                DialogFragment dialogFragment = new ConfirmDeleteDialog();
                dialogFragment.show(getSupportFragmentManager(), "ConfirmDeleteDialog");
                break;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void onSave() {
        storeData();
        mViewModel.updateEntryTime();
        mViewModel.updateEntry();
        finish();
    }

    private void storeData()
    {
        mViewModel.mEntryDTO.entryNote = mEditNote.getText().toString();
        mViewModel.mEntryDTO.overallMood = mSeekBarMood.getProgress();

        int count = mCategoryLayoutList.getChildCount();

        for (int i = 0; i < count; i++) {
            CategoryLayout layout = (CategoryLayout) mCategoryLayoutList.getChildAt(i);
            mViewModel.mEntryDTO.saveList(layout.onSave(), layout.getCategory());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        storeData();
    }

    private void fillInUI()
    {
        mEditNote.setText(mViewModel.mEntryDTO.entryNote);
        mSeekBarMood.setProgress(mViewModel.mEntryDTO.overallMood);

        setTitle(mViewModel.getActivityTitle(getTitle()));

        String[] categories = SettingsFragment.CATEGORIES_ARRAY;
        for (String category : categories) {
            mCategoryLayoutList.addView(new CategoryLayout(this, category, mViewModel.mEntryDTO.getDetailList(category)));
        }
    }

    @Override
    public void onBackPressed() {
        DialogFragment dialogFragment = new ConfirmDiscardDialog();
        dialogFragment.show(getSupportFragmentManager(), "ConfirmDiscardDialog");
    }

    @Override
    public void onConfirmDialogPositiveClick() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onConfirmDialogNegativeClick()
    {
        onSave();
    }

    @Override
    public void onConfirmDeletePositiveClick() {
        mViewModel.deleteEntry();
        super.onBackPressed();
        finish();
    }
}


