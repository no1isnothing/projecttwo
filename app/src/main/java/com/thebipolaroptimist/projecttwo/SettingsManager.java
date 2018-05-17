package com.thebipolaroptimist.projecttwo;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SettingsManager {
    final private SharedPreferences preferences;
    public static final String FIRST_LAUNCH = "first_launch";

    public SettingsManager(Activity context)
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    //TODO does first launch need a value?
    public boolean isFirstLaunch()
    {
        return !preferences.contains(FIRST_LAUNCH);
    }

    public void setLaunched()
    {
        //TODO make class variable?
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(FIRST_LAUNCH, false);
        prefEditor.apply();
    }

    public Collection<String> getDetailTypesForCategory(String category)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return preferences.getStringSet(SettingsFragment.PREFERENCE_PREFIX + category, new HashSet<String>());
        } else
        {
            String activityString = preferences.getString(SettingsFragment.PREFERENCE_PREFIX + category, "");
            return Arrays.asList(activityString.split(","));
        }
    }

    public void storeDetailTypeForCategory(String category, Collection<String> detailTypes)
    {
        for(String s : detailTypes)
        {
            Log.i("DETAIL", s);
        }
        SharedPreferences.Editor prefEditor = preferences.edit();
        Set<String> detailTypesSet = new HashSet<>(detailTypes);
        prefEditor.putStringSet(SettingsFragment.PREFERENCE_PREFIX + category, detailTypesSet);

        prefEditor.apply();
    }
}
