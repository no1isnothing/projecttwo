package com.thebipolaroptimist.projecttwo.models;

import io.realm.RealmObject;


/**
 * This class represents details associated with an entry
 * There are categories of detail, for example: mood or activity
 * Within those categories there are types, for example
 * depressed is a type of mood and boxing is a type of exercise
 * Each detail can have data and a unit of measurement
 * For boxing the data would a number and the unit would be minutes
 * For depressed the data would be a number representing intensity and unit would be a scale
 * The detailDataUnit is used to know how to the display the detail data
 */
public class Detail extends RealmObject {
    private String category;
    private String detailType;
    private String detailData;
    private String detailDataUnit;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDetailType() {
        return detailType;
    }

    public void setDetailType(String detailType) {
        this.detailType = detailType;
    }

    public String getDetailData() {
        return detailData;
    }

    public void setDetailData(String detailData) {
        this.detailData = detailData;
    }

    public String getDetailDataUnit() {
        return detailDataUnit;
    }

    public void setDetailDataUnit(String detailDataUnit) {
        this.detailDataUnit = detailDataUnit;
    }
}
