package com.thebipolaroptimist.projecttwo.models;


import com.thebipolaroptimist.projecttwo.views.ActivityDetailRow;
import com.thebipolaroptimist.projecttwo.views.MoodDetailRow;

/**
 * This class stores detail data and passes it
 * into the Detail class for storage in realm
 */
public class DetailDTO {
    public static final String UNITS_SCALE = "scale";
    public static final String UNITS_MINUTES = "minutes";

    public String category; //
    public String detailType;
    public String detailData;
    public String detailDataUnit;
    public int color;

    //TODO consider using map or settings
    public static String getUnits(String category)
    {
        if(category.equals(MoodDetailRow.CATEGORY))
        {
            return UNITS_SCALE;
        } else if(category.equals(ActivityDetailRow.CATEGORY))
        {
            return UNITS_MINUTES;
        }
        return "";
    }
}
