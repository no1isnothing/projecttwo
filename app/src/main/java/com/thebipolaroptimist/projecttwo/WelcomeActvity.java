package com.thebipolaroptimist.projecttwo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.thebipolaroptimist.projecttwo.views.ActivityDetailRow;
import com.thebipolaroptimist.projecttwo.views.IncidentDetailRow;
import com.thebipolaroptimist.projecttwo.views.MoodDetailRow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WelcomeActvity extends AppCompatActivity {
    private static final String TAG = "WelcomeActivity";
    SettingsManager mSettingsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_actvity);

        mSettingsManager = new SettingsManager(this);

        Intent intent = new Intent(getApplicationContext(), EntryCalendarActivity.class);
        if(mSettingsManager.isFirstLaunch())
        {
            Log.i(TAG, "First launch");
            intent.putExtra(SettingsManager.FIRST_LAUNCH, true);
            //TODO make this static somewhere? is it really worth it?
            List<String> moodDetailTypes = new ArrayList<>();
            moodDetailTypes.add("depressed:#530052FF");
            moodDetailTypes.add("manic:#FFFF7A00");
            moodDetailTypes.add("anxiety:#61FFFF00");
            mSettingsManager.storeDetailTypeForCategory(MoodDetailRow.CATEGORY, moodDetailTypes);
            List<String> activityDetailTypes = new ArrayList<>();
            activityDetailTypes.add("running:#FF00CD1B");
            activityDetailTypes.add("yoga:#7B7C007F");
            activityDetailTypes.add("boxing:#FF980000");
            List<String> incidentDetailTypes = new ArrayList<>();
            incidentDetailTypes.add("panic attack:#FFFFFF00");
            mSettingsManager.storeDetailTypeForCategory(IncidentDetailRow.CATEGORY, incidentDetailTypes);
            mSettingsManager.storeDetailTypeForCategory(ActivityDetailRow.CATEGORY, activityDetailTypes);

            mSettingsManager.setLaunched();
        }

        startActivity(intent);
    }
}
