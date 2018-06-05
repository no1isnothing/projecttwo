package com.thebipolaroptimist.projecttwo;

import android.arch.lifecycle.ViewModel;

import com.thebipolaroptimist.projecttwo.db.ProjectTwoDataSource;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EntryCreateViewModel extends ViewModel {
    private ProjectTwoDataSource mDataSource;
    private String mId;
    public EntryDTO mEntryDTO;
    private String mDate;

    public EntryCreateViewModel()
    {
        mDataSource = new ProjectTwoDataSource();
        mDataSource.open();
        mEntryDTO = new EntryDTO();
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        mDataSource.close();
    }

    public void setId(String id)
    {
        if(id != null) {
            mId = id;
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

    public void updateEntry() {
        mDataSource.updateEntry(mId, mEntryDTO);
    }

    public void deleteEntry() {
        if(mId != null) {
            mDataSource.deleteEntry(mId);
        }
    }
}
