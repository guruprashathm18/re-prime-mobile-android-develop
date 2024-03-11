package com.royalenfield.reprime.base.viewpager;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author BOP1KOR on 11/19/2018.
 * <p>
 * {@link ViewPager} class for showing the splash screen slides.
 */

public class REViewPager extends REBaseViewPager implements ViewPager.PageTransformer {

    /**
     * Constructor with a context.
     *
     * @param context The context.
     */
    public REViewPager(Context context) {
        this(context, null);
    }

    /**
     * Constructor with a context.
     *
     * @param context The context.
     * @param attrs   The attribute set.
     */
    public REViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        setPageTransformer(true, this);
    }

    /**
     * Apply a property transformation to the given page.
     *
     * @param page     Apply the transformation to this page
     * @param position Position of page relative to the current front-and-center
     *                 position of the pager. 0 is front and center. 1 is one full
     */
    @Override
    public void transformPage(View page, float position) {
        page.setRotation(position * 0);
    }
}
