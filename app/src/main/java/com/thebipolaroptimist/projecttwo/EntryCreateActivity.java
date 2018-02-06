package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class EntryCreateActivity extends AppCompatActivity {

    private EditText mEditName;
    private Button mButton;
    private String mId; //make sure this field is getting reset

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_create);

        mEditName = findViewById(R.id.edit_name);
        mButton = findViewById(R.id.button_save);

        Intent intent = getIntent();
        mId = intent.getStringExtra(EntryListActivity.ENTRY_FIELD_ID);
        if(mId != null)
        {
            mEditName.setText(intent.getStringExtra(EntryListActivity.ENTRY_FIELD_NAME));
       }

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        String name = mEditName.getText().toString();
        intent.putExtra(EntryListActivity.ENTRY_FIELD_NAME, name);

        if(mId != null)
        {
            intent.putExtra(EntryListActivity.ENTRY_FIELD_ID, mId);
        }

        super.onBackPressed();
        finish();
    }
}
