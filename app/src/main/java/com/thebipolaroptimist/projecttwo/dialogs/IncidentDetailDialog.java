package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class IncidentDetailDialog extends BaseDetailDialog {
    public static final String TAG = "IncidentDetailDialog";

    public static final String CATEGORY = "Incident";
    Spinner mSpinnerIncidentType;
    String mColor;
    String mIncidentType;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.dialog_incident, null);
        builder.setView(view);
        mCategory = CATEGORY;
        super.onCreateDialog(savedInstanceState);

        mSpinnerIncidentType = view.findViewById(R.id.spinner_incident);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_style, detailsTypes);
        mSpinnerIncidentType.setAdapter(adapter);

        mSpinnerIncidentType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIncidentType = detailsTypes.get(position);
                mColor = detailTypesToColors.get(mIncidentType);
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
                    mListener.onDetailDialogPositiveClick(activityDetailDTO, CATEGORY);
                }
            }
        });

        return builder.create();
    }
}
