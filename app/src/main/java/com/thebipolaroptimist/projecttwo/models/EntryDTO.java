package com.thebipolaroptimist.projecttwo.models;


import java.util.UUID;

public class EntryDTO {
    public String id = UUID.randomUUID().toString();
    public String entryNote;
    public String entryTime;
    public String lastEditedTime;
    public int overallMood;
}
