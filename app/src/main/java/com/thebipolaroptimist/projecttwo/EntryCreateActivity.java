package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.dialogs.ActivityDialog;
import com.thebipolaroptimist.projecttwo.dialogs.IncidentDialog;
import com.thebipolaroptimist.projecttwo.dialogs.MoodDialog;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;

public class EntryCreateActivity extends AppCompatActivity {
    public static final String TAG = "EntryCreaete";
    private ProjectTwoDataSource mDataSource;
    private EditText mEditNote;
    private Button mButtonSave;
    private String mId; //make sure this field is getting reset
    private AppCompatSpinner mSpinner;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        mEditNote = findViewById(R.id.notes);
        mButtonSave = findViewById(R.id.button_save);
        mSpinner = findViewById(R.id.spinner);
        mSeekBar = findViewById(R.id.moodBar);

        String[] entryTypes = {"", "Mood", "Incident", "Activity"}; //TODO extract this
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, entryTypes);
        mSpinner.setAdapter(adapter);

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i(TAG, "Position Selected " + position);
                switch (position)
                {
                    case 1:
                        DialogFragment dialog = new MoodDialog();
                        dialog.show(getSupportFragmentManager(), "MoodDialog");
                        break;
                    case 2:
                        DialogFragment dialogFragment = new IncidentDialog();
                        dialogFragment.show(getSupportFragmentManager(), "IncidentDialog");
                        break;
                    case 3:
                        DialogFragment aDialog = new ActivityDialog();
                        aDialog.show(getSupportFragmentManager(), "ActivityDialog");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "Nothing selected");
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
            mSeekBar.setProgress(entry.getOverallMood());
       }

        mButtonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mDataSource.close();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        /*Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        String name = mEditNote.getText().toString();
        intent.putExtra(EntryListActivity.ENTRY_FIELD_NAME, name);
        */
        EntryDTO entryDTO = new EntryDTO();
        entryDTO.entryNote = mEditNote.getText().toString();
        Long time = System.currentTimeMillis()/1000;
        entryDTO.entryTime = time.toString();
        entryDTO.overallMood = mSeekBar.getProgress();
        mDataSource.updateEntry(mId, entryDTO);

        super.onBackPressed();
        finish();
    }
}
