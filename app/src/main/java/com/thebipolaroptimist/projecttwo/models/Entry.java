package com.thebipolaroptimist.projecttwo.models;

import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Entry extends RealmObject
{
    @PrimaryKey
    private String id = UUID.randomUUID().toString();

    private String entryNote;
    private String entryTime;
    private int overallMood;
    private String lastEditTime;
    private RealmList<Detail> detailList;

    public Entry()
    {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryNote() {
        return entryNote;
    }

    public void setEntryNote(String entryNote) {
        this.entryNote = entryNote;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public int getOverallMood() {
        return overallMood;
    }

    public void setOverallMood(int overallMood) {
        this.overallMood = overallMood;
    }

    public String getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public List<Detail> getDetailList() {
        return detailList;
    }

    public void setDetailList(RealmList<Detail> detailList) {
        if(this.detailList == null)
        {
            this.detailList = new RealmList<>();
        }
        this.detailList.addAll(detailList);
    }
}
