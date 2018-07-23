package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;

import com.thebipolaroptimist.projecttwo.R;
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

    public static int getColorForCategory(String category, Context context)
    {
        switch (category)
        {
            case MoodDetailRow.CATEGORY:
                return ResourcesCompat.getColor(context.getResources(),R.color.mood_color ,null);
            case ActivityDetailRow.CATEGORY:
                return ResourcesCompat.getColor(context.getResources(),R.color.activity_color ,null);
            case IncidentDetailRow.CATEGORY:
                return ResourcesCompat.getColor(context.getResources(),R.color.incident_color ,null);
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
