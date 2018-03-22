package com.thebipolaroptimist.projecttwo;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.thebipolaroptimist.projecttwo.models.Entry;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import hirondelle.date4j.DateTime;

public class CaldroidCustomAdapter extends CaldroidGridAdapter{
    public CaldroidCustomAdapter(Context context, int month, int year,
                                 Map<String, Object> caldroidData, Map<String, Object> extraData)
    {
         super(context, month, year, caldroidData, extraData);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;
        if(convertView == null)
        {
            cellView = inflater.inflate(R.layout.custom_calendar_cell, null);
        }
        int topPadding = cellView.getPaddingTop();
        int leftPadding = cellView.getPaddingLeft();
        int bottomPadding = cellView.getPaddingBottom();
        int rightPadding = cellView.getPaddingRight();
        TextView tv1 = (TextView) cellView.findViewById(R.id.tv1);

        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();

        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(com.caldroid.R.color.caldroid_darker_gray));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {
            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }


        tv1.setText("" + dateTime.getDay());
        String key = dateTime.format("DD MMM YYYY", Locale.US);
        Entry entry = (Entry) extraData.get(key);
        int[] idList = {R.id.image_view_1, R.id.image_view_2,R.id.image_view_3,
                R.id.image_view_4, R.id.image_view_5, R.id.image_view_6};
        if(entry!= null) {
            for (int i = 0; i < entry.getDetailList().size() && i < 6; i++) {
                ImageView detailView = cellView.findViewById(idList[i]);
                detailView.setVisibility(View.VISIBLE);
                GradientDrawable drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.circle);
                drawable.setColor(entry.getDetailList().get(i).getColor());
                detailView.setImageDrawable(drawable);
            }
        }
        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding
        cellView.setPadding(leftPadding, topPadding, rightPadding,
                bottomPadding);

        // Set custom color if required
        return cellView;
    }
}


