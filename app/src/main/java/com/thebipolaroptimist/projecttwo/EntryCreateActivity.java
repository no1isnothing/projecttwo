package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.dialogs.ActivityDialog;
import com.thebipolaroptimist.projecttwo.dialogs.ConfirmDiscardDialog;
import com.thebipolaroptimist.projecttwo.dialogs.IncidentDialog;
import com.thebipolaroptimist.projecttwo.dialogs.MoodDialog;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

public class EntryCreateActivity extends AppCompatActivity implements ConfirmDiscardDialog.ConfirmDiscardDialogListener {
    public static final String TAG = "EntryCreaete";
    private ProjectTwoDataSource mDataSource;
    private EditText mEditNote;
    private String mId; //make sure this field is getting reset
    private SeekBar mSeekBarMood;
    private String mEntryTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        mEditNote = findViewById(R.id.edit_notes);
        mSeekBarMood = findViewById(R.id.seekbar_overall_mood);

        FabSpeedDial fabSpeedDial = findViewById(R.id.fab_add_detail);
        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                Log.i(TAG, "Menu item id" + menuItem.getItemId());

                switch (menuItem.getItemId())
                {
                    case R.id.action_activity:
                        DialogFragment aDialog = new ActivityDialog();
                        aDialog.show(getSupportFragmentManager(), "ActivityDialog");
                        break;
                    case R.id.action_incident:
                        DialogFragment dialogFragment = new IncidentDialog();
                        dialogFragment.show(getSupportFragmentManager(), "IncidentDialog");
                        break;
                    case R.id.action_mood:
                        DialogFragment dialog = new MoodDialog();
                        dialog.show(getSupportFragmentManager(), "MoodDialog");
                        break;
                }
                return true;
            }
        });

        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();

        Intent intent = getIntent();
        mId = intent.getStringExtra(EntryListActivity.ENTRY_FIELD_ID);
        if(mId != null)
        {
            Entry entry = mDataSource.getEntry(mId);
            mEditNote.setText(entry.getEntryNote());
            mSeekBarMood.setProgress(entry.getOverallMood());
            mEntryTime = entry.getEntryTime();
       }

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
        EntryDTO entryDTO = new EntryDTO();
        entryDTO.entryNote=mEditNote.getText().toString();

        Long time = System.currentTimeMillis()/1000; // be last
        entryDTO.lastEditedTime=time.toString();
        if(mEntryTime == null)
        {
            entryDTO.entryTime = entryDTO.lastEditedTime;
        } else
        {
            entryDTO.entryTime = mEntryTime;
        }
        entryDTO.overallMood=mSeekBarMood.getProgress();
        mDataSource.updateEntry(mId,entryDTO);
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
}
