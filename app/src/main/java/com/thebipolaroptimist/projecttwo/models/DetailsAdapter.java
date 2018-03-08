package com.thebipolaroptimist.projecttwo.models;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DetailsAdapter extends BaseExpandableListAdapter{
    private Activity mContext;
    private Map<String, Map<String, DetailDTO>> mDetails;
    private List<String> mDetailCategories;

    public DetailsAdapter(Activity context, List<String> detailCategories,
                          Map<String,Map<String, DetailDTO>> details)
    {
        mContext = context;
        mDetailCategories = detailCategories;
        mDetails = details;
    }
    @Override
    public int getGroupCount() {
        return mDetails.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mDetails.get(mDetailCategories.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mDetailCategories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Map<String, DetailDTO> details = mDetails.get(mDetailCategories.get(groupPosition));
        List<DetailDTO> list = new ArrayList<DetailDTO>(details.values()); //TODO consider more efficient way to do this
        return list.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String categoryName = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.detail_list_group,
                    null);
        }
        TextView item = (TextView) convertView.findViewById(R.id.detail_list_title);
        item.setTypeface(null, Typeface.BOLD);
        item.setText(categoryName);
        return convertView;
    }

    //TODO add more data to view
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DetailDTO detail = (DetailDTO) getChild(groupPosition, childPosition);
        LayoutInflater inflater = mContext.getLayoutInflater();

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.detail_list_item, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.expanded_detail_list_item);

        item.setText(detail.detailType);
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}