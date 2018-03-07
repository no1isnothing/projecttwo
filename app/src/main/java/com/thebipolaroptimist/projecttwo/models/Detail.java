package com.thebipolaroptimist.projecttwo.models;

import io.realm.RealmObject;

public class Detail extends RealmObject {
    private String category; //change to enum?
    private String detailType;
    private String detailData;

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
}
