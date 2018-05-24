package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public abstract class DetailRow extends LinearLayout {
    public DetailRow(Context context) {
        super(context);
    }
    public DetailRow(Context context, AttributeSet set) { super(context, set);}
    abstract public DetailDTO getDetailDTO();
}
