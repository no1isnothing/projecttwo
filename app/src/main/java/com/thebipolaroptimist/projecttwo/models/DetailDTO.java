package com.thebipolaroptimist.projecttwo.models;


import android.graphics.Color;

/**
 * This class stores detail data and passes it
 * into the Detail class for storage in realm
 */
public class DetailDTO {

    public String category;
    public String detailType;
    public String detailData;
    public String detailDataUnit;
    public int color;

    public DetailDTO()
    {
        detailType = "Default";
        color = Color.BLUE;
    }
}
