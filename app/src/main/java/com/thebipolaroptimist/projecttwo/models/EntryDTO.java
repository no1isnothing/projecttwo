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
    public List<DetailDTO> detailList;

    static public void EntryDTOToEntry(EntryDTO entryDTO, Entry entry)
    {
        entry.setEntryNote(entryDTO.entryNote);
        entry.setEntryTime(entryDTO.entryTime);
        entry.setOverallMood(entryDTO.overallMood);
        entry.setLastEditTime(entryDTO.lastEditedTime);
        RealmList<Detail> details = new RealmList<>();
        if(entryDTO.detailList != null) {
            for (DetailDTO detailDTO : entryDTO.detailList) {
                Detail data = new Detail();
                data.setDetailType(detailDTO.detailType);
                data.setCategory(detailDTO.category);
                data.setDetailData(detailDTO.detailData);
                details.add(data);
            }
        }
        entry.setDetailList(details);
    }

    static public void EntryToEntryDTO(Entry entry, EntryDTO entryDTO)
    {
        entryDTO.entryTime = entry.getEntryTime();
        entryDTO.entryNote = entry.getEntryNote();
        entryDTO.overallMood = entry.getOverallMood();
        entryDTO.lastEditedTime = entry.getLastEditTime();

        if(entryDTO.detailList == null)
        {
            entryDTO.detailList = new ArrayList<>();
        }

        for (Detail detail : entry.getDetailList()) {
            DetailDTO detailDTO = new DetailDTO();
            detailDTO.detailType = detail.getDetailType();
            detailDTO.category = detail.getCategory();
            detailDTO.detailData = detail.getDetailData();
            entryDTO.detailList.add(detailDTO);
        }
    }
}
