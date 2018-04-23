package com.thebipolaroptimist.projecttwo.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.dialogs.AddDetailsDialog;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;

import java.util.ArrayList;
import java.util.List;

public class CategoryLayout extends ConstraintLayout implements AddDetailsDialog.Listener {
    private final ImageButton mAddButton;
    private final LinearLayout mLayout;
    private final String mCategory;
    private final Context mContext;
    private final List<DetailDTO> mDetails;
    private final ToggleButton mExpandButton;

    public CategoryLayout(Context context, String category, List<DetailDTO> details) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.category_layout, this, true);
        }

        mContext = context;
        mLayout = findViewById(R.id.detail_list);
        mAddButton = findViewById(R.id.add_detail_button);
        mExpandButton = findViewById(R.id.expand_details_button);
        mCategory = category;
        mDetails = details;
        mExpandButton.setButtonDrawable(R.drawable.ic_expand_more_black);
        mExpandButton.setTextOff(category);
        mExpandButton.setTextOn(category);
        mExpandButton.setText(category);
        mAddButton.setVisibility(INVISIBLE);
        mAddButton.setEnabled(false);

        mExpandButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    buttonView.setButtonDrawable(R.drawable.ic_expand_less_black);
                    mAddButton.setVisibility(VISIBLE);
                    mAddButton.setEnabled(true);
                    expand();
                }else
                {
                    buttonView.setButtonDrawable(R.drawable.ic_expand_more_black);
                    mLayout.removeAllViews();
                    mAddButton.setVisibility(INVISIBLE);
                    mAddButton.setEnabled(false);
                }
            }
        });
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });

    }

    public void add()
    {
        //launch add dialog
        Bundle bundle = new Bundle();
        bundle.putString("category", mCategory);

        for (DetailDTO mDetail : mDetails) {
            bundle.putBoolean(mDetail.detailType, true);;
        }


        AddDetailsDialog fragment = new AddDetailsDialog();
        fragment.setArguments(bundle);
        fragment.setListener(this);
        fragment.show(((Activity)mContext).getFragmentManager(), "AddDetailsDialog");
    }

    public List<DetailDTO> onSave()
    {
        List<DetailDTO> dtoList = new ArrayList<>();
        int count = mLayout.getChildCount();

        DetailRow row;
        for(int i = 0; i < count; i++)
        {
           row = (DetailRow) mLayout.getChildAt(i);
           dtoList.add(row.getDetailDTO());
        }

        return dtoList;
    }

    public void expand()
    {
        for (DetailDTO detail : mDetails) {
            mLayout.addView(DetailRowFactory.getDetailRow(mContext, detail));
        }
    }

    @Override
    public void onPositiveResult(List<String> detailTypes) {
        for (String detailType : detailTypes) {
            String[] pieces = detailType.split(":");
            if(pieces.length > 1)
            {
                DetailDTO detail = new DetailDTO();
                detail.detailType = pieces[0];
                detail.color = Color.parseColor(pieces[1]);
                detail.detailData = "";
                detail.category = mCategory;
                mDetails.add(detail);
                mLayout.addView(DetailRowFactory.getDetailRow(mContext, detail));
            }
        }
    }
}
