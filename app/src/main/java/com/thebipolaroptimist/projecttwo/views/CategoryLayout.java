package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.SettingsFragment;
import com.thebipolaroptimist.projecttwo.dialogs.AddDetailsDialog;
import com.thebipolaroptimist.projecttwo.models.DetailDTO;
import com.thebipolaroptimist.projecttwo.models.SelectableWordData;

import java.util.ArrayList;
import java.util.List;

/**
 * A layout to hold the details of a category
 * It displays the category name and an expandable list of details
 * The list of details are editable and new detail types can be added
 */
public class CategoryLayout extends ConstraintLayout implements AddDetailsDialog.Listener {
    private static final String TAG = "CategoryLayout";
    private Button mAddButton;
    private LinearLayout mLayout;
    private final String mCategory;
    private final Context mContext;
    private final List<DetailDTO> mDetails;
    private ToggleButton mExpandButton;
    private TextView mDetailDataTypeLabel;

    public CategoryLayout(Context context, String category, List<DetailDTO> details, boolean expanded) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.category_layout, this, true);
        }

        mContext = context;
        mCategory = category;
        mDetails = details;
        setupUI();

        if(expanded) expand();
    }

    public CategoryLayout(Context context)
    {
        super(context);
        mContext = context;
        mCategory = "";
        mDetails = new ArrayList<>();
        setupUI();
    }

    public CategoryLayout(Context context, AttributeSet set)
    {
        super(context);
        mContext = context;
        mCategory = "";
        mDetails = new ArrayList<>();
        setupUI();
    }

    private void setupUI()
    {
        mLayout = findViewById(R.id.detail_list);
        mAddButton = findViewById(R.id.add_detail_button);
        mExpandButton = findViewById(R.id.expand_details_button);
        mDetailDataTypeLabel = findViewById(R.id.detail_data_type_label);
        setUpButtons();
    }

    public String getCategory()
    {
        return mCategory;
    }

    private void setUpButtons()
    {
        mExpandButton.setButtonDrawable(R.drawable.ic_expand_more_black);
        mExpandButton.setTextOff(mCategory);
        mExpandButton.setTextOn(mCategory);
        mExpandButton.setText(mCategory);
        mAddButton.setVisibility(GONE);
        mDetailDataTypeLabel.setVisibility(GONE);
        mDetailDataTypeLabel.setText(SettingsFragment.getLabel(mCategory));
        mAddButton.setEnabled(false);

        mExpandButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    expand();
                }else
                {
                    buttonView.setButtonDrawable(R.drawable.ic_expand_more_black);
                    mLayout.removeAllViews(); //TODO should be remove or hide? hide is more efficient
                    mAddButton.setVisibility(GONE);
                    mAddButton.setEnabled(false);
                    mDetailDataTypeLabel.setVisibility(GONE);
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

    /**
     * Launches the add detail dialog to allow user to add detail types
     */
    private void add()
    {
        //launch add dialog
        Bundle bundle = new Bundle();
        bundle.putString("category", mCategory);

        for (DetailDTO mDetail : mDetails) {
            bundle.putBoolean(mDetail.detailType, true);
        }

        AddDetailsDialog fragment = new AddDetailsDialog();
        fragment.setArguments(bundle);
        fragment.setListener(this);
        fragment.show(((AppCompatActivity)mContext).getSupportFragmentManager(), "AddDetailsDialog_" + mCategory);
    }

    /**
     * Retrieve the data from this layout
     * @return a list of detail dto, one for each row
     */
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

    /**
     * Return whether or not the layout is showing details
     */
    public boolean isExpanded()
    {
        return mAddButton.isEnabled();
    }

    /**
     * Return whether or not this view contains details
     */
    private boolean hasDetails()
    {
        return mLayout.getChildCount() > 0;
    }

    /**
     * Create a row for each detail in this entry
     */
    private void expand()
    {
        mExpandButton.setButtonDrawable(R.drawable.ic_expand_less_black);
        mAddButton.setVisibility(VISIBLE);
        mAddButton.setEnabled(true);

        for (DetailDTO detail : mDetails) {
            Log.w(TAG, "Exp: Adding view Type: " + detail.detailType + " Color: " + detail.color);
            mLayout.addView(DetailRowFactory.getDetailRow(mContext, detail));
        }

        if(hasDetails()) {
            mDetailDataTypeLabel.setVisibility(VISIBLE);
        }
    }

    /**
     * Call back from add detail dialog. Adds new detail types to variables and layouts
     * @param results list of detail types to add
     */
    @Override
    public void onPositiveResult(List<SelectableWordData> results) {
        Log.w(TAG, "On Positive Result. List size " + results.size());
        for (SelectableWordData result : results) {
                    DetailDTO detail = new DetailDTO();
                    detail.detailType = result.getWord();
                    detail.color = Color.parseColor(result.getColor());
                    detail.detailData = "";
                    detail.category = mCategory;
                    mDetails.add(detail);
                    Log.w(TAG, "OnPR: Adding view Type: " + detail.detailType + " Color: " + detail.color);
                    mLayout.addView(DetailRowFactory.getDetailRow(mContext, detail));
        }

        if(hasDetails())
        {
            mDetailDataTypeLabel.setVisibility(VISIBLE);
        }
    }
}
