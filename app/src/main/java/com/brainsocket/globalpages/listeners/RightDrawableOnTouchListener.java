package com.brainsocket.globalpages.listeners;

import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CompoundButton;


public abstract class RightDrawableOnTouchListener implements View.OnTouchListener {
    Drawable drawable;
    private int fuzz = 10;

    public RightDrawableOnTouchListener(CompoundButton view) {
        super();
        Drawable[] drawables = view.getCompoundDrawables();
        if (android.os.Build.VERSION.SDK_INT >= 17)
            drawables = view.getCompoundDrawablesRelative();
        if (drawables != null && drawables.length == 4)
            this.drawable = drawables[2];
    }

    /*
     * (non-Javadoc)
     * 
     * @see android.view.View.OnTouchListener#onTouch(android.view.View, android.view.MotionEvent)
     */
    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && drawable != null) {
            final int x = (int) event.getX();
            final int y = (int) event.getY();
            final Rect bounds = drawable.getBounds();

            boolean res = (x >= (v.getRight() - bounds.width() - fuzz) && x <= (v.getRight() - v.getPaddingRight() + fuzz)
                    && y >= (v.getPaddingTop() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom()) + fuzz);

//            int xx = v.getLeft() + v.getPaddingLeft() + fuzz;
//            int lf = v.getLeft();
//            int lfp = v.getPaddingLeft();
//            int sfp = v.getPaddingStart();

            if (Build.VERSION.SDK_INT >= 17) {
                Configuration config = v.getContext().getResources().getConfiguration();
                if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
                    res = (x >= (0) && x <= (v.getLeft() + v.getPaddingStart() + 5 * fuzz)
                            && y >= (v.getPaddingTop() - fuzz) && y <= (v.getHeight() - v.getPaddingBottom()) + fuzz);
                }
            }

            if (res) {
                return onDrawableTouch(event);
            }

        }
        return false;
    }

    public abstract boolean onDrawableTouch(final MotionEvent event);

}