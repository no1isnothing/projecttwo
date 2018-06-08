package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thebipolaroptimist.projecttwo.R;
import com.thebipolaroptimist.projecttwo.models.SelectableWordData;

/**
 * A layout that's a word, check box and color
 */
public class SelectableWord extends LinearLayout {
    private static final String TAG = "SelectableWord";
    private CheckBox mBox;
    private final String mWord;
    private final String mColor;
    private final Context mContext;

    public SelectableWord(Context context, String word, String color, boolean isChecked) {
        super(context);

        mWord = word;
        mColor = color;
        mContext = context;
        setupUI(isChecked);
    }

    public SelectableWord(Context context)
    {
        super(context);
        mContext =context;
        mWord = "";
        mColor = mContext.getString(R.string.default_color);
        setupUI(false);
    }

    public SelectableWord(Context context, AttributeSet set)
    {
        super(context);
        mContext =context;
        mWord = "";
        mColor = mContext.getString(R.string.default_color);
        setupUI(false);
    }

    private void setupUI(boolean isChecked)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_selectable_word, this, true);
        }

        TextView wordView = findViewById(R.id.selectable_word_text);
        wordView.setText(mWord);
        wordView.setTextColor(Color.parseColor(mColor));
        mBox = findViewById(R.id.selectable_word_checkbox);
        if(isChecked) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mBox.setChecked(true);
                    Log.i(TAG, "Create Selectable word checked");
                }
            }, 500);
        }
    }

    public boolean isChecked()
    {
        return mBox.isChecked();
    }

    public void setChecked(boolean checked) { mBox.setChecked(checked);}

    /*
    public String getWord()
    {
        return mWord;
    }

    public String getColor()
    {
        return mColor;
    }*/

    public SelectableWordData getData()
    {
        return new SelectableWordData(mWord, mColor, mBox.isChecked());
    }
}
