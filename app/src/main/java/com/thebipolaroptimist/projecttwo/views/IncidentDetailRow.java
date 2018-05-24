package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class IncidentDetailRow extends DetailRow {
    public static final String CATEGORY = "Incident";
    final private DetailDTO mDetail;

    public IncidentDetailRow(Context context)
    {
        super(context);
        mDetail = new DetailDTO();
        setupUI();
    }

    public IncidentDetailRow(Context context, AttributeSet set)
    {
        super(context, set);
        mDetail = new DetailDTO();
        setupUI();
    }

    public IncidentDetailRow(Context context, DetailDTO detail) {
        super(context);
        mDetail = detail;
        setupUI();
    }

    private void setupUI()
    {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.detail_row_incident, this, true);
        }

        TextView textView = findViewById(R.id.incident_detail_row_type);
        textView.setText(mDetail.detailType);
        textView.setTextColor(mDetail.color);
    }

    @Override
    public DetailDTO getDetailDTO() {
        mDetail.detailDataUnit = DetailRowFactory.getUnits(CATEGORY);
        return mDetail;
    }
}
