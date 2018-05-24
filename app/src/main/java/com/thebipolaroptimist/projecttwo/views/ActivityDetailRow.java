package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class ActivityDetailRow extends DetailRow {
    public static final String CATEGORY = "Activity";
    final private DetailDTO mDetail;
    private EditText mHourText;
    private EditText mMinuteText;

    public ActivityDetailRow(Context context)
    {
        super(context);
        mDetail= new DetailDTO();
        setupUI();
    }

    public ActivityDetailRow(Context context, AttributeSet set)
    {
        super(context, set);
        mDetail = new DetailDTO();
        setupUI();
    }

    public ActivityDetailRow(Context context, DetailDTO detail) {
        super(context);

        mDetail = detail;
        setupUI();
    }

    private void setupUI()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.detail_row_activity, this, true);
        }
        TextView textView = findViewById(R.id.dra_detail_type);
        textView.setText(mDetail.detailType);
        textView.setTextColor(mDetail.color);

        mHourText = findViewById(R.id.dra_detail_duration_hr_edit);
        mMinuteText = findViewById(R.id.dra_detail_duration_min_edit);
        if(!mDetail.detailData.isEmpty())
        {
            int totalMinutes = Integer.parseInt(mDetail.detailData);
            Time time = new Time(totalMinutes);
            mHourText.setText(time.hour);
            mMinuteText.setText(time.minute);
        }
    }

    @Override
    public DetailDTO getDetailDTO()
    {
        String hourText = mHourText.getText().toString();
        String minuteText = mMinuteText.getText().toString();
        Time time = new Time(hourText, minuteText);
        mDetail.detailData = Integer.toString(time.totalMinutes);
        return mDetail;
    }

    class Time
    {
        int totalMinutes;
        String hour;
        String minute;

        Time(int time)
        {
            totalMinutes = time;
            hour = Integer.toString(totalMinutes/60);
            minute = Integer.toString(totalMinutes%60);
        }


        Time(String hour, String minute)
        {
            this.hour = hour;
            this.minute = minute;

            int hourTime = 0;
            totalMinutes = 0;
            if(!this.hour.isEmpty()) hourTime = Integer.parseInt(this.hour);
            if(!this.minute.isEmpty()) totalMinutes = Integer.parseInt(this.minute);
            totalMinutes +=hourTime*60;
        }

    }
}
