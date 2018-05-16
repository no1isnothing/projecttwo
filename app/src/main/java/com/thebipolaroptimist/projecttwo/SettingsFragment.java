package com.thebipolaroptimist.projecttwo;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.thebipolaroptimist.projecttwo.views.ActivityDetailRow;
import com.thebipolaroptimist.projecttwo.views.CustomListPreference;
import com.thebipolaroptimist.projecttwo.views.IncidentDetailRow;
import com.thebipolaroptimist.projecttwo.views.MoodDetailRow;

public class SettingsFragment extends PreferenceFragment {
    public static final String[] CATEGORIES_ARRAY = {MoodDetailRow.CATEGORY, ActivityDetailRow.CATEGORY, IncidentDetailRow.CATEGORY};
    public static final String PREFERENCE_PREFIX = "preference_";

    public static String getLabel(String category)
    {
        if(category.equals(MoodDetailRow.CATEGORY))
        {
            return "Intensity";
        } else if(category.equals(ActivityDetailRow.CATEGORY))
        {
            return "Duration";
        }
        return "";
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        PreferenceScreen preferenceScreen = getPreferenceScreen();

        //Create a custom list preference for each category and add to screen
        for (String category : CATEGORIES_ARRAY) {
            CustomListPreference preference = new CustomListPreference(getActivity(), null);
            preference.setTitle(category + " Types");
            preference.setKey(PREFERENCE_PREFIX + category);
            preferenceScreen.addPreference(preference);
        }
    }
}