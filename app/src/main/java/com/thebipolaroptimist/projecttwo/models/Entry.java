package com.thebipolaroptimist.projecttwo.models;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * This class represents an entry into the journal
 * All entries have time, optional note and overallmood
 * In addition there can be any number of details added to an entry
 */
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

    public String getEntrySummary()
    {
        StringBuilder builder = new StringBuilder();

        builder.append(DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
                .format(new Date(Long.parseLong(entryTime))));
        if(detailList != null && detailList.size() > 0)
        {
            builder.append(" Contains details"); //TODO put something more descriptive here
        }
        return builder.toString();
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
