package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;

import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class DetailRowFactory {

    public static DetailRow getDetailRow(Context context, DetailDTO detail)
    {
        if(detail.category.equals(MoodDetailRow.CATEGORY))
        {
            return new MoodDetailRow(context,detail);
        } else if(detail.category.equals(ActivityDetailRow.CATEGORY))
        {
            return new ActivityDetailRow(context, detail);
        } else if(detail.category.equals(IncidentDetailRow.CATEGORY))
        {
            return new IncidentDetailRow(context, detail);
        }
        return null;
    }
}
