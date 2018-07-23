package com.thebipolaroptimist.projecttwo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import com.thebipolaroptimist.projecttwo.views.ActivityDetailRow;
import com.thebipolaroptimist.projecttwo.views.IncidentDetailRow;
import com.thebipolaroptimist.projecttwo.views.MoodDetailRow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SettingsManager {
    final private SharedPreferences mRreferences;
    private static final String FIRST_LAUNCH = "first_launch";

    public SettingsManager(Context context)
    {
        mRreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public boolean isFirstLaunch()
    {
        return mRreferences.getBoolean(FIRST_LAUNCH, true);
    }

    public void setFirstLaunch(boolean launched)
    {
        SharedPreferences.Editor prefEditor = mRreferences.edit();
        prefEditor.putBoolean(FIRST_LAUNCH, launched);
        prefEditor.apply();
    }

    public void setupDefaultDetails()
    {
        //Don't set up defaults if there's already settings
        for(String category : SettingsFragment.CATEGORIES_ARRAY) {
            if(getDetailTypesForCategory(category).size() > 0)
            {
                return;
            }
        }

        List<String> moodDetailTypes = new ArrayList<>();
        moodDetailTypes.add("depressed:#530052FF");
        moodDetailTypes.add("manic:#FFFF7A00");
        moodDetailTypes.add("anxiety:#61FFFF00");
        storeDetailTypeForCategory(MoodDetailRow.CATEGORY, moodDetailTypes);
        List<String> activityDetailTypes = new ArrayList<>();
        activityDetailTypes.add("running:#FF00CD1B");
        activityDetailTypes.add("yoga:#7B7C007F");
        activityDetailTypes.add("boxing:#FF980000");
        storeDetailTypeForCategory(ActivityDetailRow.CATEGORY, activityDetailTypes);
        List<String> incidentDetailTypes = new ArrayList<>();
        incidentDetailTypes.add("panic attack:#FFFFFF00");
        storeDetailTypeForCategory(IncidentDetailRow.CATEGORY, incidentDetailTypes);
    }

    public Collection<String> getDetailTypesForCategory(String category)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return mRreferences.getStringSet(SettingsFragment.PREFERENCE_PREFIX + category, new HashSet<String>());
        } else
        {
            String activityString = mRreferences.getString(SettingsFragment.PREFERENCE_PREFIX + category, "");
            return Arrays.asList(activityString.split(","));
        }
    }

    public void storeDetailTypeForCategory(String category, Collection<String> detailTypes)
    {
        SharedPreferences.Editor prefEditor = mRreferences.edit();
        Set<String> detailTypesSet = new HashSet<>(detailTypes);
        prefEditor.putStringSet(SettingsFragment.PREFERENCE_PREFIX + category, detailTypesSet);

        prefEditor.apply();
    }
}
