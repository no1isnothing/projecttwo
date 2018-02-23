package com.thebipolaroptimist.projecttwo.models;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import io.realm.RealmList;

public class EntryDTO {
    public String id = UUID.randomUUID().toString();
    public String entryNote;
    public String entryTime;
    public String lastEditedTime;
    public int overallMood;
    public List<MoodDataDTO> moodDataList;

    static public void EntryDTOToEntry(EntryDTO entryDTO, Entry entry)
    {
        entry.setEntryNote(entryDTO.entryNote);
        entry.setEntryTime(entryDTO.entryTime);
        entry.setOverallMood(entryDTO.overallMood);
        entry.setLastEditTime(entryDTO.lastEditedTime);
        RealmList<MoodData> moodData = new RealmList<>();
        if(entryDTO.moodDataList != null) {
            for (MoodDataDTO moodDataDTO : entryDTO.moodDataList) {
                MoodData data = new MoodData();
                data.setIntensity(moodDataDTO.intensity);
                data.setType(moodDataDTO.type);
                moodData.add(data);
            }
        }
        entry.setMoodDataList(moodData);
    }

    static public void EntryToEntryDTO(Entry entry, EntryDTO entryDTO)
    {
        entryDTO.entryTime = entry.getEntryTime();
        entryDTO.entryNote = entry.getEntryNote();
        entryDTO.overallMood = entry.getOverallMood();
        entryDTO.lastEditedTime = entry.getLastEditTime();

        if(entryDTO.moodDataList == null)
        {
            entryDTO.moodDataList = new ArrayList<>();
        }

        for (MoodData moodData : entry.getMoodDataList()) {
            MoodDataDTO moodDataDTO = new MoodDataDTO();
            moodDataDTO.intensity = moodData.getIntensity();
            moodDataDTO.type = moodData.getType();
            entryDTO.moodDataList.add(moodDataDTO);
        }
    }
}
