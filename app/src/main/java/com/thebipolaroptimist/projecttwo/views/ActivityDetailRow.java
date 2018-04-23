package com.thebipolaroptimist.projecttwo.views;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.Detail;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public class ActivityDetailRow extends DetailRow {
    public static final String CATEGORY = "Activity";
    final private DetailDTO mDetail;
    EditText mEditText;


    public ActivityDetailRow(Context context, DetailDTO detail) {
        super(context);

        mDetail = detail;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.detail_row_activity, this, true);
        }
        TextView textView = findViewById(R.id.activity_detail_row_type);
        textView.setText(detail.detailType);
        textView.setTextColor(detail.color);

        mEditText = findViewById(R.id.activity_detail_row_edit);
        if(!detail.detailData.isEmpty())
        {
            mEditText.setText(detail.detailData);
        }
    }

    @Override
    public DetailDTO getDetailDTO()
    {
        mDetail.detailData = mEditText.getText().toString();
        mDetail.detailDataUnit = DetailDTO.getUnits(CATEGORY);
        return mDetail;
    }
}
