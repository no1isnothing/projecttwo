package com.thebipolaroptimist.projecttwo.models;


import com.thebipolaroptimist.projecttwo.dialogs.ActivityDetailDialog;
import com.thebipolaroptimist.projecttwo.dialogs.MoodDetailDialog;
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

    /**
     *
     * @return a string representing this details data
     */
    public String getDataString()
    {
        if(detailDataUnit.equals(UNITS_SCALE)) {
            int dataValue = Integer.parseInt(detailData);
            String scaleValue = "";
            if(dataValue < 20)
            {
                scaleValue = "Very Low";
            } else if(dataValue < 40)
            {
                scaleValue = "Low";
            } else if(dataValue < 60)
            {
                scaleValue = "Mid";
            } else if(dataValue < 80)
            {
                scaleValue = "High";
            }else
            {
                scaleValue = "Very High";
            }
            return scaleValue;
        } else if(detailDataUnit.equals(UNITS_MINUTES))
        {
            return detailData + " " + detailDataUnit;
        } else
        {
            return detailData;
        }

    }

    //TODO consider using map or settings
    public static String getUnits(String category)
    {
        if(category.equals(MoodDetailRow.CATEGORY))
        {
            return UNITS_SCALE;
        } else if(category.equals(ActivityDetailDialog.CATEGORY))
        {
            return UNITS_MINUTES;
        }
        return "";
    }
}
