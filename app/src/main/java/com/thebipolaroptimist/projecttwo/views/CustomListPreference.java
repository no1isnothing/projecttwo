package com.thebipolaroptimist.projecttwo.views;


import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.SettingsFragmentViewModel;
import com.thebipolaroptimist.projecttwo.models.SelectableWordData;

import java.util.Set;

import static com.thebipolaroptimist.projecttwo.SettingsFragment.CATEGORY_KEY;


/**
 * Custom Preference Class
 * A list of action rows representing detail types
 * On creation a row is added for each existing detail type
 * The user can add new rows or remove existing rows
 */
public class CustomListPreference extends DialogFragment {
    private static final String TAG ="CustomListPreference";

    private LinearLayout mLayout;
    private String mCategory;
    private Set<SelectableWordData> mValues;
    private SettingsFragmentViewModel mViewModel;

    public CustomListPreference() {
    }

    public void setData(Set<SelectableWordData> data)
    {
        mValues = data;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.custom_list_preference_dialog, null);
        builder.setView(view);
        Bundle bundle = getArguments();
        if(bundle != null && bundle.containsKey(CATEGORY_KEY)) mCategory = bundle.getString("category");
        mLayout = view.findViewById(R.id.pref_list);
        final ScrollView scroll = view.findViewById(R.id.pref_list_scroll);
        Button addItemButton = view.findViewById(R.id.pref_list_add_button);
        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addRow(null, null);
                //Return scroll to the bottom after adding a new row
                scroll.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        scroll.fullScroll(View.FOCUS_DOWN);
                    }
                }, 200);

            }
        });

        mViewModel = ViewModelProviders.of(this).get(SettingsFragmentViewModel.class);

        if(mValues == null)
        {
            mValues = mViewModel.getDetails(mCategory);
        }

        for(SelectableWordData data : mValues)
        {
            addRow(data.getWord(), data.getColor());
        }

        if(mValues.size() == 0)
        {
            addRow(null, null);
        }

        builder.setTitle(mCategory + " Types ");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getValuesFromUI();
                mViewModel.setDetailsAndStore(mCategory, mValues);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("category", mCategory);
        getValuesFromUI();
        mViewModel.setDetails(mCategory, mValues);
    }

    private void addRow(String title, String color)
    {
        mLayout.addView(new ActionRow(getActivity(), title, color, R.drawable.ic_delete_black, new ActionRow.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.removeView(view);
            }
        }));
    }

    private void getValuesFromUI()
    {
        int count = mLayout.getChildCount();
        mValues.clear();
        for(int i = 0; i < count; i++)
        {
            ActionRow row = (ActionRow) mLayout.getChildAt(i);
            String name = row.getName();
            String color = row.getColor();
            if(!name.isEmpty() && color != null) {
                mValues.add(new SelectableWordData(name, color, true));
            }
        }
    }
}


