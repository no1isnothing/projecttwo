package com.thebipolaroptimist.projecttwo.db;

import android.util.Log;

import com.thebipolaroptimist.projecttwo.models.Entry;

import java.util.List;

import io.realm.Realm;

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

    public void updateEntry(final String id, final String name)
    {
        realm.executeTransaction(new Realm.Transaction()
        {

            @Override
            public void execute(Realm realm) {
                Entry entry = realm.where(Entry.class).equalTo("id", id).findFirst();
                entry.setEntryName(name);
            }
        });
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
