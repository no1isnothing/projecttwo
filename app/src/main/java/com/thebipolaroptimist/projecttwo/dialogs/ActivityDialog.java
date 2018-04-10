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
import android.widget.EditText;
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


public class ActivityDialog extends DialogFragment {
    public static final String TAG = "ActivityDialog";

    public static final String CATEGORY = "Activity";
    Spinner mSpinnerActivityType;
    ActivityDialogListener mListener;
    EditText mEditDuration;
    String mActivityType;
    String mColor;

    public interface ActivityDialogListener
    {
        void onActivityDialogPositiveClick(DetailDTO activityDetailDTO);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try
        {
            mListener = (ActivityDialogListener) context;
        }catch (ClassCastException e)
        {
            Log.i(TAG, "ClassCastException " + e.getMessage());
        }
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.dialog_activity, null);
        builder.setView(view);
        mEditDuration = view.findViewById(R.id.edit_duration);
        mSpinnerActivityType = view.findViewById(R.id.spinner_activity);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final List<String> activitiesFromPrefs = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activitiesFromPrefs.addAll(prefs.getStringSet(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, new HashSet<String>()));
        } else
        {
            String activityString = prefs.getString(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, "");
            activitiesFromPrefs.addAll(Arrays.asList(activityString.split(",")));
        }

        final List<String> activities = new ArrayList<>();
        final Map<String,String> activityToColors = new HashMap<>();
        activities.add(getString(R.string.dialog_activity_spinner_label));
        for (String preference : activitiesFromPrefs) {
            String[] pieces = preference.split(":");
            if(pieces.length >1) {
                activities.add(pieces[0]);
                activityToColors.put(pieces[0], pieces[1]);
            } else
            {
                Log.w(TAG, "Invalid activity type stored");
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_style, activities);
        mSpinnerActivityType.setAdapter(adapter);

        mSpinnerActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mActivityType = activities.get(position);
                mColor = activityToColors.get(mActivityType);
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
                if(!mActivityType.equals(getString(R.string.dialog_activity_spinner_label))) {
                    DetailDTO activityDetailDTO = new DetailDTO();
                    activityDetailDTO.category = CATEGORY;
                    activityDetailDTO.detailDataUnit = DetailDTO.getUnits(CATEGORY);
                    activityDetailDTO.detailType = mActivityType;
                    activityDetailDTO.detailData = mEditDuration.getText().toString();
                    activityDetailDTO.color = Color.parseColor(mColor);
                    mListener.onActivityDialogPositiveClick(activityDetailDTO);
                }
            }
        });
        return builder.create();
    }
}
