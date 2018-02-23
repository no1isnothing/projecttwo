package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;

public class SelectableSeekBar extends LinearLayout {
    final SeekBar bar;
    public SelectableSeekBar(Context context, AttributeSet set) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_selectable_seekbar, this, true);
        }
        CheckBox box = findViewById(R.id.checkbox);
        bar = findViewById(R.id.seekbar_selectable);
        bar.setEnabled(false);
        box.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = ((CheckBox) v).isChecked();

                if(checked)
                {
                    bar.setEnabled(true);
                } else
                {
                    bar.setEnabled(false);
                }
            }
        });
    }

    public int getValue()
    {
        if(bar.isEnabled())
        {
            return bar.getProgress();
        } else
        {
            return -1;
        }
    }

    public void setValue(int value)
    {
        bar.setProgress(value);
    }

    public String getTitle()
    {
        TextView view = findViewById(R.id.title_seekbar);
        return view.getText().toString();
    }

    public void setTitle(String title)
    {
        TextView view = findViewById(R.id.title_seekbar);
        view.setText(title);
    }

    /*
    public SelectableSeekBar(Context context, AttributeSet set)
    {
        SelectableSeekBar(context);
    }*/
}
