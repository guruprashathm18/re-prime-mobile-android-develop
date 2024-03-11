package com.royalenfield.reprime.base.viewpager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @author BOP1KOR on 11/19/2018.
 * <p>
 * Base {@link ViewPager} for all {@link ViewPager}s used in this application.
 */

public class REBaseViewPager extends ViewPager {

    //Holds whether paging is required or not.
    private boolean enabled;

    /**
     * Constructor with a context.
     *
     * @param context The context.
     */
    public REBaseViewPager(@NonNull Context context) {
        super(context);
    }

    /**
     * Constructor with a context.
     *
     * @param context The context.
     * @param attrs   The attribute set.
     */
    public REBaseViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.enabled = true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.enabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.enabled && super.onInterceptTouchEvent(event);
    }

    /**
     * Sets whether paging is enabled or not.
     *
     * @param enabled boolean indicating paging.
     */
    public void setPagingEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
