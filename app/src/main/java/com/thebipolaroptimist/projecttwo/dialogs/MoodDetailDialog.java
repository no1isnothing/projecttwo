package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;
import com.thebipolaroptimist.projecttwo.views.SelectableSeekBar;

import java.util.ArrayList;
import java.util.List;

public class MoodDetailDialog extends BaseDetailDialog {
    public static final String TAG = "MoodDetailDialog";
    public static final String CATEGORY = "Mood";
    ViewGroup mMoodLayout;
    Bundle mArgs;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mMoodLayout = (ViewGroup) inflater.inflate(R.layout.dialog_mood, null);
        builder.setView(mMoodLayout);
        mCategory = CATEGORY;
        super.onCreateDialog(savedInstanceState);

        mArgs = getArguments();

        for (String detailsType : detailsTypes) {
            if(detailTypesToColors.containsKey(detailsType)) {
                SelectableSeekBar bar = new SelectableSeekBar(getActivity(), null);
                bar.setTitle(detailsType);
                bar.setTitleColor(detailTypesToColors.get(detailsType));

                if (mArgs != null && mArgs.containsKey(detailsType)) {
                    bar.setValue(mArgs.getInt(detailsType));
                }

                if (mMoodLayout != null) {
                    mMoodLayout.addView(bar);
                }
            }
        }

        //Send data back to Activity
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<DetailDTO> moodDetailDTOList = new ArrayList<>();
                for(int i = 0; i < mMoodLayout.getChildCount(); i++) {
                    SelectableSeekBar bar = (SelectableSeekBar) mMoodLayout.getChildAt(i);
                    if(bar.isEnabled()) {
                        DetailDTO moodDetailDTO = new DetailDTO();
                        moodDetailDTO.category = CATEGORY;
                        moodDetailDTO.detailDataUnit = DetailDTO.getUnits(CATEGORY);
                        moodDetailDTO.detailData = Integer.toString(bar.getValue());
                        moodDetailDTO.detailType = bar.getTitle();
                        moodDetailDTO.color = Color.parseColor(bar.getTitleColor());
                        moodDetailDTOList.add(moodDetailDTO);
                    }
                }

                mListener.onDetailDialogPositiveClick(moodDetailDTOList, CATEGORY);
            }
        });

        return builder.create();
    }

}
