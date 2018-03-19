package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.widget.EditText;
import android.widget.Spinner;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.SettingsFragment;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ActivityDialog extends DialogFragment {
    public static final String TAG = "ActivityDialog";

    public static final String CATEGORY = "Activity";
    Spinner mSpinnerActivityType;
    ActivityDialogListener mListener;
    EditText mEditDuration;
    String mActivityType;

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
        mEditDuration = view.findViewById(R.id.mEditDuration);
        mSpinnerActivityType = view.findViewById(R.id.mSpinnerActivity);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        final List<String> activities = new ArrayList<>();
        activities.add("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activities.addAll(prefs.getStringSet(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, new HashSet<String>()));
        } else
        {
            String activityString = prefs.getString(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, "");
            activities.addAll(Arrays.asList(activityString.split(",")));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, activities);
        mSpinnerActivityType.setAdapter(adapter);

        mSpinnerActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mActivityType = activities.get(position);
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
                if(!mActivityType.isEmpty()) {
                    DetailDTO activityDetailDTO = new DetailDTO();
                    activityDetailDTO.category = CATEGORY;
                    activityDetailDTO.detailDataUnit = DetailDTO.getUnits(CATEGORY);
                    activityDetailDTO.detailType = mActivityType;
                    activityDetailDTO.detailData = mEditDuration.getText().toString();
                    mListener.onActivityDialogPositiveClick(activityDetailDTO);
                }
            }
        });
        return builder.create();
    }
}
