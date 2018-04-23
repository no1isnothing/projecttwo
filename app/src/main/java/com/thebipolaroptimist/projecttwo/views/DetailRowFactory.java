package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.os.Bundle;

import com.thebipolaroptimist.projecttwo.dialogs.ActivityDetailDialog;
import com.thebipolaroptimist.projecttwo.dialogs.IncidentDetailDialog;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class DetailRowFactory {

    public static DetailRow getDetailRow(Context context, DetailDTO detail)
    {
        if(detail.category.equals(MoodDetailRow.CATEGORY))
        {
            return new MoodDetailRow(context,detail);
        } else if(detail.category.equals(ActivityDetailDialog.CATEGORY))
        {
            return new ActivityDetailRow(context);
        } else if(detail.category.equals(IncidentDetailDialog.CATEGORY))
        {
            return new IncidentDetailRow(context);
        }
        return null;
    }
}
