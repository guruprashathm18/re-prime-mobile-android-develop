package com.royalenfield.reprime.utils;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;


/**
 * This class is used for setting fonts programatically
 * usinh Typeface
 */
public class RECustomTyperFaceSpanAndColor extends MetricAffectingSpan {

    private final Typeface typeface;
    private  int color= Color.BLACK;

    public RECustomTyperFaceSpanAndColor(Typeface typeface,int color) {
        this.typeface = typeface;
        this.color=color;
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf,int color) {
        paint.setColor(color);
        paint.setTypeface(tf);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, typeface,color);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, typeface,color);
    }
}
