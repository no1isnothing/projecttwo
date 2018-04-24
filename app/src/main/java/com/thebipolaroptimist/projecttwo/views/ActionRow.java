package com.thebipolaroptimist.projecttwo.views;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
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
    OnClickListener mListener;

    public interface OnClickListener
    {
        void onClick(View view);

    }

    public String getName()
    {
        return mTextPrefName.getText().toString();
    }

    public String getColor()
    {
        return mColor;
    }

    public ActionRow(Context context, String title, String color, @DrawableRes int resourceId, OnClickListener listener) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            inflater.inflate(R.layout.view_action_row, this, true);
        }
        mListener = listener;
        ImageButton button = findViewById(R.id.action_row_button);
        button.setImageResource(resourceId);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(ActionRow.this);
            }
        });

        mTextPrefName = findViewById(R.id.pref_list_edit_text);
        if(title != null) {
            mTextPrefName.setText(title);
        }

        final Button colorButton = findViewById(R.id.color_button);
        int parsedColor = Color.BLACK;
        if(color != null)
        {
            mColor = color;
            parsedColor = Color.parseColor(mColor);
            colorButton.setBackgroundColor(parsedColor);
        }

        final ColorPicker cp = new ColorPicker((Activity)context, Color.alpha(parsedColor), Color.red(parsedColor), Color.green(parsedColor), Color.blue(parsedColor));
        colorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Show color picker dialog */
                cp.show();

                /* Set a new Listener called when user click "select" */
                cp.setCallback(new ColorPickerCallback() {
                    @Override
                    public void onColorChosen(@ColorInt int color) {
                        mColor = String.format("#%08X", (0xFFFFFFFF & color));
                        colorButton.setBackgroundColor(Color.parseColor(mColor));
                        cp.dismiss();
                    }
                });
            }
        });
    }
}
