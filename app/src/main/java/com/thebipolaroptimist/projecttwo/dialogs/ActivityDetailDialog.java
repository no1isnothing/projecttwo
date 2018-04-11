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
import android.widget.EditText;
import android.widget.Spinner;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;


public class ActivityDetailDialog extends BaseDetailDialog {
    public static final String TAG = "ActivityDetailDialog";

    public static final String CATEGORY = "Activity";
    Spinner mSpinnerActivityType;
    EditText mEditDuration;
    String mActivityType;
    String mColor;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final ViewGroup view = (ViewGroup) inflater.inflate(R.layout.dialog_activity, null);
        builder.setView(view);
        mCategory = CATEGORY; //This must be set up before super is called
        super.onCreateDialog(savedInstanceState);

        mEditDuration = view.findViewById(R.id.edit_duration);
        mSpinnerActivityType = view.findViewById(R.id.spinner_activity);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.custom_spinner_style, detailsTypes);
        mSpinnerActivityType.setAdapter(adapter);

        mSpinnerActivityType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mActivityType = detailsTypes.get(position);
                mColor = detailTypesToColors.get(mActivityType);
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
                    mListener.onDetailDialogPositiveClick(activityDetailDTO, CATEGORY);
                }
            }
        });
        return builder.create();
    }
}
