package com.thebipolaroptimist.projecttwo;

import android.arch.lifecycle.ViewModel;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EntryCreateViewModel extends ViewModel {
    private ProjectTwoDataSource mDataSource;
    private String mId;
    public EntryDTO mEntryDTO;
    private String mDate;
    private Map<String, Boolean> mCategoryExpandedMap;

    public EntryCreateViewModel()
    {
        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();
        mEntryDTO = new EntryDTO();
        mCategoryExpandedMap = new HashMap<>();
    }

    public boolean isCategoryExpanded(String category)
    {
        if(mCategoryExpandedMap.get(category) != null)
        {
            return mCategoryExpandedMap.get(category);
        }
        return false;
    }

    public void storeCategoryExpanded(String category, boolean expanded)
    {
        mCategoryExpandedMap.put(category, expanded);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDataSource.close();
    }

    public void setupEntry(String entryId)
    {
        if(entryId != null) {
            mId = entryId;
            Entry entry = mDataSource.getEntry(mId);
            EntryDTO.EntryToEntryDTO(entry, mEntryDTO);
        }
    }

    public void setDate(String date)
    {
        if(date != null)
        {
            mDate = date;
        }
    }

    public String getActivityTitle(CharSequence title)
    {
        SimpleDateFormat format = new SimpleDateFormat(EntryCalendarActivity.DATE_FORMAT_PATTERN, Locale.getDefault());

        if (mId != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(mEntryDTO.entryTime));
            return title + format.format(calendar.getTime());
        } else if (mDate != null) {
            //fill in date with passed in date
            return  title + " " + mDate;
        } else {
            //fill in date with current date
            return  title + format.format(System.currentTimeMillis());
        }
    }

    public void updateEntryTime() {
        mEntryDTO.updateTimes(mDate);
    }

    public void updateEntryInDB() {
        mDataSource.updateEntry(mId, mEntryDTO);
    }

    public void deleteEntryFromDB() {
        if(mId != null) {
            mDataSource.deleteEntry(mId);
        }
    }
}
