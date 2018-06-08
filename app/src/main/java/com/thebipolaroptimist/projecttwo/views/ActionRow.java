package com.thebipolaroptimist.projecttwo.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.pes.androidmaterialcolorpickerdialog.ColorPicker;
import com.pes.androidmaterialcolorpickerdialog.ColorPickerCallback;
import com.thebipolaroptimist.projecttwo.R;

/*
 * A single row in a UI with text, a color, and a button
 * The constructor receives text and a color to display
 * and a listener to be called when the button is clicked
 */
public class ActionRow extends LinearLayout {
    private EditText mTextPrefName;
    private String mColor;
    private OnClickListener mListener;
    private Button mColorButton;
    private ImageButton mImageButton;
    private Context mContext;

    public interface OnClickListener
    {
        void onClick(View view);

    }

    public ActionRow(Context context)
    {
        super(context);
        mContext = context;
        mListener = new ActionRow.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        };
        setupUI("", mContext.getString(R.string.default_color));
    }

    public ActionRow(Context context, AttributeSet set)
    {
        super(context);
        mContext = context;
        mListener = new ActionRow.OnClickListener(){
            @Override
            public void onClick(View view) {

            }
        };
        setupUI("", mContext.getString(R.string.default_color));
    }

    public ActionRow(Context context, String title, String color, @DrawableRes int resourceId, OnClickListener listener) {
        super(context);
        mContext = context;
        mListener = listener;
        setupUI(title, color);

        mImageButton.setImageResource(resourceId);
    }

    private void setupUI(String title, String color)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_action_row, this, true);
        }
        //mListener = listener;
        mImageButton = findViewById(R.id.action_row_button);

        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(ActionRow.this);
            }
        });

        mTextPrefName = findViewById(R.id.pref_list_edit_text);
        if(title != null) {
            mTextPrefName.setText(title);
        }

        mColorButton = findViewById(R.id.color_button);

        if(color != null)
        {
            mColor = color;
        }else
        {
            mColor = "#000000";
        }

        int parsedColor = Color.parseColor(mColor);
        mColorButton.setBackgroundColor(parsedColor);
        final ColorPicker cp = new ColorPicker((Activity)mContext, Color.alpha(parsedColor), Color.red(parsedColor), Color.green(parsedColor), Color.blue(parsedColor));
        mColorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Show color picker dialog */
                cp.show();

                /* Set a new Listener called when user click "select" */
                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        mColor = String.format("#%08X", (color));
                        mColorButton.setBackgroundColor(Color.parseColor(mColor));
                        cp.dismiss();
                    }
                });
            }
        });
    }

    public String getName()
    {
        return mTextPrefName.getText().toString();
    }

    public String getColor()
    {
        return mColor;
    }

    public void setName(String name)
    {
        mTextPrefName.setText(name);
    }

    public void setColor(String color)
    {
        mColor = color;
        mColorButton.setBackgroundColor(Color.parseColor(color));
    }

    public void clearName(){ mTextPrefName.setText("");}
}
