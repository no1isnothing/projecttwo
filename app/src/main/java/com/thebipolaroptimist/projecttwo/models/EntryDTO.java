package com.thebipolaroptimist.projecttwo.models;


import com.thebipolaroptimist.projecttwo.EntryCalendarActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import io.realm.RealmList;


/**
 * This stores data for transfer to realm entry
 * It also handles converting data between Realm and DTO objects
 *
 * Unlike the Entry, the EntryDTO stores the details mapped by category and type
 * It also maintains a list of category names.
 * This makes interacting with the UI and updating details easier
 */
public class EntryDTO {
    public String id = UUID.randomUUID().toString();
    public String entryNote;
    public String entryTime;
    public String lastEditedTime;
    public int overallMood;
    public List<String> detailCategories;
    public Map<String, Map<String,DetailDTO>> categoriesToDetails;

    public EntryDTO()
    {
        detailCategories = new ArrayList<>();
        categoriesToDetails = new HashMap<>();
    }

    static public void EntryDTOToEntry(EntryDTO entryDTO, Entry entry)
    {
        entry.setEntryNote(entryDTO.entryNote);
        entry.setEntryTime(entryDTO.entryTime);
        entry.setOverallMood(entryDTO.overallMood);
        entry.setLastEditTime(entryDTO.lastEditedTime);
        RealmList<Detail> details = new RealmList<>();
        //Go through the maps to get all the details into a single list
        if(entryDTO.categoriesToDetails != null && entryDTO.detailCategories != null) {
            for(String category : entryDTO.detailCategories) {
                Map<String, DetailDTO> detailMap = entryDTO.categoriesToDetails.get(category);
                Set<String> keys = detailMap.keySet();
                for(String key : keys) {
                    DetailDTO detailDTO = detailMap.get(key);
                    Detail data = new Detail();
                    data.setDetailType(detailDTO.detailType);
                    data.setCategory(detailDTO.category);
                    data.setDetailData(detailDTO.detailData);
                    data.setDetailDataUnit(detailDTO.detailDataUnit);
                    data.setColor(detailDTO.color);
                    details.add(data);
                }
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

        if(entryDTO.detailCategories == null)
        {
            entryDTO.detailCategories = new ArrayList<>();
        }

        if(entryDTO.categoriesToDetails == null)
        {
            entryDTO.categoriesToDetails = new HashMap<>();
        }

        //iterate over the list and store details in maps
        for (Detail detail : entry.getDetailList()) {
            DetailDTO detailDTO = new DetailDTO();
            detailDTO.detailType = detail.getDetailType();
            detailDTO.category = detail.getCategory();
            detailDTO.detailData = detail.getDetailData();
            detailDTO.detailDataUnit = detail.getDetailDataUnit();
            detailDTO.color = detail.getColor();
            Map<String, DetailDTO> detailMap = entryDTO.categoriesToDetails.get(detail.getCategory());
            if(detailMap == null)
            {
                detailMap = new HashMap<>();
                entryDTO.detailCategories.add(detailDTO.category);
                entryDTO.categoriesToDetails.put(detailDTO.category, detailMap);
            }
            detailMap.put(detailDTO.detailType, detailDTO);
        }
    }

    /**
     * Add the list of details to this entry
     * details should all be for the same category
     * @param detailList list of details to add to entry
     * @param category category to add details to
     */
    public void saveList(List<DetailDTO> detailList, String category) {
        if(detailList.size() > 0) {
            Map<String, DetailDTO> detailDTOMap = categoriesToDetails.get(category);
            if (detailDTOMap == null) {
                detailDTOMap = new HashMap<>();
                categoriesToDetails.put(category, detailDTOMap);
                detailCategories.add(category);
            }

            for (DetailDTO dto : detailList) {
                detailDTOMap.put(dto.detailType, dto);
            }
        }
    }

    /**
     * Retrieves details of given type for this entry
     * @param category Category to get details for
     * @return list of details
     */
    public List<DetailDTO> getDetailList(String category)
    {
        List<DetailDTO> details = new ArrayList<>();
        if(categoriesToDetails != null) {
            Map<String, DetailDTO> detailDTOMap = categoriesToDetails.get(category);
            if(detailDTOMap != null) {
                Set<String> keys = detailDTOMap.keySet();
                for (String key : keys) {
                    details.add(detailDTOMap.get(key));
                }
            }
        }
        return details;
    }

    /**
     * Update the lastEditedTime and set entryTime for new entries
     * @param dateString previous date
     */
    public void updateTimes(String dateString)
    {
        SimpleDateFormat format = new SimpleDateFormat(EntryCalendarActivity.DATE_FORMAT_PATTERN);
        Long time = System.currentTimeMillis();
        if(dateString != null && !dateString.isEmpty()) {
            try {
                Date date = format.parse(dateString);

                Calendar calendar = Calendar.getInstance();
                int hour = calendar.get(Calendar.HOUR);
                int minute = calendar.get(Calendar.MINUTE);

                calendar.setTimeInMillis(date.getTime());
                calendar.set(Calendar.HOUR, hour);
                calendar.set(Calendar.MINUTE, minute);
                time = calendar.getTimeInMillis();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        lastEditedTime = time.toString();

        if (entryTime == null) {
            entryTime = lastEditedTime;
        }
    }
}
