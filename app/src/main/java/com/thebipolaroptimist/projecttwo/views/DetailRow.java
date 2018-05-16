package com.thebipolaroptimist.projecttwo.views;

import android.content.Context;
import android.widget.LinearLayout;

import com.thebipolaroptimist.projecttwo.models.DetailDTO;

public abstract class DetailRow extends LinearLayout {
    public DetailRow(Context context) {
        super(context);
    }
    abstract public DetailDTO getDetailDTO();
}
