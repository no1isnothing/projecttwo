package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;
import com.thebipolaroptimist.projecttwo.views.SelectableSeekBar;

import java.util.ArrayList;
import java.util.List;

public class MoodDialog extends DialogFragment {
    public static final String TAG = "MoodDialog";
    MoodDialogListener mListener;

    public interface MoodDialogListener
    {
        void onMoodDialogPositiveClick(List<DetailDTO> moodDataDTOList);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mListener = (MoodDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "ClassCastException " + e.getMessage());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.dialog_mood, null);
        builder.setView(layout);

        //Dynamically add seek bars and fill in with data
        //if it's available
        String[] moods = {"Manic", "Depressed"}; //TODO Move to settings
        Bundle args = getArguments();
        for (String mood : moods) {
            SelectableSeekBar bar = new SelectableSeekBar(getActivity(), null);
            bar.setTitle(mood);
            if(args != null && args.containsKey(mood))
            {
                bar.setValue(args.getInt(mood));
            }
            layout.addView(bar);
        }

        //Send data back to Activity
        builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<DetailDTO> moodDetailDTOList = new ArrayList<>();
                for(int i = 0; i < layout.getChildCount(); i++) {
                    SelectableSeekBar bar = (SelectableSeekBar) layout.getChildAt(i);
                    if(bar.isEnabled()) {
                        DetailDTO moodDetailDTO = new DetailDTO();
                        moodDetailDTO.category = "Mood"; //TODO match to string in activity
                        moodDetailDTO.detailData = Integer.toString(bar.getValue());
                        moodDetailDTO.detailType = bar.getTitle();
                        moodDetailDTOList.add(moodDetailDTO);
                    }
                }

                mListener.onMoodDialogPositiveClick(moodDetailDTOList);
            }
        });

        return builder.create();
    }

}
