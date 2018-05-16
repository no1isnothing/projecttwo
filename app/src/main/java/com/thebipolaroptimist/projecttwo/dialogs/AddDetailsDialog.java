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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.SettingsFragment;
import com.thebipolaroptimist.projecttwo.views.ActionRow;
import com.thebipolaroptimist.projecttwo.views.SelectableWord;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AddDetailsDialog extends DialogFragment {

    private static final String TAG = "AddDetailDialog";
    private Listener mListener;
    private LinearLayout mDetailTypeList;
    private Button mButtonAddDetailType;
    final private List<String> mDetailTypeFromPrefs = new ArrayList<>();
    private String mCategory = "";

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
                    try {
                        SelectableWord word = (SelectableWord) mDetailTypeList.getChildAt(i);
                        if (word.isChecked()) {
                            list.add(word.getWord() + ":" + word.getColor());
                        }
                    } catch(ClassCastException e)
                    {
                        Log.i(TAG, "Details saved with empty action row");
                    }
                }
                mListener.onPositiveResult(list);
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });
        mDetailTypeList = view.findViewById(R.id.detail_type_list);
        mButtonAddDetailType = view.findViewById(R.id.add_detail_type_button);

        final Bundle args = getArguments();
        if (args != null && args.containsKey("category")) {
            mCategory = args.getString("category");
        } else
        {
            Log.w(TAG, "Created without category argument");
        }

        getDetailTypesFromPrefs();

        //Put detail types into usable data structures
        List<String> detailsTypes = new ArrayList<>();
        Map<String, String> detailTypesToColors = new HashMap<>();
        for (String preference : mDetailTypeFromPrefs) {
            String[] pieces = preference.split(":");
            if(pieces.length >1) {
                detailsTypes.add(pieces[0]);
                detailTypesToColors.put(pieces[0], pieces[1]);
            } else
            {
                Log.w(TAG + mCategory, "Invalid activity type stored");
            }
        }

        //Add lines to view for each detail type
        for (String detailsType : detailsTypes) {
            if(args != null && !args.containsKey(detailsType)) {
                mDetailTypeList.addView(new SelectableWord(getActivity(), detailsType, detailTypesToColors.get(detailsType)));
            }
        }

        final ActionRow actionRow = new ActionRow(getActivity(), null, null,  R.drawable.ic_add_black, new ActionRow.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add values from action row to view
                ActionRow row = (ActionRow) view;
                mDetailTypeFromPrefs.add(row.getName() + ":" + row.getColor());
                storeDetailTypesInPrefs();
                mDetailTypeList.addView(new SelectableWord(getActivity(), row.getName(), row.getColor()));
                view.setVisibility(View.INVISIBLE);
                mButtonAddDetailType.setVisibility(View.VISIBLE);
                row.clearName();
            }
        });

        actionRow.setVisibility(View.INVISIBLE);
        LinearLayout linearLayout = view.findViewById(R.id.add_detail_layout2);
        linearLayout.addView(actionRow);

        mButtonAddDetailType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionRow.setVisibility(View.VISIBLE);
                v.setVisibility(View.INVISIBLE);
            }
        });

        return builder.create();
    }

    private void storeDetailTypesInPrefs()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefEditor = prefs.edit();

        Set<String> set = new HashSet(mDetailTypeFromPrefs);
        prefEditor.putStringSet(SettingsFragment.PREFERENCE_PREFIX + mCategory, set);

        prefEditor.commit();
    }

    private void getDetailTypesFromPrefs()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mDetailTypeFromPrefs.addAll(prefs.getStringSet(SettingsFragment.PREFERENCE_PREFIX + mCategory, new HashSet<String>()));
        } else
        {
            String activityString = prefs.getString(SettingsFragment.PREFERENCE_PREFIX + mCategory, "");
            mDetailTypeFromPrefs.addAll(Arrays.asList(activityString.split(",")));
        }
    }
}
