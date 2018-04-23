package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class IncidentDetailRow extends DetailRow {
    public static final String CATEGORY = "Incident";
    final private DetailDTO mDetail;
    public IncidentDetailRow(Context context, DetailDTO detail) {
        super(context);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.detail_row_incident, this, true);
        }

        mDetail = detail;

        TextView textView = findViewById(R.id.incident_detail_row_type);
        textView.setText(detail.detailType);
        textView.setTextColor(detail.color);
    }

    @Override
    public DetailDTO getDetailDTO() {
        mDetail.detailDataUnit = DetailDTO.getUnits(CATEGORY);
        return mDetail;
    }
}
