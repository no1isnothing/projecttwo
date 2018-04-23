package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;

public class SelectableWord extends LinearLayout {
    final private CheckBox mBox;
    private final String mWord;
    private final String mColor;

    public SelectableWord(Context context, String word, String color) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_selectable_word, this, true);
        }
        mWord = word;
        mColor = color;
        TextView wordView = findViewById(R.id.selectable_word_text);
        wordView.setText(mWord);
        wordView.setTextColor(Color.parseColor(mColor));
        mBox = findViewById(R.id.selectable_word_checkbox);
    }

    public boolean isChecked()
    {
        return mBox.isChecked();
    }

    public String getWord()
    {
        return mWord;
    }

    public String getColor()
    {
        return mColor;
    }
}
