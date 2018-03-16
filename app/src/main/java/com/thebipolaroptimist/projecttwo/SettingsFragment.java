package com.thebipolaroptimist.projecttwo;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;

import com.thebipolaroptimist.projecttwo.dialogs.ActivityDialog;
import com.thebipolaroptimist.projecttwo.dialogs.MoodDialog;
import com.thebipolaroptimist.projecttwo.views.CustomListPreference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SettingsFragment extends PreferenceFragment {
    public static final String[] CATEGORIES_ARRAY = {MoodDialog.CATEGORY, ActivityDialog.CATEGORY};
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        SharedPreferences prefs = getActivity().getPreferences(Context.MODE_PRIVATE);
        Set<String> set = prefs.getStringSet("preference_" + MoodDialog.CATEGORY, new HashSet<String>());

        PreferenceScreen preferenceScreen = getPreferenceScreen();


        for (String s : CATEGORIES_ARRAY) {
            CustomListPreference preference = new CustomListPreference(preferenceScreen.getContext(), null);
            preference.setTitle(s);
            preference.setKey("preference_" + s);

            /*String[] array = list.toArray(new String[0]);
            CharSequence[] entries = array;
            CharSequence[] entryValues = array;
            preference.setEntryValues(entryValues);
            preference.setEntries(entries);*/
            preferenceScreen.addPreference(preference);
        }

        int prefCOunt = preferenceScreen.getPreferenceCount();
        for(int i = 0; i < prefCOunt; i++)
        {
            preferenceScreen.getPreference(i);
        }
    }
}