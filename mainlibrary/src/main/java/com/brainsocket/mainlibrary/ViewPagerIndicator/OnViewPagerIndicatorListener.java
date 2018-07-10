package com.brainsocket.mainlibrary.ViewPagerIndicator;

import android.support.v4.view.ViewPager;

/**
 * Created by Adhamkh on 2017-10-23.
 */

public interface OnViewPagerIndicatorListener {
    void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    /**
     * This method will be invoked when a new page becomes selected. Animation is not
     * necessarily complete.
     *
     * @param position Position index of the new selected page.
     */
    void onPageSelected(int position);

    /**
     * Called when the scroll state changes. Useful for discovering when the user
     * begins dragging, when the pager is automatically settling to the current page,
     * or when it is fully stopped/idle.
     *
     * @param state The new scroll state.
     * @see ViewPager#SCROLL_STATE_IDLE
     * @see ViewPager#SCROLL_STATE_DRAGGING
     * @see ViewPager#SCROLL_STATE_SETTLING
     */
    void onPageScrollStateChanged(int state);
}
