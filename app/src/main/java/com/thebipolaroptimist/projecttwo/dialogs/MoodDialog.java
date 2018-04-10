package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.SettingsActivity;
import com.thebipolaroptimist.projecttwo.SettingsFragment;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;
import com.thebipolaroptimist.projecttwo.views.SelectableSeekBar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MoodDialog extends DialogFragment {
    public static final String TAG = "MoodDialog";
    public static final String CATEGORY = "Mood";
    MoodDialogListener mListener;
    ViewGroup mMoodLayout;
    Bundle mArgs;

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
    public void onResume() {
        super.onResume();
        mMoodLayout.removeAllViews();
        loadDataFromPrefs();
    }

    private void loadDataFromPrefs()
    {
        //Dynamically add seek bars and fill in with data if it's available
        Set<String> moods;
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            moods = prefs.getStringSet(SettingsFragment.PREFERENCE_PREFIX + CATEGORY, new HashSet<String>());
        } else
        {
            String moodString = prefs.getString(SettingsFragment.PREFERENCE_PREFIX+CATEGORY,"");
            if(!moodString.isEmpty()){
                moods=new HashSet<>(Arrays.asList(moodString.split(",")));
            }else
            {
                moods=new HashSet<>();
            }
        }

        for (String mood : moods) {
            String[] pieces = mood.split(":");
            if(pieces.length >1)
            {
                SelectableSeekBar bar = new SelectableSeekBar(getActivity(), null);
                bar.setTitle(pieces[0]);
                bar.setTitleColor(pieces[1]);

                if (mArgs != null && mArgs.containsKey(pieces[0])) {
                    bar.setValue(mArgs.getInt(pieces[0]));
                }

                if(mMoodLayout != null) {
                    mMoodLayout.addView(bar);
                }
            }
        }

        if(mMoodLayout.getChildCount() == 0)
        {
            TextView textView = new TextView(getActivity());
            textView.setText("No moods available. Please Enter moods in settings.");
            Button button = new Button(getActivity());
            button.setText(getString(R.string.action_settings));
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), SettingsActivity.class);
                    startActivity(intent);
                }
            });
            mMoodLayout.addView(textView);
            mMoodLayout.addView(button);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        mMoodLayout = (ViewGroup) inflater.inflate(R.layout.dialog_mood, null);
        builder.setView(mMoodLayout);

        mArgs = getArguments();

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

                mListener.onMoodDialogPositiveClick(moodDetailDTOList);
            }
        });

        return builder.create();
    }

}
