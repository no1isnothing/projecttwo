package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class MoodDetailRow extends DetailRow {
    public static final String CATEGORY = "Mood";
    private SeekBar mSeekbar;
    final private DetailDTO mDetail;

    public MoodDetailRow(Context context)
    {
        super(context);

        mDetail = new DetailDTO();
        setupUI();
    }

    public MoodDetailRow(Context context, AttributeSet set)
    {
        super(context, set);
        mDetail = new DetailDTO();
        setupUI();
    }

    public MoodDetailRow(Context context, DetailDTO detail) {
        super(context);

        mDetail = detail;
        setupUI();
    }

    private void setupUI()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.detail_row_mood, this, true);
        }

        mSeekbar = findViewById(R.id.mood_detail_row_value);

        if(!mDetail.detailData.isEmpty()) {
            mSeekbar.setProgress(Integer.parseInt(mDetail.detailData));
        }
        TextView textView = findViewById(R.id.mood_detail_row_type);
        textView.setText(mDetail.detailType);
        textView.setTextColor(mDetail.color);

    }

    @Override
    public DetailDTO getDetailDTO() {
        mDetail.detailData = Integer.toString(mSeekbar.getProgress());
        mDetail.detailDataUnit = DetailRowFactory.getUnits(CATEGORY);
        return mDetail;
    }
}
