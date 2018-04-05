package com.thebipolaroptimist.projecttwo;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.thebipolaroptimist.projecttwo.dialogs.ActivityDialog;
import com.thebipolaroptimist.projecttwo.dialogs.IncidentDialog;
import com.thebipolaroptimist.projecttwo.dialogs.MoodDialog;
import com.thebipolaroptimist.projecttwo.views.CustomListPreference;

public class SettingsFragment extends PreferenceFragment {
    public static final String[] CATEGORIES_ARRAY = {MoodDialog.CATEGORY, ActivityDialog.CATEGORY, IncidentDialog.CATEGORY};
    public static final String PREFERENCE_PREFIX = "preference_";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        //Create a custom list preference for each category and add to screen
        for (String category : CATEGORIES_ARRAY) {
            CustomListPreference preference = new CustomListPreference(preferenceScreen.getContext(), null);
            preference.setTitle(category);
            preference.setKey(PREFERENCE_PREFIX + category);
            preferenceScreen.addPreference(preference);
        }
    }
}