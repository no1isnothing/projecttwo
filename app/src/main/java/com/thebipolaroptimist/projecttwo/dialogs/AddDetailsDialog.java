package com.thebipolaroptimist.projecttwo.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.SelectableWordData;
import com.thebipolaroptimist.projecttwo.views.ActionRow;
import com.thebipolaroptimist.projecttwo.views.DetailRowFactory;
import com.thebipolaroptimist.projecttwo.views.SelectableWord;

import java.util.List;

public class AddDetailsDialog extends DialogFragment {

    private static final String TAG = "AddDetailDialog";
    public static final String ACTION_ROW_VISIBLE = "ACTION_ROW_VISIBLE";
    private Listener mListener;
    private LinearLayout mDetailTypeList;
    private Button mButtonAddDetailType;
    private ActionRow mActionRow;
    private AddDetailsViewModel mViewModel;

    public interface Listener{
         void onPositiveResult(List<SelectableWordData> results);
    }

    public void setListener(Listener listener)
    {
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(AddDetailsViewModel.class);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if( mActionRow.getVisibility() == View.VISIBLE) {
            mViewModel.addedSelectableWordData = getNewDetailType();
        }
        updateViewModel();
        outState.putInt(ACTION_ROW_VISIBLE, mActionRow.getVisibility());
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Bundle args = getArguments();
        if (args != null && args.containsKey("category")) {
            mViewModel.mCategory = args.getString("category");
            mViewModel.setupDetailTypes();
        } else
        {
            Log.w(TAG, "Created without category argument");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.diloag_add_details, null);
        builder.setView(view);

        View titleView = inflater.inflate(R.layout.dialog_add_details_title, null);
        TextView title = titleView.findViewById(R.id.dadi_title);
        title.setText(mViewModel.mCategory + " " + title.getText());
        titleView.setBackgroundColor(DetailRowFactory.getColorForCategory(mViewModel.mCategory));
        builder.setCustomTitle(titleView);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                updateViewModel();
                List<SelectableWordData> list = mViewModel.getSelected();
                SelectableWordData newDetailType = getNewDetailType();
                if(newDetailType != null)
                {
                    list.add(newDetailType);
                }

                Log.w(TAG, "Calling on positive result. List size: " + list.size());
                mListener.onPositiveResult(list);
                dismiss();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dismiss();
            }
        });

        mDetailTypeList = view.findViewById(R.id.detail_type_list);
        mButtonAddDetailType = view.findViewById(R.id.add_detail_type_button);

        //Add lines to view for each detail type
        for (SelectableWordData selectableWordData : mViewModel.getSelectableWordData().values()) {
            if(args != null && !args.containsKey(selectableWordData.getWord())) {
                mDetailTypeList.addView(new SelectableWord(getActivity(), selectableWordData.getWord(), selectableWordData.getColor(), selectableWordData.isChecked()));
            }
        }

        mActionRow = new ActionRow(getActivity(), null, null,  R.drawable.ic_add_black, new ActionRow.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Add values from action row to view
                ActionRow row = (ActionRow) view;
                mViewModel.addNewDetailType(row.getName(), row.getColor());
                mDetailTypeList.addView(new SelectableWord(getActivity(), row.getName(), row.getColor(), true));
                view.setVisibility(View.INVISIBLE);
                mButtonAddDetailType.setVisibility(View.VISIBLE);
                row.clearName();
            }
        });

        mActionRow.setVisibility(View.INVISIBLE);
        LinearLayout linearLayout = view.findViewById(R.id.add_detail_layout2);
        linearLayout.addView(mActionRow);

        mButtonAddDetailType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActionRow.setVisibility(View.VISIBLE);
                v.setVisibility(View.INVISIBLE);
            }
        });

        if(savedInstanceState != null) {
            int visible = savedInstanceState.getInt(ACTION_ROW_VISIBLE);

            if( visible == View.VISIBLE)
            {
                mActionRow.setVisibility(View.VISIBLE);
                mButtonAddDetailType.setVisibility(View.INVISIBLE);
                if(mViewModel.addedSelectableWordData != null) {
                    mActionRow.setName(mViewModel.addedSelectableWordData.getWord());
                    mActionRow.setColor(mViewModel.addedSelectableWordData.getColor());
                }
            }
        }
        return builder.create();
    }

    private void updateViewModel()
    {
        int count = mDetailTypeList.getChildCount();
        for(int i = 0; i < count; i++)
        {
            try {
                SelectableWord word = (SelectableWord) mDetailTypeList.getChildAt(i);
                SelectableWordData data = word.getData();
                mViewModel.updateChecked(data.getWord(), data.isChecked());
            } catch(ClassCastException e)
            {
                Log.i(TAG, "Failed to cast view to selectable word");
            }
        }
    }

    private SelectableWordData getNewDetailType()
    {
        if(mActionRow.getVisibility() == View.VISIBLE)
        {
            if(!mActionRow.getName().isEmpty() && mActionRow.getColor()!= null)
            {
                return new SelectableWordData(mActionRow.getName(),
                        mActionRow.getColor(), true);
            }
        }
        return null;
    }

}

