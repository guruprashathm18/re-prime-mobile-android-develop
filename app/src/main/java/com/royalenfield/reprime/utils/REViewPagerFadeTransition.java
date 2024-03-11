package com.royalenfield.reprime.utils;

import androidx.viewpager.widget.ViewPager;
import android.view.View;

/**
 * A PageTransformer is invoked whenever a visible/attached page is scrolled.
 * This offers an opportunity for the application to apply a custom transformation
 * to the page views using animation properties.
 */
public class REViewPagerFadeTransition implements ViewPager.PageTransformer {
    @Override
    public void transformPage(View view, float position) {
        view.setTranslationX(view.getWidth() * -position);
        if (position <= -1.0F || position >= 1.0F) {
            view.setAlpha(0.0F);
        } else if (position == 0.0F) {
            view.setAlpha(1.0F);
        } else {
            // position is between -1.0F & 0.0F OR 0.0F & 1.0F
            view.setAlpha(1.0F - Math.abs(position));
        }
    }
}
