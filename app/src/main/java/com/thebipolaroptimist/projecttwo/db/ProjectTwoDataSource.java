package com.thebipolaroptimist.projecttwo.db;

import android.util.Log;

import com.thebipolaroptimist.projecttwo.EntryCalendarActivity;
import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmObject;

public class ProjectTwoDataSource {
    private static final String TAG = "ProjectTwoDataSource";
    private Realm realm;

    public void open()
    {
        realm = Realm.getDefaultInstance();

        Log.d( TAG, "open: database opened" );
    }

    public void close()
    {
        realm.close();
        Log.d( TAG, "close: database closed" );
    }

    public void createEntry(final Entry entry)
    {
        realm.executeTransaction(new Realm.Transaction()
        {
            @Override
            public void execute(Realm realm)
            {
                realm.insertOrUpdate(entry);
            }
        });

        Log.d(TAG, "createEntry: the id: " + entry.getId());
    }

    public void updateEntry(final String id, final EntryDTO entryDTO)
    {
        realm.executeTransaction(new Realm.Transaction()
        {

            @Override
            public void execute(Realm realm) {
                Entry entry = realm.where(Entry.class).equalTo("id", id).findFirst();
                if(entry == null)
                {
                    entry = new Entry();
                }
                EntryDTO.EntryDTOToEntry(entryDTO, entry);
                realm.insertOrUpdate(entry);
            }
        });
    }

    public List<Entry> getEntriesForDay(String day)
    {
        List<Entry> entries = getAllEntries();
        List<Entry> entriesForDay = new ArrayList<>();

        for(Entry entry: entries)
        {
            String thisDay = getFormattedTime(entry.getEntryTime());

            if(thisDay.equals(day))
            {
                entriesForDay.add(entry);
            }
        }
        return entriesForDay;
    }

    public Map<String, Object> getEntriesByDay()
    {
        List<Entry> entries = getAllEntries();
        Map<String,Object>  dayToEntries = new HashMap<>();
        for (Entry entry : entries) {
            String key = getFormattedTime(entry.getEntryTime());
            List<Entry> entriesForToday  = null;

            try {

                entriesForToday = (List<Entry>) dayToEntries.get(key);
            } catch(ClassCastException e)
            {
                Log.w(TAG, "Failed to cast List<Entry> to List<Object>");
            }
            if(entriesForToday == null)
            {
                entriesForToday= new ArrayList<>();
                dayToEntries.put(key, entriesForToday);
            }
            entriesForToday.add(entry);
        }
        return dayToEntries;
    }

    public List<Entry > getAllEntries()
    {
        return realm.where(Entry.class).findAll();
    }

    public Entry getEntry(String entryId)
    {
        return realm.where(Entry.class).equalTo("id", entryId).findFirst();
    }

    public void deleteEntry(final String entryId)
    {
        final RealmObject entry = realm.where(Entry.class).equalTo("id", entryId).findFirst();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                entry.deleteFromRealm();
            }
        });
    }
    /**
     * Change time from millisecond to MM DD YYYY representation
     * @param time string representing in milliseconds
     * @return a string formatted MM DD YYYY
     */
    private String getFormattedTime(String time)
    {
        final SimpleDateFormat formatter = new SimpleDateFormat(EntryCalendarActivity.DATE_FORMAT_PATTERN, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(Long.parseLong(time));
        return formatter.format(calendar.getTime());
    }
}
