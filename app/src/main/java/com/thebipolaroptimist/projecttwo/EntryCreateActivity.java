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

import com.thebipolaroptimist.projecttwo.dialogs.AddDetailsDialog;
import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDeleteDialog;
import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDiscardDialog;
import com.thebipolaroptimist.projecttwo.views.CategoryLayout;


public class EntryCreateActivity extends AppCompatActivity implements ConfirmDiscardDialog.ConfirmDiscardDialogListener,
        ConfirmDeleteDialog.ConfirmDeleteDialogListener  {
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

        //Setup data in the ViewModel if it's the first time onCreate is called
        if(savedInstanceState == null) {
            Intent intent = getIntent();
            mViewModel.setupEntry(intent.getStringExtra(EntryListActivity.ENTRY_FIELD_ID));
            mViewModel.setDate(intent.getStringExtra(EntryCalendarActivity.DATE_FIELD));
        }

        fillInUI();

        //Check to see if AddDetailsDialog is opened and set listener if it is
        if(savedInstanceState != null)
        {
            for(int i = 0; i < mCategoryLayoutList.getChildCount(); i++) {
                CategoryLayout categoryLayout = (CategoryLayout) mCategoryLayoutList.getChildAt(i);
                AddDetailsDialog fragment = (AddDetailsDialog) getSupportFragmentManager().findFragmentByTag("AddDetailsDialog_" + categoryLayout.getCategory());
                if (fragment != null) {
                    fragment.setListener(categoryLayout);
                }
            }
        }

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

    private void onSave() {
        storeDataInViewModel();
        mViewModel.updateEntryTime();
        mViewModel.updateEntryInDB();
        finish();
    }

    private void storeDataInViewModel()
    {
        mViewModel.mEntryDTO.entryNote = mEditNote.getText().toString();
        mViewModel.mEntryDTO.overallMood = mSeekBarMood.getProgress();

        int count = mCategoryLayoutList.getChildCount();

        for (int i = 0; i < count; i++) {
            CategoryLayout layout = (CategoryLayout) mCategoryLayoutList.getChildAt(i);
            mViewModel.mEntryDTO.saveList(layout.onSave(), layout.getCategory());
            mViewModel.storeCategoryExpanded(layout.getCategory(), layout.isExpanded());
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        storeDataInViewModel();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        //For some reason this was causing all the seekbars to get set to the same value as the last one
        //when mWindow.restoreHierarchyState(windowState); is called in the base Activity class
        //The functional problem is fixed. Need to check this doens't have unintended consequences
        //super.onRestoreInstanceState(savedInstanceState);
    }

    private void fillInUI()
    {
        mEditNote.setText(mViewModel.mEntryDTO.entryNote);
        mSeekBarMood.setProgress(mViewModel.mEntryDTO.overallMood);

        setTitle(mViewModel.getActivityTitle(getTitle()));

        String[] categories = SettingsFragment.CATEGORIES_ARRAY;
        for (String category : categories) {
            mCategoryLayoutList.addView(new CategoryLayout(this, category,
                    mViewModel.mEntryDTO.getDetailList(category), mViewModel.isCategoryExpanded(category)));
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
        mViewModel.deleteEntryFromDB();
        super.onBackPressed();
        finish();
    }
}


