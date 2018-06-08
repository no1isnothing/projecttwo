package com.thebipolaroptimist.projecttwo.models;

public class SelectableWordData {
    private String mWord;
    private String mColor;
    private boolean mChecked;

    public SelectableWordData(String word, String color, boolean checked)
    {
        mWord = word;
        mColor = color;
        mChecked = checked;
    }

    public String getWord() {
        return mWord;
    }

    public void setWord(String word) {
        mWord = word;
    }

    public String getColor() {
        return mColor;
    }

    public void setColor(String color) {
        mColor = color;
    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        mChecked = checked;
    }
}
