package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.SettingsFragment;
import com.thebipolaroptimist.projecttwo.views.SelectableWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class AddDetailsDialog extends DialogFragment {

    public static final String TAG = "AddDetailDialog";
    private Listener mListener;
    private LinearLayout mDetailTypeList;

    public interface Listener{
         void onPositiveResult(List<String> detailTypes);
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.diloag_add_details, null);
        builder.setView(view);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                List<String> list = new ArrayList<>();
                int count = mDetailTypeList.getChildCount();
                for(int i = 0; i < count; i++)
                {
                    SelectableWord word = (SelectableWord) mDetailTypeList.getChildAt(i);
                    if(word.isChecked())
                    {
                        list.add(word.getWord() + ":" + word.getColor());
                    }
                }
                mListener.onPositiveResult(list);
            }
        });

        mDetailTypeList = view.findViewById(R.id.detail_type_list);

        Bundle args = getArguments();

        String mCategory = "";
        if (args != null && args.containsKey("category")) {
            mCategory = args.getString("category");
        } else
        {
            Log.w(TAG, "Created without category argument");
        }


        //TODO Extract this?
        //Get detail types from preferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        final List<String> activitiesFromPrefs = new ArrayList<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            activitiesFromPrefs.addAll(prefs.getStringSet(SettingsFragment.PREFERENCE_PREFIX + mCategory, new HashSet<String>()));
        } else
        {
            String activityString = prefs.getString(SettingsFragment.PREFERENCE_PREFIX + mCategory, "");
            activitiesFromPrefs.addAll(Arrays.asList(activityString.split(",")));
        }

        //Put detail types into useable data structures
        List<String> detailsTypes = new ArrayList<>();
        Map<String, String> detailTypesToColors = new HashMap<>();
        for (String preference : activitiesFromPrefs) {
            String[] pieces = preference.split(":");
            if(pieces.length >1) {
                detailsTypes.add(pieces[0]);
                detailTypesToColors.put(pieces[0], pieces[1]);
            } else
            {
                Log.w(TAG + mCategory, "Invalid activity type stored");
            }
        }
        //

        //Add lines to view for each detail type
        for (String detailsType : detailsTypes) {
            if(!args.containsKey(detailsType)) {
                mDetailTypeList.addView(new SelectableWord(getActivity(), detailsType, detailTypesToColors.get(detailsType)));
            }
        }

        return builder.create();
    }
}
