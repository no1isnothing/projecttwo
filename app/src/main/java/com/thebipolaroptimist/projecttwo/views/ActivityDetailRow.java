package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class ActivityDetailRow extends DetailRow {
    public static final String CATEGORY = "Activity";
    final private DetailDTO mDetail;
    final private EditText mHourText;
    final private EditText mMinuteText;

    public ActivityDetailRow(Context context, DetailDTO detail) {
        super(context);

        mDetail = detail;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.detail_row_activity, this, true);
        }
        TextView textView = findViewById(R.id.dra_detail_type);
        textView.setText(detail.detailType);
        textView.setTextColor(detail.color);

        mHourText = findViewById(R.id.dra_detail_duration_hr_edit);
        mMinuteText = findViewById(R.id.dra_detail_duration_min_edit);
        if(!detail.detailData.isEmpty())
        {
            //TODO pull this out
            int totalMinutes = Integer.parseInt(detail.detailData);
            mHourText.setText(Integer.toString(totalMinutes/60));
            mMinuteText.setText(Integer.toString(totalMinutes%60));
        }
    }

    @Override
    public DetailDTO getDetailDTO()
    {
        //THIS TOO
        int hour = 0;
        int minute = 0;
        String hourText = mHourText.getText().toString();
        String minuteText = mMinuteText.getText().toString();
        if(!hourText.isEmpty()) hour = Integer.parseInt(hourText);
        if(!minuteText.isEmpty()) minute = Integer.parseInt(minuteText);
        minute +=hour*60;
        mDetail.detailData = Integer.toString(minute);
        return mDetail;
    }
}
