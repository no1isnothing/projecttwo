package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.util.Log;

import com.thebipolaroptimist.projecttwo.SettingsManager;
import com.thebipolaroptimist.projecttwo.models.SelectableWordData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddDetailsViewModel extends AndroidViewModel {
    private static final String TAG = "AddDetailsViewModel";
    final private List<String> mDetailTypeFromPrefs = new ArrayList<>();
    private SettingsManager mSettingsManager;
    public String mCategory = "";
    private Map<String, SelectableWordData> selectableWordDataMap = new HashMap<>();
    public SelectableWordData addedSelectableWordData;

    public AddDetailsViewModel(Application context)
    {
        super(context);
        mSettingsManager = new SettingsManager(context);
    }

    public void setupDetailTypes()
    {
        if(mDetailTypeFromPrefs.size() == 0) {
            mDetailTypeFromPrefs.addAll(mSettingsManager.getDetailTypesForCategory(mCategory));
            for (String preference : mDetailTypeFromPrefs) {
                String[] pieces = preference.split(":");
                if (pieces.length > 1) {
                    selectableWordDataMap.put(pieces[0], new SelectableWordData(pieces[0], pieces[1], false));
                } else {
                    Log.w(TAG + mCategory, "Invalid activity type stored");
                }
            }
        }
    }

    public Map<String, SelectableWordData> getSelectableWordData()
    {
        return selectableWordDataMap;
    }

    public void addNewDetailType(String name, String color)
    {
        //update view model
        selectableWordDataMap.put(name, new SelectableWordData(name, color, true));

        //update preferences
        mDetailTypeFromPrefs.add(name + ":" + color);
        mSettingsManager.storeDetailTypeForCategory(mCategory, mDetailTypeFromPrefs);
    }

    public List<SelectableWordData> getSelected()
    {
        ArrayList<SelectableWordData> checkedData = new ArrayList<>();
        for(SelectableWordData data : selectableWordDataMap.values())
        {
            if(data.isChecked())
            {
                checkedData.add(data);
            }
        }
        return  checkedData;
    }

    public void updateChecked(String type, boolean value)
    {
        SelectableWordData data = selectableWordDataMap.get(type);
        data.setChecked(value);
    }

}
