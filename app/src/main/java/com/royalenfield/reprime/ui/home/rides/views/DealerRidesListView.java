package com.royalenfield.reprime.ui.home.rides.views;

import android.content.Context;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * @author BOP1KOR on 3/26/2019.
 * <p>
 * {@link RecyclerView} class responsible for displaying list of dealer rides.
 */
public class DealerRidesListView extends RecyclerView {
    private static final String TAG = DealerRidesListView.class.getSimpleName();

    //LayoutManager instance.
    private LinearLayoutManager mLayoutManager;


    /**
     * Constructor with a context.
     *
     * @param context The context.
     */
    public DealerRidesListView(Context context) {
        this(context, null, 0);
    }

    /**
     * Constructor with a context and attribute set.
     *
     * @param context The context.
     * @param attrs   The attribute set.
     */
    public DealerRidesListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    /**
     * Constructor with a context, attribute set and style parameter.
     *
     * @param context      The context.
     * @param attrs        The attribute set.
     * @param defStyleAttr The style parameters.
     */
    public DealerRidesListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    @Override
    public void setAdapter(Adapter adapter) {
        super.setAdapter(adapter);
     /*   if (adapter instanceof DealerRidesListAdapter) {
            ((DealerRidesListAdapter) adapter).setOnItemClickListener(onItemClickListener);
        }*/
    }


    /**
     * Initializes the widget.
     */
    private void init() {
        mLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        setLayoutManager(mLayoutManager);
    }

}
