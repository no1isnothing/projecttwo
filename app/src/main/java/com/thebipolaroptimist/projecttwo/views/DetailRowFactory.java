package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.graphics.Color;

import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class DetailRowFactory {
    public static final String UNITS_SCALE = "scale";
    public static final String UNITS_TIME = "time";

    public static DetailRow getDetailRow(Context context, DetailDTO detail)
    {
        switch (detail.category) {
            case MoodDetailRow.CATEGORY:
                return new MoodDetailRow(context, detail);
            case ActivityDetailRow.CATEGORY:
                return new ActivityDetailRow(context, detail);
            case IncidentDetailRow.CATEGORY:
                return new IncidentDetailRow(context, detail);
        }
        return null;
    }

    public static int getColorForCategory(String category)
    {
        switch (category)
        {
            case MoodDetailRow.CATEGORY:
                return Color.BLUE;
            case ActivityDetailRow.CATEGORY:
                return Color.YELLOW;
            case IncidentDetailRow.CATEGORY:
                return Color.RED;
        }
        return Color.WHITE;
    }

    public static String getUnits(String category)
    {
        if(category.equals(MoodDetailRow.CATEGORY))
        {
            return UNITS_SCALE;
        } else if(category.equals(ActivityDetailRow.CATEGORY))
        {
            return UNITS_TIME;
        }
        return "";
    }
}
