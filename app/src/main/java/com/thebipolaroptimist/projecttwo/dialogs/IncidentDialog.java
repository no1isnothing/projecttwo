package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.SettingsFragment;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class IncidentDialog extends DialogFragment {
    public static final String TAG = "IncidentDialog";

    public static final String CATEGORY = "Incident";
    Spinner mSpinnerIncidentType;
    String mColor;
    IncidentDialogListener mListener;
    String mIncidentType;

    public interface IncidentDialogListener
    {
        void onIncidentDialogPositiveClick(DetailDTO incidentDetailDTO);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            mListener = (IncidentDialogListener) context;
        } catch (ClassCastException e)
        {
            Log.i(TAG, e.getMessage());
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.dialog_incident, null);
        builder.setView(view);
        mSpinnerIncidentType = view.findViewById(R.id.spinner_incident);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final List<String> incidentsFromPrefs = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incidentsFromPrefs.addAll(prefs.getStringSet(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, new HashSet<String>()));
        } else
        {
            String activityString = prefs.getString(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, "");
            incidentsFromPrefs.addAll(Arrays.asList(activityString.split(",")));
        }

        final Map<String, String> incidentToColors = new HashMap<>();
        final List<String> incidents = new ArrayList<>();
        incidents.add(getString(R.string.dialog_incident_spinner_label));
        for (String incident : incidentsFromPrefs) {
            String[] pieces = incident.split(":");
            if(pieces.length > 1) {
                incidents.add(pieces[0]);
                incidentToColors.put(pieces[0], pieces[1]);
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_style, incidents);
        mSpinnerIncidentType.setAdapter(adapter);

        mSpinnerIncidentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIncidentType = incidents.get(position);
                mColor = incidentToColors.get(mIncidentType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i(TAG, "Nothing selected");
            }
        });

        builder.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //get data
                if(!mIncidentType.equals(getString(R.string.dialog_incident_spinner_label))) {
                    DetailDTO activityDetailDTO = new DetailDTO();
                    activityDetailDTO.category = CATEGORY;
                    activityDetailDTO.detailDataUnit = DetailDTO.getUnits(CATEGORY);
                    activityDetailDTO.detailType = mIncidentType;
                    activityDetailDTO.color = Color.parseColor(mColor);
                    mListener.onIncidentDialogPositiveClick(activityDetailDTO);
                }
            }
        });

        return builder.create();
    }
}
