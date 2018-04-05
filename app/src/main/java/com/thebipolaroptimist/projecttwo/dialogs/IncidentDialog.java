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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.SettingsFragment;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

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
        final List<String> incidents = new ArrayList<>();
        incidents.add("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            incidents.addAll(prefs.getStringSet(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, new HashSet<String>()));
        } else
        {
            String activityString = prefs.getString(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, "");
            incidents.addAll(Arrays.asList(activityString.split(",")));
        }

        final List<String> colors = new ArrayList<>(incidents.size());
        for (int i = 1; i < incidents.size(); i++) {
            String[] pieces = incidents.get(i).split(":");
            incidents.set(i, pieces[0]);
            colors.add( pieces[1]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, incidents);
        mSpinnerIncidentType.setAdapter(adapter);

        mSpinnerIncidentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIncidentType = incidents.get(position);
                if(position != 0) {
                    mColor = colors.get(position - 1);
                }
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
                if(!mIncidentType.isEmpty()) {
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
