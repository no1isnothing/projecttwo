package com.thebipolaroptimist.projecttwo.models;

public class Entry {
    private String entryName;

    public Entry(String name)
    {
        entryName = name;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
}
