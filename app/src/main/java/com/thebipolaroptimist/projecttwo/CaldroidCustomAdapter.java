package com.thebipolaroptimist.projecttwo;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidGridAdapter;
import com.thebipolaroptimist.projecttwo.models.Detail;
import com.thebipolaroptimist.projecttwo.models.Entry;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import hirondelle.date4j.DateTime;

public class CaldroidCustomAdapter extends CaldroidGridAdapter{
    public static final String DATE_FORMAT_PATTERN = "MMM DD YYYY";
    public CaldroidCustomAdapter(Context context, int month, int year,
                                 Map<String, Object> caldroidData, Map<String, Object> extraData)
    {
         super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //Set up layout for day cell
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;
        if(cellView == null)
        {
            cellView = inflater.inflate(R.layout.custom_calendar_cell, null);
        }

        //Get day and resources
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        //Set day number
        TextView tv1 = cellView.findViewById(R.id.tv1);
        tv1.setText("" + dateTime.getDay());

        // Dim color for dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        } else
        {
            cellView.setBackgroundColor(resources
                    .getColor(com.caldroid.R.color.caldroid_white));
        }

        if (dateTime.equals(getToday())) {
            cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
        }

        //Set up colored dots matching details for this day
        String key = dateTime.format(DATE_FORMAT_PATTERN, Locale.US);
        List<Entry> entries = (List<Entry>) extraData.get(key);

        //List of ids for views to draw dots in
        //If this code ends up changing often, pull the ids from the layout
        //instead of hard coding them
        int[] idList = {R.id.image_view_1, R.id.image_view_2,R.id.image_view_3,
                R.id.image_view_4, R.id.image_view_5, R.id.image_view_6};

        if(entries!= null) {
            int i = 0;
            for (Entry entry : entries) {
                for(Detail detail : entry.getDetailList()) {
                    ImageView detailView = cellView.findViewById(idList[i]);
                    detailView.setVisibility(View.VISIBLE);
                    GradientDrawable drawable = (GradientDrawable) resources.getDrawable(R.drawable.circle);
                    drawable.setColor(detail.getColor());
                    detailView.setImageDrawable(drawable);
                    i++;
                    if(i >= idList.length)
                    {
                        return  cellView;
                    }
                }
            }
        }

        return cellView;
    }
}


