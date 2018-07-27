package com.brainsocket.mainlibrary.SupportViews.RecyclerViewLayoutManager;

import android.content.Context;
import android.support.v7.widget.OrientationHelper;
import android.util.AttributeSet;

import com.google.android.flexbox.FlexboxLayoutManager;

/**
 * Created by Adhamkh on 2018-07-27.
 */

public class FlexibleFlexboxLayoutManager extends FlexboxLayoutManager /*implements IFlexibleLayoutManager*/ {
    public FlexibleFlexboxLayoutManager(Context context) {
        super(context);
    }

    public FlexibleFlexboxLayoutManager(Context context, int flexDirection) {
        super(context, flexDirection);
    }

    public FlexibleFlexboxLayoutManager(Context context, int flexDirection, int flexWrap) {
        super(context, flexDirection, flexWrap);
    }

    public FlexibleFlexboxLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /*
    @Override
    public int getOrientation() {
        return OrientationHelper.VERTICAL;
    }

    @Override
    public int getSpanCount() {
        return 1;
    }
    */
}
