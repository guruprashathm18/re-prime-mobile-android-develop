package com.royalenfield.reprime.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;


/**
 * This class is used for setting fonts programatically
 * usinh Typeface
 */
public class RECustomTyperFaceSpan extends MetricAffectingSpan {

    private final Typeface typeface;

    public RECustomTyperFaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    private static void applyCustomTypeFace(Paint paint, Typeface tf) {
        paint.setTypeface(tf);
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        applyCustomTypeFace(ds, typeface);
    }

    @Override
    public void updateMeasureState(TextPaint paint) {
        applyCustomTypeFace(paint, typeface);
    }
}
