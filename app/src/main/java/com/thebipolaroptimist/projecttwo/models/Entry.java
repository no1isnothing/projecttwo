package com.thebipolaroptimist.projecttwo.models;

import java.util.UUID;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Entry extends RealmObject
{
    @PrimaryKey
    private String id = UUID.randomUUID().toString();
    private String entryName;

    public Entry()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
}
