package com.thebipolaroptimist.projecttwo.models;

import io.realm.RealmObject;

public class MoodData extends RealmObject {
    private String type; //change to enum?
    private int intensity;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIntensity() {
        return intensity;
    }

    public void setIntensity(int intensity) {
        this.intensity = intensity;
    }
}
