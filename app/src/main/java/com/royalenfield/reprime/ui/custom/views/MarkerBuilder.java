package com.royalenfield.reprime.ui.custom.views;

/**
 * Created by JAL7KOR on 1/7/2019.
 */

@SuppressWarnings("unused")
public class MarkerBuilder {

    private static final String VALUE_SEPARATOR = "%7C";
    private double mLatitude, mLongitude;
    private String mColor;
    private Character mLabel;
    private MarkerSize mSize;

    public MarkerBuilder() {
        mLatitude = 0;
        mLongitude = 0;
        mColor = null;
        mLabel = null;
        mSize = MarkerSize.NORMAL;
    }

    public MarkerBuilder position(double latitude, double longitude) {
        mLatitude = latitude;
        mLongitude = longitude;

        return this;
    }

    public MarkerBuilder size(MarkerSize size) {
        mSize = size;

        return this;
    }

    public MarkerBuilder color(int color) {
        mColor = Integer.toHexString(color);

        if (mColor.length() == 8) {
            mColor = mColor.substring(2);
        }

        return this;
    }

    public MarkerBuilder color(String color) {
        mColor = color;
        return this;
    }

    public MarkerBuilder label(String label) {
        mLabel = label.charAt(0);

        return this;
    }

    public MarkerBuilder label(char label) {
        mLabel = label;

        return this;
    }

    public String build() {
        StringBuilder result = new StringBuilder();

        if (mColor != null) {
            result.append("color:0x").append(mColor).append(VALUE_SEPARATOR);
        }

        if (mLabel != null) {
            result.append("label:").append(mLabel).append(VALUE_SEPARATOR);
        }

        switch (mSize) {
            case SMALL:
                result.append("size:small").append(VALUE_SEPARATOR);
                break;
            case MID:
                result.append("size:mid").append(VALUE_SEPARATOR);
                break;
            case TINY:
                result.append("size:tiny").append(VALUE_SEPARATOR);
                break;
        }

        result.append(mLatitude).append(",").append(mLongitude);

        return result.toString();
    }

    enum MarkerSize {
        NORMAL,
        SMALL,
        MID,
        TINY
    }
}