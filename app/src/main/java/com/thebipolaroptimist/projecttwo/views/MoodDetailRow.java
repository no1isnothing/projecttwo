package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class MoodDetailRow extends DetailRow {
    public static final String CATEGORY = "Mood";
    final private SeekBar mSeekbar;
    final private DetailDTO mDetail;

    public MoodDetailRow(Context context, DetailDTO detail) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.detail_row_mood, this, true);
        }

        mDetail = detail;
        mSeekbar = findViewById(R.id.mood_detail_row_value);
        if(!detail.detailData.isEmpty()) {
            mSeekbar.setProgress(Integer.parseInt(detail.detailData));
        }
        TextView textView = findViewById(R.id.mood_detail_row_type);
        textView.setText(detail.detailType);
        textView.setTextColor(detail.color);
    }

    @Override
    public DetailDTO getDetailDTO() {
        mDetail.detailData = Integer.toString(mSeekbar.getProgress());
        mDetail.detailDataUnit = DetailDTO.getUnits(CATEGORY);
        return mDetail;
    }
}
