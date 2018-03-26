package com.thebipolaroptimist.projecttwo.db;

import android.util.Log;

import com.thebipolaroptimist.projecttwo.models.Entry;
import com.thebipolaroptimist.projecttwo.models.EntryDTO;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmList;

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

    public Map<String, Object> getEntriesByDay()
    {
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        List<Entry> entries = realm.where(Entry.class).findAll();
        Map<String,Object>  dayToEntries = new HashMap<>();
        for (Entry entry : entries) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(Long.parseLong(entry.getEntryTime()));
            String key = formatter.format(calendar.getTime());
            List<Entry> entriesForToday = (List<Entry>) dayToEntries.get(key);
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
}
