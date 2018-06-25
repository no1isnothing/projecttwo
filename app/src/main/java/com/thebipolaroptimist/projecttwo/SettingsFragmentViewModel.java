package com.thebipolaroptimist.projecttwo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;

import com.thebipolaroptimist.projecttwo.models.SelectableWordData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class SettingsFragmentViewModel extends AndroidViewModel {
    private Map<String, Set<SelectableWordData>> mCategoryToDetailType;
    private SettingsManager mSettingsManager;

    public SettingsFragmentViewModel(Application context)
    {
        super(context);
        mSettingsManager = new SettingsManager(context);
        mCategoryToDetailType = new HashMap<>();
        setupDetails();
    }

    private void setupDetails()
    {
        for(String category : SettingsFragment.CATEGORIES_ARRAY)
        {
            Set<SelectableWordData> data = new TreeSet<>();
            for (final String s : mSettingsManager.getDetailTypesForCategory(category)) {
                String[] parts = s.split(":");
                if(parts.length > 1) {
                    data.add(new SelectableWordData(parts[0], parts[1], true));
                }
            }
            mCategoryToDetailType.put(category, data);
        }
    }

    public Set<SelectableWordData> getDetails(String category)
    {
        return mCategoryToDetailType.get(category);
    }

    public List<String> setDetails(String category, Set<SelectableWordData> data)
    {
        mCategoryToDetailType.put(category, data);
        List<String> details = new ArrayList<>();
        for(SelectableWordData word : data)
        {
            details.add(word.getWord() + ":" + word.getColor());
        }
        return details;
    }
    public void setDetailsAndStore(String category, Set<SelectableWordData> data)
    {
        List<String> details = setDetails(category, data);
        mSettingsManager.storeDetailTypeForCategory(category, details);
    }
}
