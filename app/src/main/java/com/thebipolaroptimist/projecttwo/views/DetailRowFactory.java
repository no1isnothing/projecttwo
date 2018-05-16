package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;

import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class DetailRowFactory {

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
}
