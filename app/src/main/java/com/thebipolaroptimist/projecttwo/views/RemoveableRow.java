package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;

public class RemoveableRow extends LinearLayout {

    public interface OnClickListener
    {
        public void onClick();
    }

    OnClickListener mListener;
    public RemoveableRow(Context context, String title, OnClickListener listener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_removeable_row, this, true);
        }
        mListener = listener;
        Button button = findViewById(R.id.remove_row_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick();
            }
        });

        TextView view = findViewById(R.id.removeable_row_text);
        view.setText(title);
    }
}
